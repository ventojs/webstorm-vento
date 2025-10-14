/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.lang.PsiBuilder
import org.js.vento.plugin.lexer.LexerTokens

fun parsSet(builder: PsiBuilder) {
    val m = builder.mark()

    expect(builder, LexerTokens.SET_START, "Expected '{{' ")
    expect(builder, LexerTokens.SET_KEY, "Expected 'set' keyword")
    expect(builder, LexerTokens.IDENTIFIER, "Expected identifier")

    val hasEq = optional(builder, LexerTokens.EQUAL, "Expected '=' keyword")

    val hasExp: Boolean = parseExpression(builder, hasEq)

    if (hasEq && !hasExp) builder.error("Expected expression after '='")
    if (!hasEq && hasExp) builder.error("Expected '='")

    parsePipe(builder)

    expect(builder, LexerTokens.SET_END, "Expected '}}' ")

    m.done(ParserElements.SET_ELEMENT)
}
