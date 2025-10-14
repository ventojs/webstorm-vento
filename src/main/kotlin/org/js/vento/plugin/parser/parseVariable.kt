/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.lang.PsiBuilder
import org.js.vento.plugin.lexer.LexerTokens

fun parseVariable(builder: PsiBuilder) {
    val m = builder.mark()
    builder.advanceLexer() // consume {{ or {{-

    // Consume content tokens until we see the end or EOF
    while (
        !builder.eof() &&
        (
            builder.tokenType == LexerTokens.VARIABLE_ELEMENT ||
                builder.tokenType == LexerTokens.PIPE ||
                builder.tokenType == LexerTokens.STRING ||
                builder.tokenType == LexerTokens.UNKNOWN
        )
    ) {
        if (builder.tokenType == LexerTokens.UNKNOWN) {
            builder.error("Unexpected variable content")
        }
        builder.advanceLexer()
    }

    // Expect end
    if (builder.tokenType == LexerTokens.VARIABLE_END) {
        builder.advanceLexer()
    } else {
        builder.error("Unexpected variable content")
    }

    m.done(ParserElements.JAVACRIPT_VARIABLE_ELEMENT)
}
