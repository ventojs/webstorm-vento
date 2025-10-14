/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.lang.PsiBuilder
import org.js.vento.plugin.lexer.LexerTokens

fun parseExport(builder: PsiBuilder) {
    val m = builder.mark()

    expect(builder, LexerTokens.EXPORT_START, "Expected '{{' ")
    expect(builder, LexerTokens.EXPORT_KEY, "Expected 'export' keyword")
    expect(builder, LexerTokens.EXPORT_VAR, "Expected variable", true)

    val hasEq = optional(builder, LexerTokens.EQUAL, "Expected '=' keyword")
    var hasVal = false
    if (hasEq) hasVal = parseExpression(builder)
    if (hasEq && !hasVal) builder.error("Expected expression after '='")

    while (!builder.eof() && builder.tokenType == LexerTokens.PIPE) {
        val hasPipe = optional(builder, LexerTokens.PIPE, "Expected pipe (|>)")
        var hasPipeExpression = false
        if (hasPipe) hasPipeExpression = parseExpression(builder)
        if (hasPipe && !hasPipeExpression) builder.error("Expected expression after '|>'")
    }

    expect(builder, LexerTokens.EXPORT_END, "Expected '}}' ")

    if (hasEq) {
        m.done(ParserElements.EXPORT_ELEMENT)
    } else {
        m.done(ParserElements.EXPORT_OPEN_ELEMENT)
    }
}
