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

package com.demonwav.mcdev.util

import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleUtilCore
import com.intellij.openapi.util.Key
import com.intellij.psi.ElementManipulator
import com.intellij.psi.ElementManipulators
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiEllipsisType
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiMember
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiType
import com.intellij.psi.filters.ElementFilter
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.PsiTypesUtil
import com.intellij.psi.util.TypeConversionUtil

// Parent
fun PsiElement.findModule(): Module? = ModuleUtilCore.findModuleForPsiElement(this)

fun PsiElement.findContainingClass(): PsiClass? = findParent(resolveReferences = false)

fun PsiElement.findContainingMethod(): PsiMethod? = findParent(resolveReferences = false) { it is PsiClass }

private inline fun <reified T : PsiElement> PsiElement.findParent(
    resolveReferences: Boolean,
    stop: (PsiElement) -> Boolean = { false },
): T? {
    var el: PsiElement = this

    while (true) {
        if (resolveReferences && el is PsiReference) {
            el = el.resolve() ?: return null
        }

        if (el is T) {
            return el
        }

        if (el is PsiFile || el is PsiDirectory || stop(el)) {
            return null
        }

        el = el.parent ?: return null
    }
}

inline fun <reified T : PsiElement> PsiElement.childrenOfType(): Collection<T> =
    PsiTreeUtil.findChildrenOfType(this, T::class.java)

fun <T : Any> Sequence<T>.filter(filter: ElementFilter?, context: PsiElement): Sequence<T> {
    filter ?: return this
    return filter { filter.isAcceptable(it, context) }
}

val PsiElement.constantValue: Any?
    get() = JavaPsiFacade.getInstance(project).constantEvaluationHelper.computeConstantExpression(this)

val PsiElement.constantStringValue: String?
    get() = constantValue as? String

infix fun PsiElement.equivalentTo(other: PsiElement?): Boolean {
    return manager.areElementsEquivalent(this, other)
}

fun PsiType?.isErasureEquivalentTo(other: PsiType?): Boolean {
    return this?.normalize() == other?.normalize()
}

fun PsiType.normalize(): PsiType {
    var normalized = TypeConversionUtil.erasure(this)
    if (normalized is PsiEllipsisType) {
        normalized = normalized.toArrayType()
    }
    return normalized
}

val <T : PsiElement> T.manipulator: ElementManipulator<T>?
    get() = ElementManipulators.getManipulator(this)

private val REAL_NAME_KEY = Key<String>("mcdev.real_name")

var PsiMember.realName: String?
    get() = getUserData(REAL_NAME_KEY)
    set(value) = putUserData(REAL_NAME_KEY, value)

val PsiClass.psiType: PsiType
    get() = PsiTypesUtil.getClassType(this)

