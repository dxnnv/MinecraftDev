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

package com.demonwav.mcdev.platform.mixin.inspection.injector

import com.demonwav.mcdev.platform.mixin.inspection.MixinInspection
import com.demonwav.mcdev.platform.mixin.util.MixinConstants
import com.intellij.codeInspection.LocalQuickFixOnPsiElement
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.project.Project
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiEnumConstant
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiReferenceExpression

class CaptureFailExceptionInspection : MixinInspection() {
    override fun getStaticDescription() = """
        Usage of <code>LocalCapture.CAPTURE_FAILEXCEPTION</code> is usually a mistake and should be replaced with
        <code>LocalCapture.CAPTURE_FAILHARD</code>. <code>CAPTURE_FAILEXCEPTION</code> generates code which throws an
        exception when the callback is reached, if the locals do not match. If this is really what you want, you can
        suppress this warning.
    """.trimIndent()

    override fun buildVisitor(holder: ProblemsHolder) = object : JavaElementVisitor() {
        override fun visitReferenceExpression(expression: PsiReferenceExpression) {
            if (expression.referenceName != "CAPTURE_FAILEXCEPTION") {
                return
            }
            val resolved = expression.resolve() as? PsiEnumConstant ?: return
            if (resolved.containingClass?.qualifiedName != MixinConstants.Classes.LOCAL_CAPTURE) {
                return
            }

            holder.registerProblem(
                expression,
                "Suspicious usage of CAPTURE_FAILEXCEPTION",
                ReplaceWithCaptureFailHardFix(expression)
            )
        }
    }

    private class ReplaceWithCaptureFailHardFix(
        expression: PsiReferenceExpression
    ) : LocalQuickFixOnPsiElement(expression) {
        override fun getFamilyName() = "Replace with CAPTURE_FAILHARD"

        override fun getText() = "Replace with CAPTURE_FAILHARD"

        override fun invoke(project: Project, file: PsiFile, startElement: PsiElement, endElement: PsiElement) {
            val captureFailHardText = "${MixinConstants.Classes.LOCAL_CAPTURE}.CAPTURE_FAILHARD"
            val captureFailHardExpr =
                JavaPsiFacade.getElementFactory(project).createExpressionFromText(captureFailHardText, startElement)
            startElement.replace(captureFailHardExpr)
        }
    }
}
