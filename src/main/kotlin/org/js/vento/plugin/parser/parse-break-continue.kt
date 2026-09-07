/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.lang.PsiBuilder
import org.js.vento.plugin.lexer.LexerTokens

fun parseBreak(builder: PsiBuilder) {
    val m = builder.mark()
    expect(builder, LexerTokens.BREAK_KEY, "Expected 'break' keyword")
    closeOrError(builder, "syntax error: break")
    m.done(ParserElements.BREAK_ELEMENT)
}

fun parseContinue(builder: PsiBuilder) {
    val m = builder.mark()
    expect(builder, LexerTokens.CONTINUE_KEY, "Expected 'continue' keyword")
    closeOrError(builder, "syntax error: continue")
    m.done(ParserElements.CONTINUE_ELEMENT)
}
