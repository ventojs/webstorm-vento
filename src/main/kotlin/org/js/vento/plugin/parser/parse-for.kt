/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.lang.PsiBuilder
import org.js.vento.plugin.lexer.LexerTokens

fun parseFor(builder: PsiBuilder) {
    val m = builder.mark()

    expect(builder, LexerTokens.FOR_KEY, "Expected 'for' keyword")
    // PARSE VALUE
    var value = parseForValue(builder)
//        var value = parseJavaScriptExpresion(builder)

    if (!value) builder.error("Expected value")

    expect(builder, LexerTokens.FOR_OF, "Expected 'of' keyword")

    // PARSE COLLECTION
//        var collection = parseForCollection(builder)
    var collection = parseJavaScriptExpression(builder)
    if (!collection) builder.error("Expected collection")
    parsePipe(builder)
    closeOrError(builder, "syntax error: for [value] in [collection]")

    m.done(ParserElements.FOR_ELEMENT)
}

fun parseForValue(builder: PsiBuilder): Boolean {
    var value = false
    while (
        !builder.eof() &&
        (
            builder.tokenType == LexerTokens.BRACE ||
                builder.tokenType == LexerTokens.SYMBOL ||
                builder.tokenType == LexerTokens.COMMA ||
                builder.tokenType == LexerTokens.BRACKET ||
                builder.tokenType == LexerTokens.UNKNOWN
        )
    ) {
        value = true
        builder.advanceLexer()
        if (builder.tokenType == LexerTokens.UNKNOWN) {
            value = false
            builder.error("Unexpected for content")
        }
        if (builder.tokenType == LexerTokens.BRACKET && builder.tokenText?.trim() == "[") {
            parseArray(builder)
        }
    }
    return value
}

fun parseForCollection(builder: PsiBuilder): Boolean {
    var collection = false
    while (
        !builder.eof() &&
        (
            (builder.tokenType == LexerTokens.BRACKET && builder.tokenText?.trim() == "[") ||
                (builder.tokenType == LexerTokens.BRACE && builder.tokenText?.trim() == "{") ||
                builder.tokenType == LexerTokens.STRING ||
                builder.tokenType == LexerTokens.NUMBER ||
                builder.tokenType == LexerTokens.SYMBOL ||
                builder.tokenType == LexerTokens.COMMA ||
                builder.tokenType == LexerTokens.PARENTHESIS ||
                builder.tokenType == LexerTokens.DOT ||
                builder.tokenType == LexerTokens.UNKNOWN
        )
    ) {
        collection = true
        if (builder.tokenType == LexerTokens.BRACKET) {
            parseArray(builder)
        }

        if (builder.tokenType == LexerTokens.BRACE) {
            parseObject(builder)
        }

        if (builder.tokenType == LexerTokens.STRING) {
            parseString(builder)
        }

        if (builder.tokenType == LexerTokens.SYMBOL ||
            builder.tokenType == LexerTokens.PARENTHESIS ||
            builder.tokenType == LexerTokens.DOT ||
            builder.tokenType == LexerTokens.COMMA ||
            builder.tokenType == LexerTokens.NUMBER
        ) {
            builder.advanceLexer()
        }
        if (builder.tokenType == LexerTokens.UNKNOWN) {
            collection = false
            parseUnknown(builder)
        }
    }
    return collection
}

fun parseForClose(builder: PsiBuilder) {
    val m = builder.mark()

    expect(builder, LexerTokens.FOR_CLOSE_KEY, "Expected '/for' keyword")
    closeOrError(builder, "syntax error: for [value] in [collection]")

    m.done(ParserElements.FOR_CLOSE_ELEMENT)
}
