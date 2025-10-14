/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.lang.PsiBuilder
import org.js.vento.plugin.lexer.LexerTokens

fun parseExpression(builder: PsiBuilder, required: Boolean = true): Boolean {
    val m = builder.mark()

    var hasExpression = false
    while (
        !builder.eof() &&
        (
            builder.tokenType == LexerTokens.EXPRESSION ||
                builder.tokenType == LexerTokens.STRING ||
                builder.tokenType == LexerTokens.REGEX ||
                builder.tokenType == LexerTokens.BRACKET ||
                builder.tokenType == LexerTokens.DOT ||
                builder.tokenType == LexerTokens.IDENTIFIER ||
                builder.tokenType == LexerTokens.UNKNOWN
        )
    ) {
        if (builder.tokenType == LexerTokens.UNKNOWN) {
            if (required) builder.error("Unexpected expression content")
        } else if (builder.tokenType == LexerTokens.IDENTIFIER ||
            builder.tokenType == LexerTokens.EXPRESSION ||
            builder.tokenType == LexerTokens.STRING ||
            builder.tokenType == LexerTokens.REGEX
        ) {
            hasExpression = true
        }
        builder.advanceLexer()
    }
    if (!hasExpression && required) builder.error("Expected expression")
    if (hasExpression) m.done(ParserElements.EXPRESSION) else m.drop()

    return hasExpression
}
