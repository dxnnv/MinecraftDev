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

package com.demonwav.mcdev.platform.mixin.expression.psi.mixins.impl

import com.demonwav.mcdev.platform.mixin.expression.MESourceMatchContext
import com.demonwav.mcdev.platform.mixin.expression.gen.psi.MEArguments
import com.demonwav.mcdev.platform.mixin.expression.gen.psi.MEName
import com.demonwav.mcdev.platform.mixin.expression.gen.psi.impl.MEExpressionImpl
import com.demonwav.mcdev.platform.mixin.handlers.injectionPoint.QualifiedMember
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethodCallExpression
import com.siyeh.ig.psiutils.MethodCallUtils

abstract class MESuperCallExpressionImplMixin(node: ASTNode) : MEExpressionImpl(node) {
    override fun matchesJava(java: PsiElement, context: MESourceMatchContext): Boolean {
        if (java !is PsiMethodCallExpression) {
            return false
        }
        if (!MethodCallUtils.hasSuperQualifier(java)) {
            return false
        }

        val memberName = this.memberName ?: return false
        if (!memberName.isWildcard) {
            val method = java.resolveMethod() ?: return false
            val methodId = memberName.text
            val qualifier =
                QualifiedMember.resolveQualifier(java.methodExpression) ?: method.containingClass ?: return false
            if (context.getMethods(methodId).none { it.matchMethod(method, qualifier) }) {
                return false
            }
        }

        return arguments?.matchesJava(java.argumentList, context) == true
    }

    override fun getInputExprs() = arguments?.expressionList ?: emptyList()

    protected abstract val memberName: MEName?
    protected abstract val arguments: MEArguments?
}
