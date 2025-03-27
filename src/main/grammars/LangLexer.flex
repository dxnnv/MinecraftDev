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

package com.demonwav.mcdev.translations.lang.gen;

import com.intellij.lexer.*;
import com.intellij.psi.tree.IElementType;
import static com.demonwav.mcdev.translations.lang.gen.psi.LangTypes.*;
import static com.intellij.psi.TokenType.*;

%%

%{
    public LangLexer() {
        this((java.io.Reader)null);
    }
%}

%public
%class LangLexer
%implements FlexLexer
%function advance
%type IElementType

%s WAITING_VALUE
%s WAITING_EQUALS

%unicode

EOL_WS              = \n | \r | \r\n
LINE_ENDING         = {EOL_WS}

KEY = [^=#\n\r][^=\n\r]*
VALUE = [^\n\r]+
COMMENT = #[^\n\r]*

%%

<YYINITIAL> {
    {KEY}/"="                   { yybegin(WAITING_EQUALS); return KEY; }
    {KEY}                       { return DUMMY; }
    {COMMENT}                   { return COMMENT; }
    {LINE_ENDING}               { return LINE_ENDING; }
}

<WAITING_EQUALS> {
    "="                         { yybegin(WAITING_VALUE); return EQUALS; }
}

<WAITING_VALUE> {
    {LINE_ENDING}               { yybegin(YYINITIAL); return LINE_ENDING; }
    {VALUE}                     { yybegin(YYINITIAL); return VALUE; }
}

[^]                             { return BAD_CHARACTER; }
