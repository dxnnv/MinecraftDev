/*
 * Minecraft Development for IntelliJ
 *
 * https://mcdev.io/
 *
 * Copyright (C) 2024 minecraft-dev
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

package com.demonwav.mcdev.asset

@Suppress("unused")
object MixinAssets : Assets() {
    val SHADOW = loadIcon("/assets/icons/mixin/shadow.png")
    val SHADOW_DARK = loadIcon("/assets/icons/mixin/shadow_dark.png")

    val MIXIN_CLASS_ICON = loadIcon("/assets/icons/mixin/mixin_class_gutter.png")
    val MIXIN_CLASS_ICON_DARK = loadIcon("/assets/icons/mixin/mixin_class_gutter_dark.png")

    val MIXIN_MARK = loadIcon("/assets/icons/mixin/mixin_mark.svg")
}
