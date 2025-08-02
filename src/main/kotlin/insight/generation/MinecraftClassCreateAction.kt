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

package com.demonwav.mcdev.insight.generation

import com.demonwav.mcdev.asset.GeneralAssets
import com.demonwav.mcdev.asset.MCDevBundle
import com.intellij.codeInsight.daemon.JavaErrorBundle
import com.intellij.ide.actions.CreateFileFromTemplateDialog
import com.intellij.ide.actions.CreateTemplateInPackageAction
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.InputValidatorEx
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.JavaDirectoryService
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameHelper
import com.intellij.psi.util.PsiTypesUtil
import com.intellij.psi.util.PsiUtil
import org.jetbrains.jps.model.java.JavaModuleSourceRootTypes

class MinecraftClassCreateAction :
    CreateTemplateInPackageAction<PsiClass>(
        CAPTION,
        MCDevBundle("generate.class.description"),
        GeneralAssets.MC_TEMPLATE,
        JavaModuleSourceRootTypes.SOURCES,
    ),
    DumbAware {

    override fun getActionName(directory: PsiDirectory?, newName: String, templateName: String?): String = CAPTION

    override fun buildDialog(project: Project, directory: PsiDirectory, builder: CreateFileFromTemplateDialog.Builder) {
        builder.setTitle(CAPTION)
        builder.setValidator(ClassInputValidator(project, directory))
    }

    override fun isAvailable(dataContext: DataContext): Boolean {
        return super.isAvailable(dataContext)
    }

    override fun checkPackageExists(directory: PsiDirectory): Boolean {
        val pkg = JavaDirectoryService.getInstance().getPackage(directory) ?: return false

        val name = pkg.qualifiedName
        return StringUtil.isEmpty(name) || PsiNameHelper.getInstance(directory.project).isQualifiedName(name)
    }

    override fun getNavigationElement(createdElement: PsiClass): PsiElement? {
        return createdElement.lBrace
    }

    override fun doCreate(dir: PsiDirectory, className: String, templateName: String): PsiClass? {
        return JavaDirectoryService.getInstance().createClass(dir, className, templateName, false)
    }

    private class ClassInputValidator(
        private val project: Project,
        private val directory: PsiDirectory,
    ) : InputValidatorEx {
        override fun getErrorText(inputString: String): String? {
            if (inputString.isNotEmpty() && !PsiNameHelper.getInstance(project).isQualifiedName(inputString)) {
                return JavaErrorBundle.message("create.class.action.this.not.valid.java.qualified.name")
            }

            val shortName = StringUtil.getShortName(inputString)
            val languageLevel = PsiUtil.getLanguageLevel(directory)
            return if (PsiTypesUtil.isRestrictedIdentifier(shortName, languageLevel)) {
                JavaErrorBundle.message("restricted.identifier", shortName)
            } else {
                null
            }
        }

        override fun checkInput(inputString: String): Boolean =
            inputString.isNotBlank() && getErrorText(inputString) == null

        override fun canClose(inputString: String): Boolean =
            checkInput(inputString)
    }

    private companion object {
        private val CAPTION
            get() = MCDevBundle("generate.class.caption")
    }
}
