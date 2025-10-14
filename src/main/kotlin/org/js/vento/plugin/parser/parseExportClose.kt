/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.lang.PsiBuilder
import org.js.vento.plugin.lexer.LexerTokens

fun parseExportClose(builder: PsiBuilder) {
    val m = builder.mark()

    expect(builder, LexerTokens.EXPORT_CLOSE_START, "Expected '{{/' ")
    expect(builder, LexerTokens.EXPORT_CLOSE_KEY, "Expected '/export' keyword")
    expect(builder, LexerTokens.EXPORT_CLOSE_END, "Expected '}}' ")

    m.done(ParserElements.EXPORT_CLOSE_ELEMENT)
}
