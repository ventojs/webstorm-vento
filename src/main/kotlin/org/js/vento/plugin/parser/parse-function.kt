/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.lang.PsiBuilder
import org.js.vento.plugin.lexer.LexerTokens

fun parseFunctionClose(builder: PsiBuilder) {
    val m = builder.mark()
    expect(builder, LexerTokens.FUNCTION_CLOSE_KEY, "Expected '/function' keyword'")
    closeOrError(builder, "syntax error: /function expression")
    m.done(ParserElements.FUNCTION_CLOSE_ELEMENT)
}

fun parseFunction(builder: PsiBuilder): Boolean {
    val m = builder.mark()

    parseFunctionSignature(builder)
    parseFunctionBody(builder)

    m.done(ParserElements.FUNCTION_ELEMENT)
    return true
}

fun parseFunctionBody(builder: PsiBuilder) {
    val mark = builder.mark()

    expect(builder, LexerTokens.BRACE, "Expected '{' ") { it.trim() == "{" }

    var braceCounter = 1
    while (
        braceCounter > 0 &&
        (
            !builder.eof() &&
                (
                    builder.tokenType == LexerTokens.BRACE ||
                        builder.tokenType == LexerTokens.STATEMENT ||
                        builder.tokenType == LexerTokens.SEMICOLON ||
                        builder.tokenType == LexerTokens.UNKNOWN
                )
        )
    ) {
        when (builder.tokenType) {
            LexerTokens.UNKNOWN -> parseUnknown(builder)
            LexerTokens.BRACE -> {
                if (builder.tokenText?.trim() == "{") braceCounter++
                if (builder.tokenText?.trim() == "}") braceCounter--
                if (braceCounter != 0) builder.advanceLexer()
            }

            LexerTokens.STATEMENT -> {
                builder.advanceLexer()
                optional(builder, LexerTokens.SEMICOLON, "Expected ';' ", true)
            }

            else -> builder.advanceLexer()
        }
    }

    expect(builder, LexerTokens.BRACE, "Expected '}' ") { it.trim() == "}" }

    mark.done(ParserElements.FUNCTION_BODY_ELEMENT)
}

fun parseFunctionSignature(builder: PsiBuilder, nameRequired: Boolean = false) {
    val mark = builder.mark()
    optional(builder, LexerTokens.ASYNC_KEY, "Expected 'async'")

    expect(builder, LexerTokens.FUNCTION_KEY, "Expected 'function' keyword")
    if (nameRequired) {
        expect(builder, LexerTokens.FUNCTION_NAME, "Expected function name")
    } else {
        optional(builder, LexerTokens.FUNCTION_NAME, "Expected function name")
    }

    // ARGS
    if (builder.tokenType == LexerTokens.PARENTHESIS) {
        parseFunctionArguments(builder)
    }

    mark.done(ParserElements.FUNCTION_SIGNATURE_ELEMENT)
}

fun parseFunctionArguments(builder: PsiBuilder) {
    val mark = builder.mark()
    expect(builder, LexerTokens.PARENTHESIS, "Expected '('") { it.trim() == "(" }
    if (builder.tokenType == LexerTokens.FUNCTION_ARG || (builder.tokenType == LexerTokens.BRACE && builder.tokenText?.trim() == "{")) {
        val isOpen = optional(builder, LexerTokens.BRACE, "Expected '{'") { it.trim() == "{" }
        parseFunctionArg(builder)
        while (optional(builder, LexerTokens.COMMA, "Expected ','")) {
            parseFunctionArg(builder)
        }
        if (isOpen) expect(builder, LexerTokens.BRACE, "Expected '}'") { it.trim() == "}" }
    }
    if (optional(builder, LexerTokens.EQUAL, "Expected '='")) {
        parseExpression(builder)
    }
    expect(builder, LexerTokens.PARENTHESIS, "Expected ')'") { it.trim() == ")" }
    mark.done(ParserElements.FUNCTION_ARGUMENTS_ELEMENT)
}

fun parseFunctionArg(builder: PsiBuilder) {
    val mark = builder.mark()

    expect(builder, LexerTokens.FUNCTION_ARG, "Expected function argument name")
    if (optional(builder, LexerTokens.EQUAL, "Expected '='")) {
        val isOpen = optional(builder, LexerTokens.BRACE, "Expected '{'") { it.trim() == "{" }
        when (builder.tokenType) {
            LexerTokens.STRING -> parseString(builder)
            LexerTokens.NUMBER -> expect(builder, LexerTokens.NUMBER, "Expected number", true)
            LexerTokens.BOOLEAN -> expect(builder, LexerTokens.BOOLEAN, "Expected boolean")
            LexerTokens.REGEX -> parseRegex(builder)
            LexerTokens.BRACKET -> parseArray(builder)
            LexerTokens.BRACE -> parseObject(builder)
            else -> builder.error("Expected string, number, boolean, regex, array, or object")
        }
        if (isOpen) expect(builder, LexerTokens.BRACE, "Expected '}'") { it.trim() == "}" }
    }

    mark.done(ParserElements.FUNCTION_ARG_ELEMENT)
}
