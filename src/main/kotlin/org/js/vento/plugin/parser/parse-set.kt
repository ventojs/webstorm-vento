/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.lang.PsiBuilder
import org.js.vento.plugin.lexer.LexerTokens
import org.js.vento.plugin.lexer.LexerTokens.ASYNC_KEY
import org.js.vento.plugin.lexer.LexerTokens.BRACE
import org.js.vento.plugin.lexer.LexerTokens.COLON
import org.js.vento.plugin.lexer.LexerTokens.COMMA
import org.js.vento.plugin.lexer.LexerTokens.DESTRUCTURE_BRACE
import org.js.vento.plugin.lexer.LexerTokens.DESTRUCTURE_BRACKET
import org.js.vento.plugin.lexer.LexerTokens.DESTRUCTURE_KEY
import org.js.vento.plugin.lexer.LexerTokens.EQUAL
import org.js.vento.plugin.lexer.LexerTokens.EXPAND
import org.js.vento.plugin.lexer.LexerTokens.FUNCTION_KEY
import org.js.vento.plugin.lexer.LexerTokens.LAMBDA_ARROW
import org.js.vento.plugin.lexer.LexerTokens.PARENTHESIS
import org.js.vento.plugin.lexer.LexerTokens.SYMBOL

fun parsSet(builder: PsiBuilder) {
    val m = builder.mark()

    expect(builder, LexerTokens.SET_KEY, "Expected 'set' keyword")
    parseVariable(builder)

    val hasEq = optional(builder, EQUAL, "Expected '=' keyword")
    val hasExp =
        when {
            builder.tokenType == ASYNC_KEY ||
                builder.tokenType == FUNCTION_KEY ||
                builder.tokenType == PARENTHESIS &&
                builder.rawLookup(1) == FUNCTION_KEY -> {
                isFunctionOrIife(builder)
            }

            builder.tokenType == PARENTHESIS -> {
                parseFunctionArguments(builder)
                expect(builder, LAMBDA_ARROW, "Expected '=>'")
                if (builder.tokenType == BRACE) {
                    parseFunctionBody(builder)
                } else {
                    parseJavaScriptExpression(builder)
                }
                true
            }

            else -> {
                parseJavaScriptExpression(builder)
            }
        }

    if (hasEq && !hasExp) builder.error("Expected expression after '='")
    if (!hasEq && hasExp) builder.error("Expected '='")

    parsePipe(builder)

    closeOrError(builder, "syntax error: set symbol | set symbol = expression")

    m.done(ParserElements.SET_ELEMENT)
}

fun isFunctionOrIife(builder: PsiBuilder): Boolean {
    val iife = optional(builder, PARENTHESIS, "Expected '('") { it.trim() == "(" }
    val hasFunction = parseFunction(builder)
    if (iife) {
        expect(builder, PARENTHESIS, "Expected ')'") { it.trim() == ")" }
        parseFunctionArguments(builder)
    }
    return hasFunction
}

private fun parseVariable(builder: PsiBuilder) {
    if (nextTokenIs(builder, DESTRUCTURE_BRACE, "{")) {
        parseDestructuredObject(builder)
    } else if (nextTokenIs(builder, DESTRUCTURE_BRACKET, "[")) {
        parseDestructuredArray(builder)
    } else {
        expect(builder, SYMBOL, "Expected identifier")
    }
}

private fun parseDestructuredObject(builder: PsiBuilder) {
    expect(builder, DESTRUCTURE_BRACE, "Expected '{'") { it.trim() == "{" }
    var error = false
    while (!error && !builder.eof() && !nextTokenIs(builder, DESTRUCTURE_BRACE, "}")) {
        error =
            when {
                nextTokenIs(builder, SYMBOL) || nextTokenIs(builder, DESTRUCTURE_KEY) -> {
                    parseDestructuredElement(builder)
                }

                nextTokenIs(builder, EXPAND) -> {
                    parseExpandedElement(builder)
                }

                else -> {
                    false
                }
            }

        if (!error) error = parseComaOrNot(builder)
    }
    if (error) builder.error("Unable to parse set")
    expect(builder, DESTRUCTURE_BRACE, "Expected '}'") { it.trim() == "}" }
}

private fun parseDestructuredArray(builder: PsiBuilder) {
    expect(builder, DESTRUCTURE_BRACKET, "Expected '['") { it.trim() == "[" }
    var error = false
    while (!error && !builder.eof() && !nextTokenIs(builder, DESTRUCTURE_BRACKET, "]")) {
        when {
            nextTokenIs(builder, SYMBOL) -> {
                error = parseDestructuredElement(builder)
            }

            nextTokenIs(builder, EXPAND) -> {
                error = parseExpandedElement(builder)
            }
        }
        if (optional(builder, COMMA, "Expected ','")) {
            if (!nextTokenIs(builder, SYMBOL) && !nextTokenIs(builder, EXPAND) && !nextTokenIs(builder, COMMA)) {
                builder.error("no dangling ',' allowed")
                error = true
            }
        } else {
            if (!nextTokenIs(builder, DESTRUCTURE_BRACKET, "]")) {
                builder.error("Expected ',' or ']'")
                error = true
            }
        }
    }
    if (error) builder.error("Unable to parse set")
    expect(builder, DESTRUCTURE_BRACKET, "Expected ']'") { it.trim() == "]" }
}

private fun parseExpandedElement(builder: PsiBuilder): Boolean {
    expect(builder, EXPAND, "Expected '...'")
    return when {
        nextTokenIs(builder, SYMBOL) -> {
            expect(builder, SYMBOL, "Expected identifier")
            false
        }

        nextTokenIs(builder, DESTRUCTURE_BRACE, "{") -> {
            parseDestructuredObject(builder)
            false
        }

        nextTokenIs(builder, DESTRUCTURE_BRACKET, "[") -> {
            parseDestructuredArray(builder)
            false
        }

        else -> {
            true
        }
    }
}

private fun parseDestructuredElement(builder: PsiBuilder): Boolean {
    var error: Boolean = false
    if (!optional(builder, SYMBOL, "Expected identifier")) {
        expect(builder, DESTRUCTURE_KEY, "Expected identifier")
    }
    if (nextTokenIs(builder, COLON)) {
        expect(builder, COLON, "Expected ':'")
        error = !expect(builder, SYMBOL, "Expected identifier")
    }
    if (nextTokenIs(builder, EQUAL)) {
        expect(builder, EQUAL, "1.Expected '='")
        parseStrictJavaScriptExpression(builder)
//        error = !expect(builder, SYMBOL, "Expected identifier")
    }
    return error
}

private fun parseComaOrNot(builder: PsiBuilder): Boolean {
    var error = false
    if (optional(builder, COMMA, "Expected ','")) {
        if (!nextTokenIs(builder, SYMBOL) && !nextTokenIs(builder, EXPAND)) {
            builder.error("no dangling ',' allowed")
            error = true
        }
    } else {
        if (!nextTokenIs(builder, DESTRUCTURE_BRACE, "}")) {
            builder.error("Expected ',' or '}'")
            error = true
        }
    }
    return error
}

fun parsSetClose(builder: PsiBuilder) {
    val m = builder.mark()
    expect(builder, LexerTokens.SET_CLOSE_KEY, "Expected '/set' keyword")
    closeOrError(builder, "syntax error: /set ")
    m.done(ParserElements.SET_CLOSE_ELEMENT)
}
