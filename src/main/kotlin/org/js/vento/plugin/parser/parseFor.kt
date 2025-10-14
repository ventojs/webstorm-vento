/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.lang.PsiBuilder
import org.js.vento.plugin.lexer.LexerTokens

fun parseFor(builder: PsiBuilder) {
    val m = builder.mark()
    builder.advanceLexer() // consume {{

    // Consume content tokens until we see the end or EOF
    while (
        !builder.eof() &&
        (
            builder.tokenType == LexerTokens.FOR_CLOSE_KEY ||
                builder.tokenType == LexerTokens.FOR_KEY ||
                builder.tokenType == LexerTokens.FOR_VALUE ||
                builder.tokenType == LexerTokens.FOR_OF ||
                builder.tokenType == LexerTokens.FOR_COLLECTION ||
                builder.tokenType == LexerTokens.UNKNOWN
        )
    ) {
        builder.advanceLexer()
    }

    if (builder.tokenType == LexerTokens.FOR_END) {
        builder.advanceLexer()
    }

    m.done(ParserElements.FOR_ELEMENT)
}
