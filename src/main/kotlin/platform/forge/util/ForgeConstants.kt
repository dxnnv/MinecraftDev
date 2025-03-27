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

package com.demonwav.mcdev.platform.forge.util

import com.demonwav.mcdev.util.SemanticVersion

object ForgeConstants {

    const val SIDED_PROXY_ANNOTATION = "net.minecraftforge.fml.common.SidedProxy"
    const val MOD_ANNOTATION = "net.minecraftforge.fml.common.Mod"
    const val CORE_MOD_INTERFACE = "net.minecraftforge.fml.relauncher.IFMLLoadingPlugin"
    const val EVENT_HANDLER_ANNOTATION = "net.minecraftforge.fml.common.Mod.EventHandler"
    const val SUBSCRIBE_EVENT_ANNOTATION = "net.minecraftforge.fml.common.eventhandler.SubscribeEvent"
    const val EVENTBUS_SUBSCRIBE_EVENT_ANNOTATION = "net.minecraftforge.eventbus.api.SubscribeEvent"
    const val FML_EVENT = "net.minecraftforge.fml.common.event.FMLEvent"
    const val EVENT = "net.minecraftforge.fml.common.eventhandler.Event"
    const val EVENTBUS_EVENT = "net.minecraftforge.eventbus.api.Event"
    const val NETWORK_MESSAGE = "net.minecraftforge.fml.common.network.simpleimpl.IMessage"
    const val NETWORK_MESSAGE_HANDLER = "net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler"
    const val MCMOD_INFO = "mcmod.info"
    const val MODS_TOML = "mods.toml"
    const val PACK_MCMETA = "pack.mcmeta"

    const val JAR_VERSION_VAR = "\${file.jarVersion}"

    // From https://github.com/MinecraftForge/MinecraftForge/blob/0ff8a596fc1ef33d4070be89dd5cb4851f93f731/src/fmllauncher/java/net/minecraftforge/fml/loading/StringSubstitutor.java
    val KNOWN_SUBSTITUTIONS = setOf(JAR_VERSION_VAR, "\${global.mcVersion}", "\${global.forgeVersion}")

    val DISPLAY_TESTS = setOf("MATCH_VERSION", "IGNORE_SERVER_VERSION", "IGNORE_ALL_VERSION", "NONE")
    val DEPENDENCY_SIDES = setOf("BOTH", "CLIENT", "SERVER")
    val DEPENDENCY_ORDER = setOf("NONE", "BEFORE", "AFTER")

    // From https://github.com/MinecraftForge/MinecraftForge/blob/38a5400a8c878fe39cd389e6d4f68619d2738b88/src/fmllauncher/java/net/minecraftforge/fml/loading/moddiscovery/ModInfo.java#L45
    val MOD_ID_REGEX = "^[a-z][a-z0-9_-]{1,63}$".toRegex()

    val DISPLAY_TEST_MANIFEST_VERSION = SemanticVersion.release(40, 2, 14)
    val CLIENT_ONLY_MANIFEST_VERSION = SemanticVersion.release(49, 0, 6)
}
