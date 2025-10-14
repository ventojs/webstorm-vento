/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.lang.PsiBuilder
import org.js.vento.plugin.lexer.LexerTokens

fun parsePipe(builder: PsiBuilder) {
    if (builder.tokenType == LexerTokens.PIPE) {
        expect(builder, LexerTokens.PIPE, "Expected pipe (|>)")
        parseExpression(builder)
    }
}
