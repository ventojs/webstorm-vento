/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.lang.PsiBuilder
import org.js.vento.plugin.lexer.LexerTokens

fun parsSetClose(builder: PsiBuilder) {
    val m = builder.mark()
    expect(builder, LexerTokens.SET_CLOSE_START, "Expected '{{' ")
    expect(builder, LexerTokens.SET_CLOSE_KEY, "Expected '/set' keyword")
    expect(builder, LexerTokens.SET_CLOSE_END, "Expected '}}' ")
    m.done(ParserElements.SET_CLOSE_ELEMENT)
}
