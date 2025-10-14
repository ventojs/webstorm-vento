/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.lang.PsiBuilder
import org.js.vento.plugin.lexer.LexerTokens

fun parseImport(builder: PsiBuilder) {
    val m = builder.mark()

    expect(builder, LexerTokens.IMPORT_START, "Expected '{{' ")
    expect(builder, LexerTokens.IMPORT_KEY, "Expected 'import' keyword")
    expect(builder, LexerTokens.IMPORT_VALUES, "Expected import values", true)
    expect(builder, LexerTokens.IMPORT_FROM, "Expected 'from' keyword")
    expect(builder, LexerTokens.IMPORT_FILE, "Expected vento(.vto) path string")
    expect(builder, LexerTokens.IMPORT_END, "Expected '}}' ")

    m.done(ParserElements.IMPORT_ELEMENT)
}
