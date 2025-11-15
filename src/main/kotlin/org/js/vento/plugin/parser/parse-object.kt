/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.lang.PsiBuilder
import org.js.vento.plugin.lexer.LexerTokens

fun parseJsDataObject(builder: PsiBuilder) {
    val marker = builder.mark()

    while (!builder.eof() &&
        builder.tokenType != LexerTokens.PIPE &&
        (builder.tokenType != LexerTokens.VBLOCK_CLOSE && builder.tokenText?.trim() != "}}")
    ) {
        builder.advanceLexer()
    }

    marker.done(ParserElements.JAVASCRIPT_DATA_OBJECT_ELEMENT)
}

fun parseObject(builder: PsiBuilder, optional: Boolean = false) {
    if (!optional || builder.tokenType == LexerTokens.BRACE) {
        val m = builder.mark()

        expect(builder, LexerTokens.BRACE, "Expected brace", false) { it.trim() == "{" }
        if (builder.tokenType != LexerTokens.BRACE && builder.tokenText?.trim() != "}") {
            parseObjectElement(builder)
            while (!builder.eof() && builder.tokenType == LexerTokens.COMMA) {
                builder.advanceLexer() // comma
                parseObjectElement(builder)
            }
        }
        expect(builder, LexerTokens.BRACE, "Expected brace", false) { it.trim() == "}" }

        m.done(ParserElements.OBJECT_ELEMENT)
    }
}

fun parseObjectElement(builder: PsiBuilder) {
    parseObjectElementKey(builder)
    if (optional(builder, LexerTokens.COLON, "Expected ':' ")) {
        parseExpression(builder)
    }
}

fun parseObjectElementKey(builder: PsiBuilder) {
    val sym = optional(builder, LexerTokens.SYMBOL, "Expected symbol")
    val num = optional(builder, LexerTokens.NUMBER, "Expected symbol")

    val stg =
        if (builder.tokenType == LexerTokens.STRING) {
            parseString(builder)
            true
        } else {
            false
        }

    if (!sym && !num && !stg) {
        builder.error("Expected symbol, number, or string")
    }
}
