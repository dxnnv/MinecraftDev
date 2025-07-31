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

package com.demonwav.mcdev.platform

import com.demonwav.mcdev.platform.adventure.AdventureModuleType
import com.demonwav.mcdev.platform.adventure.framework.ADVENTURE_LIBRARY_KIND
import com.demonwav.mcdev.platform.bukkit.BukkitModuleType
import com.demonwav.mcdev.platform.bukkit.PaperModuleType
import com.demonwav.mcdev.platform.bukkit.SpigotModuleType
import com.demonwav.mcdev.platform.bukkit.framework.BUKKIT_LIBRARY_KIND
import com.demonwav.mcdev.platform.bukkit.framework.PAPER_LIBRARY_KIND
import com.demonwav.mcdev.platform.bukkit.framework.SPIGOT_LIBRARY_KIND
import com.demonwav.mcdev.platform.velocity.VelocityModuleType
import com.demonwav.mcdev.platform.velocity.framework.VELOCITY_LIBRARY_KIND
import com.intellij.openapi.roots.libraries.LibraryKind

enum class PlatformType(
    val type: AbstractModuleType<*>,
    val versionJson: String? = null,
    private val parent: PlatformType? = null,
) {

    BUKKIT(BukkitModuleType, "bukkit.json"),
    SPIGOT(SpigotModuleType, "spigot.json", BUKKIT),
    PAPER(PaperModuleType, "paper.json", SPIGOT),
    VELOCITY(VelocityModuleType, "velocity.json"),
    ADVENTURE(AdventureModuleType),
    ;

    private val children = mutableListOf<PlatformType>()

    init {
        parent?.addChild(this)
    }

    private fun addChild(child: PlatformType) {
        children += child
        parent?.addChild(child)
    }

    companion object {
        fun removeParents(types: Set<PlatformType?>) =
            types.asSequence()
                .filterNotNull()
                .filter { type -> type.children.isEmpty() || types.none { type.children.contains(it) } }
                .toHashSet()

        fun fromLibraryKind(kind: LibraryKind) = when (kind) {
            BUKKIT_LIBRARY_KIND -> BUKKIT
            SPIGOT_LIBRARY_KIND -> SPIGOT
            PAPER_LIBRARY_KIND -> PAPER
            VELOCITY_LIBRARY_KIND -> VELOCITY
            ADVENTURE_LIBRARY_KIND -> ADVENTURE
            else -> null
        }
    }
}
