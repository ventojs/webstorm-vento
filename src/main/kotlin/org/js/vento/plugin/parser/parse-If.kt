/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.lang.PsiBuilder
import org.js.vento.plugin.lexer.LexerTokens

fun parseIf(builder: PsiBuilder) {
    val m = builder.mark()
    expect(builder, LexerTokens.IF_KEY, "Expected 'if' keyword' ")
    parseJavaScriptExpression(builder)
    closeOrError(builder, "syntax error: if expression")
    m.done(ParserElements.IF_ELEMENT)
}

fun parseElseIf(builder: PsiBuilder) {
    val m = builder.mark()
    expect(builder, LexerTokens.ELSEIF_KEY, "Expected 'elseif' keyword")
    parseJavaScriptExpression(builder)
    closeOrError(builder, "syntax error: elseif expression")
    m.done(ParserElements.ELSEIF_ELEMENT)
}

fun parseElse(builder: PsiBuilder) {
    val m = builder.mark()
    expect(builder, LexerTokens.ELSE_KEY, "Expected 'else' keyword")
    closeOrError(builder, "syntax error: else expression")
    m.done(ParserElements.ELSE_ELEMENT)
}

fun parseIfClose(builder: PsiBuilder) {
    val m = builder.mark()
    expect(builder, LexerTokens.IF_CLOSE_KEY, "Expected '/if' keyword")
    closeOrError(builder, "syntax error: /if expression")
    m.done(ParserElements.IF_CLOSE_ELEMENT)
}
