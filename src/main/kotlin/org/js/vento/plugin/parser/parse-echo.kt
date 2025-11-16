/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.lang.PsiBuilder
import org.js.vento.plugin.lexer.LexerTokens

fun parseEchoClose(builder: PsiBuilder) {
    val m = builder.mark()
    expect(builder, LexerTokens.ECHO_CLOSE_KEY, "Expected 'echo' keyword' ")
    closeOrError(builder, "syntax error: /echo expression")
    m.done(ParserElements.ECHO_CLOSE_ELEMENT)
}

fun parseEcho(builder: PsiBuilder) {
    val m = builder.mark()
    expect(builder, LexerTokens.ECHO_KEY, "Expected 'echo' keyword' ")
    if (builder.tokenType == LexerTokens.STRING) {
        parseString(builder)
    }
    if (builder.tokenType == LexerTokens.PIPE) {
        parsePipe(builder)
    }
    closeOrError(builder, "syntax error: echo expression")
    m.done(ParserElements.ECHO_ELEMENT)
}
