/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.lang.PsiBuilder
import org.js.vento.plugin.lexer.LexerTokens

fun parseDefault(builder: PsiBuilder) {
    val m = builder.mark()

    expect(builder, LexerTokens.DEFAULT_KEY, "Expected 'default' keyword")
    expect(builder, LexerTokens.SYMBOL, "Expected identifier")

    val hasEq = optional(builder, LexerTokens.EQUAL, "Expected '=' keyword")
    val hasExp =
        if (builder.tokenType == LexerTokens.ASYNC_KEY ||
            builder.tokenType == LexerTokens.FUNCTION_KEY ||
            (builder.tokenType == LexerTokens.PARENTHESIS && builder.rawLookup(1) == LexerTokens.FUNCTION_KEY)
        ) {
            val iife = optional(builder, LexerTokens.PARENTHESIS, "Expected '('") { it.trim() == "(" }
            val hasFunction = parseFunction(builder)
            if (iife) {
                expect(builder, LexerTokens.PARENTHESIS, "Expected ')'") { it.trim() == ")" }
                parseFunctionArguments(builder)
            }
            hasFunction
        } else if (builder.tokenType == LexerTokens.PARENTHESIS) {
            parseFunctionArguments(builder)
            expect(builder, LexerTokens.LAMBDA_ARROW, "Expected '=>'")
            if (builder.tokenType == LexerTokens.BRACE) {
                parseFunctionBody(builder)
            } else {
                parseJavaScriptExpression(builder)
            }
            true
        } else {
            parseJavaScriptExpression(builder)
        }

    if (hasEq && !hasExp) builder.error("Expected expression after '='")
    if (!hasEq && hasExp) builder.error("Expected '='")

    parsePipe(builder)

    closeOrError(builder, "syntax error: default symbol | default symbol = expression")

    m.done(ParserElements.DEFAULT_ELEMENT)
}

fun parseDefaultClose(builder: PsiBuilder) {
    val m = builder.mark()
    expect(builder, LexerTokens.DEFAULT_CLOSE_KEY, "Expected '/default' keyword")
    closeOrError(builder, "syntax error: /default ")
    m.done(ParserElements.DEFAULT_CLOSE_ELEMENT)
}
