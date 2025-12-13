/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.lang.PsiBuilder
import org.js.vento.plugin.lexer.LexerTokens

fun parseFragment(builder: PsiBuilder) {
    val m = builder.mark()
    expect(builder, LexerTokens.FRAGMENT_KEY, "Expected fragment keyword")
    expect(builder, LexerTokens.SYMBOL, "Expected symbol")
    closeOrError(builder, "syntax error: fragment 'path/to/file.js' | data | pipe")
    m.done(ParserElements.FRAGMENT_ELEMENT)
}

fun parseFragmentClose(builder: PsiBuilder) {
    val m = builder.mark()
    expect(builder, LexerTokens.FRAGMENT_CLOSE_KEY, "Expected /fragment keyword")
    m.done(ParserElements.FRAGMENT_CLOSE_ELEMENT)
}
