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

package com.demonwav.mcdev.insight

import com.demonwav.mcdev.asset.GeneralAssets
import com.demonwav.mcdev.asset.MCDevBundle
import com.demonwav.mcdev.facet.MinecraftFacet
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProviderDescriptor
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.openapi.module.ModuleUtilCore
import com.intellij.psi.PsiElement
import com.intellij.util.FunctionUtil

class PluginLineMarkerProvider : LineMarkerProviderDescriptor() {

    override fun getName() = MCDevBundle("insight.plugin.marker")

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
        if (!element.isValid) {
            return null
        }

        val module = ModuleUtilCore.findModuleForPsiElement(element) ?: return null

        val instance = MinecraftFacet.getInstance(module) ?: return null

        if (!instance.shouldShowPluginIcon(element)) {
            return null
        }

        val a11yText = MCDevBundle("insight.plugin.marker.accessible_name")

        @Suppress("MoveLambdaOutsideParentheses")
        return LineMarkerInfo(
            element,
            element.textRange,
            GeneralAssets.PLUGIN,
            FunctionUtil.nullConstant(),
            null,
            GutterIconRenderer.Alignment.RIGHT,
            { a11yText },
        )
    }
}
