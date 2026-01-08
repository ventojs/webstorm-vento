/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.lang.PsiBuilder
import org.js.vento.plugin.lexer.LexerTokens
import org.js.vento.plugin.lexer.LexerTokens.BRACE
import org.js.vento.plugin.lexer.LexerTokens.COLON
import org.js.vento.plugin.lexer.LexerTokens.COMMA
import org.js.vento.plugin.lexer.LexerTokens.EQUAL
import org.js.vento.plugin.lexer.LexerTokens.EXPAND
import org.js.vento.plugin.lexer.LexerTokens.SYMBOL

fun parsSet(builder: PsiBuilder) {
    val m = builder.mark()

    expect(builder, LexerTokens.SET_KEY, "Expected 'set' keyword")
    parseVariable(builder)

    val hasEq = optional(builder, EQUAL, "Expected '=' keyword")
    val hasExp =
        if (builder.tokenType == LexerTokens.ASYNC_KEY ||
            builder.tokenType == LexerTokens.FUNCTION_KEY ||
            (builder.tokenType == LexerTokens.PARENTHESIS && builder.rawLookup(1) == LexerTokens.FUNCTION_KEY)
        ) {
            isFunctionOrIife(builder)
        } else if (builder.tokenType == LexerTokens.PARENTHESIS) {
            parseFunctionArguments(builder)
            expect(builder, LexerTokens.LAMBDA_ARROW, "Expected '=>'")
            if (builder.tokenType == BRACE) {
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

    closeOrError(builder, "syntax error: set symbol | set symbol = expression")

    m.done(ParserElements.SET_ELEMENT)
}

fun isFunctionOrIife(builder: PsiBuilder): Boolean {
    val iife = optional(builder, LexerTokens.PARENTHESIS, "Expected '('") { it.trim() == "(" }
    val hasFunction = parseFunction(builder)
    if (iife) {
        expect(builder, LexerTokens.PARENTHESIS, "Expected ')'") { it.trim() == ")" }
        parseFunctionArguments(builder)
    }
    return hasFunction
}

private fun parseVariable(builder: PsiBuilder) {
    if (nextTokenIs(builder, BRACE, "{")) {
        expect(builder, BRACE, "Expected '{'") { it.trim() == "{" }
        var error = false
        while (!error && !builder.eof() && !nextTokenIs(builder, BRACE, "}")) {
            when {
                nextTokenIs(builder, SYMBOL) -> {
                    error = !expect(builder, SYMBOL, "Expected identifier")
                    if (nextTokenIs(builder, COLON)) {
                        expect(builder, COLON, "Expected ':'")
                        error = !expect(builder, SYMBOL, "Expected identifier")
                    }
                    if (nextTokenIs(builder, EQUAL)) {
                        expect(builder, EQUAL, "Expected '='")
                        error = !expect(builder, SYMBOL, "Expected identifier")
                    }
                }

                nextTokenIs(builder, EXPAND) -> {
                    expect(builder, EXPAND, "Expected '...'")
                    error = !expect(builder, SYMBOL, "Expected identifier")
                }
            }
            if (optional(builder, COMMA, "Expected ','")) {
                if (!nextTokenIs(builder, SYMBOL) && !nextTokenIs(builder, EXPAND)) {
                    builder.error("no dangling ',' allowed")
                    error = true
                }
            } else {
                if (!nextTokenIs(builder, BRACE, "}")) {
                    builder.error("Expected ',' or '}'")
                    error = true
                }
            }
        }
        if (error) builder.error("Unable to parse set")
        expect(builder, BRACE, "Expected '}'") { it.trim() == "}" }
    } else {
        expect(builder, SYMBOL, "Expected identifier")
    }
}

fun parsSetClose(builder: PsiBuilder) {
    val m = builder.mark()
    expect(builder, LexerTokens.SET_CLOSE_KEY, "Expected '/set' keyword")
    closeOrError(builder, "syntax error: /set ")
    m.done(ParserElements.SET_CLOSE_ELEMENT)
}
