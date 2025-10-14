/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.lang.PsiBuilder
import org.js.vento.plugin.lexer.LexerTokens

fun parseCommentBlock(builder: PsiBuilder) {
    val marker = builder.mark()

    // Consume opening token
    builder.advanceLexer()

    // Consume content tokens
    while (!builder.eof() &&
        builder.tokenType == LexerTokens.COMMENT_CONTENT
    ) {
        builder.advanceLexer()
    }

    // Consume closing token if present
    if (builder.tokenType == LexerTokens.COMMENT_END ||
        builder.tokenType == LexerTokens.TRIM_COMMENT_END
    ) {
        builder.advanceLexer()
    }

    marker.done(ParserElements.COMMENT_BLOCK)
}
