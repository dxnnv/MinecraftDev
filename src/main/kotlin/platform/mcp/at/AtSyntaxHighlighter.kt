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

package com.demonwav.mcdev.platform.mcp.at

import com.demonwav.mcdev.platform.mcp.at.gen.psi.AtTypes
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType

class AtSyntaxHighlighter : SyntaxHighlighterBase() {

    override fun getHighlightingLexer() = AtLexerAdapter()

    override fun getTokenHighlights(tokenType: IElementType?): Array<TextAttributesKey> =
        when (tokenType) {
            AtTypes.KEYWORD_ELEMENT -> KEYWORD_KEYS
            AtTypes.CLASS_NAME_ELEMENT -> CLASS_NAME_KEYS
            AtTypes.CLASS_VALUE -> CLASS_VALUE_KEYS
            AtTypes.NAME_ELEMENT -> ELEMENT_NAME_KEYS
            AtTypes.ASTERISK_ELEMENT -> ASTERISK_KEYS
            AtTypes.PRIMITIVE -> PRIMITIVE_KEYS
            AtTypes.COMMENT -> COMMENT_KEYS
            TokenType.BAD_CHARACTER -> BAD_CHARACTER_KEYS
            else -> TextAttributesKey.EMPTY_ARRAY
        }

    companion object {
        val KEYWORD = TextAttributesKey.createTextAttributesKey("AT_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)
        val CLASS_NAME =
            TextAttributesKey.createTextAttributesKey("AT_CLASS_NAME", DefaultLanguageHighlighterColors.STRING)
        val CLASS_VALUE =
            TextAttributesKey.createTextAttributesKey("AT_CLASS_VALUE", DefaultLanguageHighlighterColors.STATIC_METHOD)
        val ELEMENT_NAME =
            TextAttributesKey.createTextAttributesKey("AT_ELEMENT_NAME", DefaultLanguageHighlighterColors.STATIC_FIELD)
        val ASTERISK =
            TextAttributesKey.createTextAttributesKey("AT_ASTERISK", DefaultLanguageHighlighterColors.KEYWORD)
        val PRIMITIVE =
            TextAttributesKey.createTextAttributesKey("AT_PRIMITIVE", DefaultLanguageHighlighterColors.NUMBER)
        val COMMENT =
            TextAttributesKey.createTextAttributesKey("AT_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)
        val BAD_CHARACTER =
            TextAttributesKey.createTextAttributesKey("AT_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER)

        private val KEYWORD_KEYS = arrayOf(KEYWORD)
        private val CLASS_NAME_KEYS = arrayOf(CLASS_NAME)
        private val CLASS_VALUE_KEYS = arrayOf(CLASS_VALUE)
        private val ELEMENT_NAME_KEYS = arrayOf(ELEMENT_NAME)
        private val ASTERISK_KEYS = arrayOf(ASTERISK)
        private val PRIMITIVE_KEYS = arrayOf(PRIMITIVE)
        private val COMMENT_KEYS = arrayOf(COMMENT)
        private val BAD_CHARACTER_KEYS = arrayOf(BAD_CHARACTER)
    }
}
