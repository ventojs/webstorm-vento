/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.lang.PsiBuilder
import org.js.vento.plugin.lexer.LexerTokens

fun parseLayout(builder: PsiBuilder) {
    val m = builder.mark()
    expect(builder, LexerTokens.LAYOUT_KEY, "Expected layout keyword")
    expect(builder, LexerTokens.FILE, "Expected filepath")
    if (nextTokenIs(builder, LexerTokens.BRACE, "{")) parseJsDataObject(builder)
    if (nextTokenIs(builder, LexerTokens.SYMBOL)) expect(builder, LexerTokens.SYMBOL, "Expected data reference")
    parsePipe(builder)
    closeOrError(builder, "syntax error: layout 'path/to/file.js' | data | pipe")
    m.done(ParserElements.LAYOUT_ELEMENT)
}

fun parseLayoutClose(builder: PsiBuilder) {
    val m = builder.mark()
    expect(builder, LexerTokens.LAYOUT_CLOSE_KEY, "Expected /layout keyword")
    m.done(ParserElements.LAYOUT_CLOSE_ELEMENT)
}

fun parseSlot(builder: PsiBuilder) {
    val m = builder.mark()
    expect(builder, LexerTokens.LAYOUT_SLOT_KEY, "Expected slot keyword")
    expect(builder, LexerTokens.SYMBOL, "Expected identifier")
    parsePipe(builder)
    closeOrError(builder, "syntax error: slot symbol")
    m.done(ParserElements.LAYOUT_SLOT_ELEMENT)
}

fun parseSlotClose(builder: PsiBuilder) {
    val m = builder.mark()
    expect(builder, LexerTokens.LAYOUT_SLOT_CLOSE_KEY, "Expected slot keyword")
    m.done(ParserElements.LAYOUT_SLOT_CLOSE_ELEMENT)
}
