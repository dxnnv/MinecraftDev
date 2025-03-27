/*
 * Minecraft Development for IntelliJ
 *
 * https://mcdev.io/
 *
 * Copyright (C) 2025 minecraft-dev
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, version 3.0 only.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.demonwav.mcdev.platform.mixin.action

import com.demonwav.mcdev.platform.mixin.util.MixinConstants
import com.demonwav.mcdev.platform.mixin.util.findMethods
import com.demonwav.mcdev.platform.mixin.util.findOrConstructSourceMethod
import com.demonwav.mcdev.util.MinecraftTemplates.Companion.MIXIN_OVERWRITE_FALLBACK
import com.demonwav.mcdev.util.findContainingClass
import com.demonwav.mcdev.util.generationInfoFromMethod
import com.demonwav.mcdev.util.ifEmpty
import com.demonwav.mcdev.util.toTypedArray
import com.intellij.codeInsight.generation.GenerateMembersUtil
import com.intellij.codeInsight.generation.OverrideImplementUtil
import com.intellij.codeInsight.generation.PsiMethodMember
import com.intellij.codeInsight.hint.HintManager
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.util.MemberChooser
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiMember
import com.intellij.psi.PsiMethod
import com.intellij.psi.codeStyle.CodeStyleManager

class GenerateOverwriteAction : MixinCodeInsightAction() {

    override fun invoke(project: Project, editor: Editor, file: PsiFile) {
        val offset = editor.caretModel.offset
        val psiClass = file.findElementAt(offset)?.findContainingClass() ?: return
        val methods = findMethods(psiClass, allowClinit = false)
            ?.map { classAndMethodNode ->
                PsiMethodMember(
                    classAndMethodNode.method.findOrConstructSourceMethod(
                        classAndMethodNode.clazz,
                        project,
                        canDecompile = true,
                    ),
                )
            }?.toTypedArray() ?: return

        if (methods.isEmpty()) {
            HintManager.getInstance().showErrorHint(editor, "No methods to overwrite have been found")
            return
        }

        val chooser = MemberChooser(methods, false, true, project)
        chooser.title = "Select Methods to Overwrite"
        chooser.setCopyJavadocVisible(false)
        chooser.show()

        val elements = (chooser.selectedElements ?: return).ifEmpty { return }

        val requiredMembers = LinkedHashSet<PsiMember>()

        runWriteAction {
            val newMethods = elements.map {
                val method = it.element
                val sourceClass = method.containingClass
                val codeBlock = method.body

                val newMethod: PsiMethod
                if (sourceClass != null && codeBlock != null) {
                    // Source of method is available

                    // Collect all references to potential @Shadow members
                    requiredMembers.addAll(collectRequiredMembers(codeBlock, sourceClass))
                }

                // Create temporary (dummy) method
                var tmpMethod =
                    JavaPsiFacade.getElementFactory(project).createMethod(method.name, method.returnType!!, psiClass)

                // Replace temporary method with a copy of the original method
                tmpMethod = tmpMethod.replace(method) as PsiMethod

                // Remove Javadocs
                OverrideImplementUtil.deleteDocComment(tmpMethod)

                // Reformat the code with the project settings
                newMethod = CodeStyleManager.getInstance(project).reformat(tmpMethod) as PsiMethod

                if (codeBlock == null) {
                    // Generate fallback method body if source is not available
                    OverrideImplementUtil.setupMethodBody(
                        newMethod,
                        method,
                        psiClass,
                        FileTemplateManager.getInstance(project).getCodeTemplate(MIXIN_OVERWRITE_FALLBACK),
                    )
                }

                // TODO: Automatically add Javadoc comment for @Overwrite? - yes please

                // Add @Overwrite annotation
                val annotation = newMethod.modifierList.addAnnotation(MixinConstants.Annotations.OVERWRITE)
                generationInfoFromMethod(method, annotation, newMethod)
            }

            // Insert new methods
            GenerateMembersUtil.insertMembersAtOffset(file, offset, newMethods)
                // Select first element in editor
                .first().positionCaret(editor, true)
        }

        if (!psiClass.isValid) {
            return
        }

        disableAnnotationWrapping(project) {
            runWriteAction {
                // Generate needed shadows
                val newShadows = createShadowMembers(project, psiClass, filterNewShadows(requiredMembers, psiClass))
                // Insert shadows
                insertShadows(psiClass, newShadows)
            }
        }
    }
}
