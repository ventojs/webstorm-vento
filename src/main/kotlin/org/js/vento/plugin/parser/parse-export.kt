/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.lang.PsiBuilder
import org.js.vento.plugin.lexer.LexerTokens

fun parseExport(builder: PsiBuilder) {
    val m = builder.mark()

    expect(builder, LexerTokens.EXPORT_KEY, "Expected 'export' keyword")
    if (builder.tokenType == LexerTokens.FUNCTION_KEY || builder.tokenType == LexerTokens.ASYNC_KEY) {
        parseFunctionSignature(builder, true)
        m.done(ParserElements.EXPORT_OPEN_ELEMENT)
    } else {
        expect(builder, LexerTokens.SYMBOL, "Expected symbol", true)

        val hasEq = optional(builder, LexerTokens.EQUAL, "Expected '=' keyword")
        var hasExpression = false
        if (hasEq) hasExpression = parseJavaScriptExpression(builder)
        if (hasEq && !hasExpression) builder.error("Expected expression after '='")

        while (!builder.eof() && builder.tokenType == LexerTokens.PIPE) {
            val hasPipe = optional(builder, LexerTokens.PIPE, "Expected pipe (|>)")
            var hasPipeExpression = false
            if (hasPipe) hasPipeExpression = parseJavaScriptExpression(builder)
            if (hasPipe && !hasPipeExpression) builder.error("Expected expression after '|>'")
        }

        closeOrError(builder, "syntax error: export symbol | export symbol = expression")

        if (hasEq) {
            m.done(ParserElements.EXPORT_ELEMENT)
        } else {
            m.done(ParserElements.EXPORT_OPEN_ELEMENT)
        }
    }
}

fun parseExportClose(builder: PsiBuilder) {
    val m = builder.mark()

    expect(builder, LexerTokens.EXPORT_CLOSE_KEY, "Expected '/export' keyword")

    m.done(ParserElements.EXPORT_CLOSE_ELEMENT)
}
