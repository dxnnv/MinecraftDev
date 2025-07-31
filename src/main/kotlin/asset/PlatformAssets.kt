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

package com.demonwav.mcdev.asset

import com.intellij.util.IconUtil

@Suppress("unused")
object PlatformAssets : Assets() {
    val MINECRAFT_ICON = loadIcon("/assets/icons/platform/Minecraft.png")
    val MINECRAFT_ICON_2X = loadIcon("/assets/icons/platform/Minecraft@2x.png")

    val ADVENTURE_ICON = loadIcon("/assets/icons/platform/Adventure.png")
    val ADVENTURE_ICON_2X = loadIcon("/assets/icons/platform/Adventure@2x.png")

    val BUKKIT_ICON = loadIcon("/assets/icons/platform/Bukkit.png")
    val BUKKIT_ICON_2X = loadIcon("/assets/icons/platform/Bukkit@2x.png")
    val SPIGOT_ICON = loadIcon("/assets/icons/platform/Spigot.png")
    val SPIGOT_ICON_2X = loadIcon("/assets/icons/platform/Spigot@2x.png")
    val PAPER_ICON = loadIcon("/assets/icons/platform/Paper.png")
    val PAPER_ICON_2X = loadIcon("/assets/icons/platform/Paper@2x.png")

    val VELOCITY_ICON = loadIcon("/assets/icons/platform/Velocity.png")
    val VELOCITY_ICON_2X = loadIcon("/assets/icons/platform/Velocity@2x.png")

    val MCP_ICON = loadIcon("/assets/icons/platform/MCP.png")
    val MCP_ICON_2X = loadIcon("/assets/icons/platform/MCP@2x.png")
    val MCP_ICON_DARK = loadIcon("/assets/icons/platform/MCP_dark.png")
    val MCP_ICON_2X_DARK = loadIcon("/assets/icons/platform/MCP@2x_dark.png")
}
