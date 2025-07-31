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

import com.demonwav.mcdev.asset.PlatformAssets
import com.intellij.ide.fileTemplates.FileTemplateDescriptor
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptor
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptorFactory

class MinecraftTemplates : FileTemplateGroupDescriptorFactory {

    override fun getFileTemplatesDescriptor(): FileTemplateGroupDescriptor {
        val group = FileTemplateGroupDescriptor("Minecraft", PlatformAssets.MINECRAFT_ICON)

        FileTemplateGroupDescriptor("Bukkit", PlatformAssets.BUKKIT_ICON).let { bukkitGroup ->
            group.addTemplate(bukkitGroup)
            bukkitGroup.addTemplate(FileTemplateDescriptor(BUKKIT_MAIN_CLASS_TEMPLATE))
            bukkitGroup.addTemplate(FileTemplateDescriptor(BUKKIT_PLUGIN_YML_TEMPLATE))
            bukkitGroup.addTemplate(FileTemplateDescriptor(BUKKIT_BUILD_GRADLE_TEMPLATE))
            bukkitGroup.addTemplate(FileTemplateDescriptor(BUKKIT_GRADLE_PROPERTIES_TEMPLATE))
            bukkitGroup.addTemplate(FileTemplateDescriptor(BUKKIT_SETTINGS_GRADLE_TEMPLATE))
            bukkitGroup.addTemplate(FileTemplateDescriptor(BUKKIT_POM_TEMPLATE))
        }

        FileTemplateGroupDescriptor("Paper", PlatformAssets.PAPER_ICON).let { paperGroup ->
            group.addTemplate(paperGroup)
            paperGroup.addTemplate(FileTemplateDescriptor(PAPER_PLUGIN_YML_TEMPLATE))
        }

        FileTemplateGroupDescriptor("Velocity", PlatformAssets.VELOCITY_ICON).let { velocityGroup ->
            group.addTemplate(velocityGroup)
            velocityGroup.addTemplate(FileTemplateDescriptor(VELOCITY_MAIN_CLASS_TEMPLATE))
            velocityGroup.addTemplate(FileTemplateDescriptor(VELOCITY_BUILD_CONSTANTS_TEMPLATE))
            velocityGroup.addTemplate(FileTemplateDescriptor(VELOCITY_MAIN_CLASS_V2_TEMPLATE))
            velocityGroup.addTemplate(FileTemplateDescriptor(VELOCITY_BUILD_GRADLE_TEMPLATE))
            velocityGroup.addTemplate(FileTemplateDescriptor(VELOCITY_GRADLE_PROPERTIES_TEMPLATE))
            velocityGroup.addTemplate(FileTemplateDescriptor(VELOCITY_SETTINGS_GRADLE_TEMPLATE))
            velocityGroup.addTemplate(FileTemplateDescriptor(VELOCITY_POM_TEMPLATE))
        }

        FileTemplateGroupDescriptor("Common", PlatformAssets.MINECRAFT_ICON).let { commonGroup ->
            group.addTemplate(commonGroup)
            commonGroup.addTemplate(FileTemplateDescriptor(GRADLE_GITIGNORE_TEMPLATE))
            commonGroup.addTemplate(FileTemplateDescriptor(MAVEN_GITIGNORE_TEMPLATE))
            commonGroup.addTemplate(FileTemplateDescriptor(GRADLE_WRAPPER_PROPERTIES))
        }

        FileTemplateGroupDescriptor("Licenses", null).let { licenseGroup ->
            group.addTemplate(licenseGroup)
            enumValues<License>().forEach { license ->
                licenseGroup.addTemplate(FileTemplateDescriptor(license.id))
            }
        }

        return group
    }

    companion object {
        const val BUKKIT_MAIN_CLASS_TEMPLATE = "Bukkit Main Class.java"
        const val BUKKIT_PLUGIN_YML_TEMPLATE = "Bukkit plugin.yml"
        const val BUKKIT_BUILD_GRADLE_TEMPLATE = "Bukkit build.gradle"
        const val BUKKIT_GRADLE_PROPERTIES_TEMPLATE = "Bukkit gradle.properties"
        const val BUKKIT_SETTINGS_GRADLE_TEMPLATE = "Bukkit settings.gradle"
        const val BUKKIT_POM_TEMPLATE = "Bukkit pom.xml"

        const val PAPER_PLUGIN_YML_TEMPLATE = "Paper paper-plugin.yml"

        const val VELOCITY_MAIN_CLASS_TEMPLATE = "Velocity Main Class.java"
        const val VELOCITY_MAIN_CLASS_V2_TEMPLATE = "Velocity Main Class V2.java"
        const val VELOCITY_BUILD_CONSTANTS_TEMPLATE = "Velocity Build Constants.java"
        const val VELOCITY_BUILD_GRADLE_TEMPLATE = "Velocity build.gradle"
        const val VELOCITY_GRADLE_PROPERTIES_TEMPLATE = "Velocity gradle.properties"
        const val VELOCITY_SETTINGS_GRADLE_TEMPLATE = "Velocity settings.gradle"
        const val VELOCITY_POM_TEMPLATE = "Velocity pom.xml"

        const val GRADLE_WRAPPER_PROPERTIES = "MinecraftDev gradle-wrapper.properties"
        const val GRADLE_GITIGNORE_TEMPLATE = "Gradle.gitignore"
        const val MAVEN_GITIGNORE_TEMPLATE = "Maven.gitignore"
    }

    private fun template(fileName: String, displayName: String? = null) = CustomDescriptor(fileName, displayName)

    private class CustomDescriptor(fileName: String, val visibleName: String?) : FileTemplateDescriptor(fileName) {
        override fun getDisplayName(): String = visibleName ?: fileName
    }
}
