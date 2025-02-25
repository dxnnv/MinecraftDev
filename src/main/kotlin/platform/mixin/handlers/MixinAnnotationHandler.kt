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

package com.demonwav.mcdev.platform.mixin.handlers

import com.demonwav.mcdev.asset.MixinAssets
import com.demonwav.mcdev.platform.mixin.handlers.injectionPoint.InsnResolutionInfo
import com.demonwav.mcdev.platform.mixin.util.MixinConstants
import com.demonwav.mcdev.platform.mixin.util.MixinTargetMember
import com.demonwav.mcdev.platform.mixin.util.mixinTargets
import com.demonwav.mcdev.util.cached
import com.demonwav.mcdev.util.findAnnotation
import com.demonwav.mcdev.util.findContainingClass
import com.demonwav.mcdev.util.resolveClass
import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.openapi.extensions.RequiredElement
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.KeyedExtensionCollector
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiAnnotation
import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.AnnotatedElementsSearch
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import com.intellij.serviceContainer.BaseKeyedLazyInstance
import com.intellij.util.KeyedLazyInstance
import com.intellij.util.xmlb.annotations.Attribute
import javax.swing.Icon
import org.objectweb.asm.tree.ClassNode

interface MixinAnnotationHandler {
    fun resolveTarget(annotation: PsiAnnotation) = annotation.cached(PsiModificationTracker.MODIFICATION_COUNT) {
        val containingClass = annotation.findContainingClass() ?: return@cached emptyList()
        containingClass.mixinTargets.flatMap { resolveTarget(annotation, it) }
    }

    fun resolveTarget(annotation: PsiAnnotation, targetClass: ClassNode): List<MixinTargetMember>

    fun isUnresolved(annotation: PsiAnnotation): InsnResolutionInfo.Failure? {
        val containingClass = annotation.findContainingClass() ?: return InsnResolutionInfo.Failure()
        return containingClass.mixinTargets
            .mapNotNull { isUnresolved(annotation, it) }
            .reduceOrNull(InsnResolutionInfo.Failure::combine)
    }

    fun isUnresolved(annotation: PsiAnnotation, targetClass: ClassNode): InsnResolutionInfo.Failure?

    fun resolveForNavigation(annotation: PsiAnnotation): List<PsiElement> {
        val containingClass = annotation.findContainingClass() ?: return emptyList()
        return containingClass.mixinTargets.flatMap { resolveForNavigation(annotation, it) }
    }

    fun resolveForNavigation(annotation: PsiAnnotation, targetClass: ClassNode): List<PsiElement>

    fun createUnresolvedMessage(annotation: PsiAnnotation): String?

    /**
     * Returns true if we don't actually know the implementation of the annotation, and we're just making
     * a guess. Prevents unresolved errors but still attempts navigation
     */
    val isSoft: Boolean get() = false

    /**
     * Returns whether elements annotated with this annotation should be considered "entry points",
     * i.e. not reported as unused
     */
    val isEntryPoint: Boolean

    val icon: Icon get() = MixinAssets.MIXIN_ELEMENT_ICON

    companion object {
        private val EP_NAME = ExtensionPointName<KeyedLazyInstance<MixinAnnotationHandler>>(
            "com.demonwav.minecraft-dev.mixinAnnotationHandler",
        )
        private val COLLECTOR = KeyedExtensionCollector<MixinAnnotationHandler, String>(EP_NAME)

        fun getBuiltinHandlers(): Sequence<Pair<String, MixinAnnotationHandler>> =
            EP_NAME.extensions.asSequence().map { it.key to it.instance }

        fun forMixinAnnotation(annotation: PsiAnnotation, project: Project = annotation.project): MixinAnnotationHandler? {
            val qName = annotation.qualifiedName ?: return null
            return forMixinAnnotation(qName, project)
        }

        fun forMixinAnnotation(qualifiedName: String, project: Project? = null): MixinAnnotationHandler? {
            val extension = COLLECTOR.findSingle(qualifiedName)
            if (extension != null) {
                return extension
            }

            if (project != null) {
                val extraMixinAnnotations = CachedValuesManager.getManager(project).getCachedValue(project) {
                    val result = JavaPsiFacade.getInstance(project)
                        .findClass(MixinConstants.Annotations.ANNOTATION_TYPE, GlobalSearchScope.allScope(project))
                        ?.let { annotationType ->
                            AnnotatedElementsSearch.searchPsiClasses(
                                annotationType,
                                GlobalSearchScope.allScope(project),
                            ).mapNotNull { injectionInfoClass ->
                                injectionInfoClass.findAnnotation(MixinConstants.Annotations.ANNOTATION_TYPE)
                                    ?.findAttributeValue("value")
                                    ?.resolveClass()
                                    ?.qualifiedName
                            }.toSet()
                        } ?: emptySet()
                    CachedValueProvider.Result(result, PsiModificationTracker.MODIFICATION_COUNT)
                }
                if (extraMixinAnnotations != null && qualifiedName in extraMixinAnnotations) {
                    return DefaultInjectorAnnotationHandler
                }
            }

            return null
        }
    }
}

class MixinAnnotationHandlerInfo :
    BaseKeyedLazyInstance<MixinAnnotationHandler>(), KeyedLazyInstance<MixinAnnotationHandler> {

    @Attribute("annotation")
    @RequiredElement
    lateinit var annotation: String

    @Attribute("implementation")
    @RequiredElement
    lateinit var implementation: String

    override fun getKey(): String {
        return annotation
    }

    override fun getImplementationClassName(): String {
        return implementation
    }
}
