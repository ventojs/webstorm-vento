/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.lang.PsiBuilder
import org.js.vento.plugin.lexer.LexerTokens

fun parseArray(builder: PsiBuilder) {
    val m = builder.mark()

    expect(builder, LexerTokens.BRACKET, "Expected bracket", false) { it.trim() == "[" }

    if (builder.tokenType == LexerTokens.BRACKET && builder.tokenText?.trim() == "[") {
        parseArray(builder)
    } else if (builder.tokenType != LexerTokens.BRACKET && builder.tokenText?.trim() != "]") {
        parseExpression(builder)
    }
    while (!builder.eof() && builder.tokenType == LexerTokens.COMMA) {
        builder.advanceLexer()
        parseExpression(builder)
    }

    expect(builder, LexerTokens.BRACKET, "Expected bracket", false) { it.trim() == "]" }

    m.done(ParserElements.ARRAY_ELEMENT)
}
