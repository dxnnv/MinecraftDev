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

package com.demonwav.mcdev.platform.mixin.insight.target

import com.demonwav.mcdev.asset.MCDevBundle
import com.demonwav.mcdev.platform.mixin.action.FindMixinsAction
import com.demonwav.mcdev.platform.mixin.util.isAccessorMixin
import com.demonwav.mcdev.util.findReferencedClass
import com.intellij.codeInsight.codeVision.CodeVisionRelativeOrdering
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import java.awt.event.MouseEvent

class AccessorTargetCodeVisionProvider : AbstractMixinTargetCodeVisionProvider() {
    override val id = "mcdev.mixin.target.accessor"
    override val name: String
        get() = MCDevBundle("mixin.codeVision.target.accessor.name")
    override val relativeOrderings: List<CodeVisionRelativeOrdering>
        get() = super.relativeOrderings +
            CodeVisionRelativeOrdering.CodeVisionRelativeOrderingAfter(MixinTargetCodeVisionProvider.ID)

    override fun acceptsElement(element: PsiElement) = element is PsiClass

    override fun getHint(element: PsiElement, file: PsiFile): String? {
        val targetClass = element as? PsiClass ?: return null
        val numberOfMixins = FindMixinsAction.findMixins(targetClass, element.project)?.count { it.isAccessorMixin }
            ?: return null
        if (numberOfMixins == 0) {
            return null
        }
        return MCDevBundle("mixin.codeVision.target.accessor.hint", numberOfMixins)
    }

    override fun handleClick(editor: Editor, element: PsiElement, event: MouseEvent?) {
        val project = editor.project ?: return
        val file = element.containingFile ?: return
        val targetClass = element.findReferencedClass() ?: return
        FindMixinsAction.openFindMixinsUI(project, editor, file, targetClass) { it.isAccessorMixin }
    }
}
