/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.lang.PsiBuilder
import org.js.vento.plugin.lexer.LexerTokens

fun parseExportFunction(builder: PsiBuilder) {
    val m = builder.mark()

    expect(builder, LexerTokens.EXPORT_FUNCTION_START, "Expected '{{' ")
    expect(builder, LexerTokens.EXPORT_KEY, "Expected 'export' keyword")
    expect(builder, LexerTokens.EXPORT_FUNCTION_KEY, "Expected 'function' keyword")
    expect(builder, LexerTokens.EXPORT_VAR, "Expected function name")
    expect(builder, LexerTokens.EXPORT_FUNCTION_ARGS, "Expected function arguments: (arg1[,arg2])", true)
    expect(builder, LexerTokens.EXPORT_FUNCTION_END, "Expected '}}' ")

    m.done(ParserElements.EXPORT_FUNCTION_ELEMENT)
}
