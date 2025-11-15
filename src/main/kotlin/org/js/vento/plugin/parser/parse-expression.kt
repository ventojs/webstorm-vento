/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.lang.PsiBuilder
import org.js.vento.plugin.lexer.LexerTokens

fun parseStrictJavaScriptExpression(builder: PsiBuilder, required: Boolean = true): Boolean {
    val m = builder.mark()

    // starts with (,[,{, symbol, string, number, boolean, regex

    var hasExpression = false

    when {
        optional(builder, LexerTokens.PARENTHESIS, "Expected '('") { it.trim() == "(" } -> {
            parseStrictJavaScriptExpression(builder)
            expect(builder, LexerTokens.PARENTHESIS, "Expected ')'") { it.trim() == ")" }
            hasExpression = true
        }

        nextTokenIs(builder, LexerTokens.BRACKET, "[") -> {
            parseArray(builder)
            hasExpression = true
        }

        nextTokenIs(builder, LexerTokens.BRACE, "{") -> {
            parseObject(builder)
            hasExpression = true
        }

        nextTokenIs(builder, LexerTokens.STRING) -> {
            parseString(builder)
            if (optional(builder, LexerTokens.DOT, "Expected '.'")) {
                parseInvocation(builder)
            }
            hasExpression = true
        }

        nextTokenIs(builder, LexerTokens.NUMBER) -> {
            expect(builder, LexerTokens.NUMBER, "Expected number")
            if (optional(builder, LexerTokens.DOT, "Expected '.'")) {
                parseInvocation(builder)
            }
            hasExpression = true
        }

        nextTokenIs(builder, LexerTokens.BOOLEAN) -> {
            expect(builder, LexerTokens.BOOLEAN, "Expected boolean")
            if (optional(builder, LexerTokens.DOT, "Expected '.'")) {
                parseInvocation(builder)
            }
            hasExpression = true
        }

        nextTokenIs(builder, LexerTokens.REGEX) -> {
            expect(builder, LexerTokens.REGEX, "Expected regex", true)
            if (optional(builder, LexerTokens.DOT, "Expected '.'")) {
                parseInvocation(builder)
            }
            hasExpression = true
        }

        nextTokenIs(builder, LexerTokens.SYMBOL) -> {
            expect(builder, LexerTokens.SYMBOL, "Expected symbol")
            parseInvocation(builder)
            if (optional(builder, LexerTokens.DOT, "Expected '.'")) {
                builder.advanceLexer()
                expect(builder, LexerTokens.SYMBOL, "Expected symbol")
                parseInvocation(builder)
            }
            hasExpression = true
        }

        else -> hasExpression = false
    }
//        closeOrError(builder, "syntax error: expression")
    if (!hasExpression && required) builder.error("Expected expression")
    if (hasExpression) m.done(ParserElements.JAVASCRIPT_EXPRESSION_ELEMENT) else m.drop()
    return hasExpression
}

fun parseInvocation(builder: PsiBuilder) {
    if (optional(builder, LexerTokens.PARENTHESIS, "Expected '('") { it.trim() == "(" }) {
        parseArgument(builder)
        while (!builder.eof() && builder.tokenType == LexerTokens.COMMA) {
            builder.advanceLexer()
            parseArgument(builder)
        }
        expect(builder, LexerTokens.PARENTHESIS, "Expected ')'") { it.trim() == ")" }
    }
}

fun parseArgument(builder: PsiBuilder) {
    when {
        nextTokenIs(builder, LexerTokens.PARENTHESIS, "(") -> {
            parseInvocation(builder)
            if (optional(builder, LexerTokens.LAMBDA_ARROW, "Expected '=>'") { it.trim() == "=>" }) {
                var parenthesis = 0
                while (
                    builder.tokenType != LexerTokens.PARENTHESIS ||
                    (builder.tokenType == LexerTokens.PARENTHESIS && builder.tokenText?.trim() == ")" && parenthesis > 0)
                ) {
                    if (builder.tokenType == LexerTokens.PARENTHESIS && builder.tokenText?.trim() == "(") {
                        parenthesis++
                    } else if (builder.tokenType == LexerTokens.PARENTHESIS && builder.tokenText?.trim() == ")") {
                        parenthesis--
                    }
                    builder.advanceLexer()
                }
            }
        }

        nextTokenIs(builder, LexerTokens.SYMBOL) -> {
            optional(builder, LexerTokens.NEW, "Expected 'new'")
            parseStrictJavaScriptExpression(builder)
        }

        nextTokenIs(builder, LexerTokens.BRACE) -> parseObject(builder)
        nextTokenIs(builder, LexerTokens.BRACKET) -> parseArray(builder)
        nextTokenIs(builder, LexerTokens.STRING) -> parseString(builder)
        else -> parseStrictJavaScriptExpression(builder)
    }
}

