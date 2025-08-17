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

import com.intellij.ui.JBColor
import java.awt.Color

@Suppress("MemberVisibilityCanBePrivate")
object CommonColors {

    val DARK_RED: Color = JBColor.decode("#AA0000")
    val RED: Color = JBColor.decode("#FF5555")
    val GOLD: Color = JBColor.decode("#FFAA00")
    val YELLOW: Color = JBColor.decode("#FFFF55")
    val DARK_GREEN: Color = JBColor.decode("#00AA00")
    val GREEN: Color = JBColor.decode("#55FF55")
    val AQUA: Color = JBColor.decode("#55FFFF")
    val DARK_AQUA: Color = JBColor.decode("#00AAAA")
    val DARK_BLUE: Color = JBColor.decode("#0000AA")
    val BLUE: Color = JBColor.decode("#5555FF")
    val LIGHT_PURPLE: Color = JBColor.decode("#FF55FF")
    val DARK_PURPLE: Color = JBColor.decode("#AA00AA")
    val WHITE: Color = JBColor.decode("#FFFFFF")
    val GRAY: Color = JBColor.decode("#AAAAAA")
    val DARK_GRAY: Color = JBColor.decode("#555555")
    val BLACK: Color = JBColor.decode("#000000")

    fun applyStandardColors(map: MutableMap<String, Color>, prefix: String) {
        map.apply {
            put("$prefix.DARK_RED", DARK_RED)
            put("$prefix.RED", RED)
            put("$prefix.GOLD", GOLD)
            put("$prefix.YELLOW", YELLOW)
            put("$prefix.DARK_GREEN", DARK_GREEN)
            put("$prefix.GREEN", GREEN)
            put("$prefix.AQUA", AQUA)
            put("$prefix.DARK_AQUA", DARK_AQUA)
            put("$prefix.DARK_BLUE", DARK_BLUE)
            put("$prefix.BLUE", BLUE)
            put("$prefix.LIGHT_PURPLE", LIGHT_PURPLE)
            put("$prefix.DARK_PURPLE", DARK_PURPLE)
            put("$prefix.WHITE", WHITE)
            put("$prefix.GRAY", GRAY)
            put("$prefix.DARK_GRAY", DARK_GRAY)
            put("$prefix.BLACK", BLACK)
        }
    }
}
