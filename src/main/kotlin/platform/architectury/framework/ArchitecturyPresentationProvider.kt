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

package com.demonwav.mcdev.platform.architectury.framework

import com.demonwav.mcdev.asset.PlatformAssets
import com.demonwav.mcdev.util.localFile
import com.intellij.framework.library.LibraryVersionProperties
import com.intellij.openapi.roots.libraries.LibraryPresentationProvider
import com.intellij.openapi.vfs.VirtualFile
import java.util.jar.JarFile

class ArchitecturyPresentationProvider : LibraryPresentationProvider<LibraryVersionProperties>(
    ARCHITECTURY_LIBRARY_KIND,
) {
    override fun getIcon(properties: LibraryVersionProperties?) = PlatformAssets.ARCHITECTURY_ICON

    override fun detect(classesRoots: MutableList<VirtualFile>): LibraryVersionProperties? {
        for (classesRoot in classesRoots) {
            if (classesRoot.name.endsWith(".jar")) {
                runCatching {
                    val jar = JarFile(classesRoot.localFile)
                    val isArchitecturyLib = jar.entries().asSequence().any {
                        it.name == "architectury.common.json"
                    }
                    if (isArchitecturyLib) {
                        return LibraryVersionProperties()
                    }
                }
            }
        }
        return null
    }
}
