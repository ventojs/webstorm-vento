/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.lang.PsiBuilder
import org.js.vento.plugin.lexer.LexerTokens

fun parseInclude(builder: PsiBuilder) {
    val m = builder.mark()
    expect(builder, LexerTokens.INCLUDE_KEY, "Expected 'include' keyword' ")
    when (builder.tokenType) {
        LexerTokens.STRING -> parseString(builder)
        LexerTokens.FILE -> expect(builder, LexerTokens.FILE, "Expected filepath")
        else -> parseStrictJavaScriptExpression(builder)
    }
    if (nextTokenIs(builder, LexerTokens.BRACE, "{")) parseJsDataObject(builder)
    parsePipe(builder)
    closeOrError(builder, "syntax error: include 'path/to/file.js' | data | pipe")
    m.done(ParserElements.INCLUDE_ELEMENT)
}