fun parseExpression(builder: PsiBuilder, required: Boolean = true): Boolean {
    val m = builder.mark()

    var hasExpression = false
    var isParenOpen = 0
    var exit = false
    while (
        !builder.eof() &&
        !exit &&
        (
            (builder.tokenType == LexerTokens.BRACE && builder.tokenText?.trim() == "{") ||
                (builder.tokenType == LexerTokens.BRACE && builder.tokenText?.trim() == "{") ||
                (builder.tokenType == LexerTokens.BRACKET && builder.tokenText?.trim() == "[") ||
                (builder.tokenType == LexerTokens.PARENTHESIS) ||
                builder.tokenType == LexerTokens.ASTERISK ||
                builder.tokenType == LexerTokens.AWAIT_KEY ||
                builder.tokenType == LexerTokens.BOOLEAN ||
                builder.tokenType == LexerTokens.COLON ||
                builder.tokenType == LexerTokens.COMMA ||
                builder.tokenType == LexerTokens.DOT ||
                builder.tokenType == LexerTokens.INSTANCEOF ||
                builder.tokenType == LexerTokens.MINUS ||
                builder.tokenType == LexerTokens.NEW ||
                builder.tokenType == LexerTokens.NUMBER ||
                builder.tokenType == LexerTokens.PARENTHESIS ||
                builder.tokenType == LexerTokens.PLUS ||
                builder.tokenType == LexerTokens.REGEX ||
                builder.tokenType == LexerTokens.STRING ||
                builder.tokenType == LexerTokens.SYMBOL ||
                builder.tokenType == LexerTokens.UNKNOWN
        )
    ) {
        if (builder.tokenType == LexerTokens.BRACE) {
            parseObject(builder)
            hasExpression = true
        } else if (builder.tokenType == LexerTokens.STRING) {
            parseString(builder)
            hasExpression = true
        } else if (builder.tokenType == LexerTokens.BRACKET) {
            parseArray(builder)
            hasExpression = true
        } else if (
            builder.tokenType == LexerTokens.REGEX
        ) {
            parseRegex(builder)
            hasExpression = true
        } else if (builder.tokenType == LexerTokens.PARENTHESIS) {
            if (builder.tokenText?.trim() == "(") {
                expect(builder, LexerTokens.PARENTHESIS, "Expected '('") { it.trim() == "(" }
                isParenOpen++
            }

            if (isParenOpen == 0 && builder.tokenText?.trim() == ")") {
                exit = true
                isParenOpen = 0
            } else if (builder.tokenText?.trim() == ")") {
                expect(builder, LexerTokens.PARENTHESIS, "Expected ')'") { it.trim() == ")" }
                isParenOpen--
            }
        } else if (
            builder.tokenType == LexerTokens.ASTERISK ||
            builder.tokenType == LexerTokens.AWAIT_KEY ||
            builder.tokenType == LexerTokens.BOOLEAN ||
            builder.tokenType == LexerTokens.COLON ||
            builder.tokenType == LexerTokens.COMMA ||
            builder.tokenType == LexerTokens.DOT ||
            builder.tokenType == LexerTokens.INSTANCEOF ||
            builder.tokenType == LexerTokens.MINUS ||
            builder.tokenType == LexerTokens.NEW ||
            builder.tokenType == LexerTokens.NUMBER ||
            builder.tokenType == LexerTokens.PLUS ||
            builder.tokenType == LexerTokens.SYMBOL
        ) {
            builder.advanceLexer()
            hasExpression = true
        } else if (builder.tokenType == LexerTokens.UNKNOWN) {
            parseUnknown(builder)
            hasExpression = false
        } else {
            val m = builder.mark()
            hasExpression = false
            builder.error("Unexpected token")
            builder.advanceLexer()
            m.done(ParserElements.UNKNOWN_ELEMENT)
        }
    }
    if (!hasExpression && required) builder.error("Expected expression")
    if (hasExpression) m.done(ParserElements.EXPRESSION_ELEMENT) else m.drop()

    return hasExpression
}

fun parseJavaScript(builder: PsiBuilder) {
    val marker = builder.mark()

    expect(builder, LexerTokens.JSBLOCK_OPEN, "Expected javascript element")
    expect(builder, LexerTokens.JAVASCRIPT, "Expected javascript element", true)
    expect(builder, LexerTokens.JSBLOCK_CLOSE, "Expected javascript element")

    marker.done(ParserElements.JAVASCRIPT_ELEMENT)
}

fun parseJavaScriptExpression(builder: PsiBuilder): Boolean {
    val marker = builder.mark()

    var hasExpression = false
    while (!builder.eof() &&
        builder.tokenType != LexerTokens.PIPE &&
        builder.tokenType != LexerTokens.FOR_OF &&
        builder.tokenType != LexerTokens.VBLOCK_CLOSE
    ) {
        builder.advanceLexer()
        hasExpression = true
    }

    if (hasExpression) marker.done(ParserElements.JAVASCRIPT_EXPRESSION_ELEMENT) else marker.drop()
    return hasExpression
}

fun parseRegex(builder: PsiBuilder) {
    val m = builder.mark()
    expect(builder, LexerTokens.REGEX, "Expected REGEX", true)
    m.done(ParserElements.REGEX_ELEMENT)
}

fun parseString(builder: PsiBuilder) {
    val m = builder.mark()
    expect(builder, LexerTokens.STRING, "Expected string", true)
    m.done(ParserElements.STRING_ELEMENT)
}
