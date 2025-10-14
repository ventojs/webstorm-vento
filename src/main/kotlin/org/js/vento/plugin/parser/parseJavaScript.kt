/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.lang.PsiBuilder
import org.js.vento.plugin.lexer.LexerTokens

fun parseJavaScript(builder: PsiBuilder) {
    val marker = builder.mark()

    if (builder.tokenType == LexerTokens.JAVASCRIPT_START) {
        builder.advanceLexer()
    }

    if (builder.tokenType == ParserElements.JAVASCRIPT_ELEMENT) {
        builder.advanceLexer()
    }

    if (builder.tokenType == LexerTokens.JAVASCRIPT_END) {
        builder.advanceLexer()
    }

    marker.done(ParserElements.JAVASCRIPT_ELEMENT)
}
