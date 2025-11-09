/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import org.js.vento.plugin.lexer.LexerTokens

fun optional(
    builder: PsiBuilder,
    expected: IElementType,
    message: String,
    expectMultipleTokens: Boolean = false,
    test: (text: String) -> Boolean = { true },
): Boolean {
    return if (builder.tokenType == expected) {
        builder.advanceLexer()
        return if (expectMultipleTokens && builder.tokenType == expected) {
            expect(builder, expected, message, true, test)
        } else {
            true
        }
    } else {
        if (builder.tokenType == LexerTokens.UNKNOWN) {
            builder.advanceLexer()
            builder.error(message)
            return false
        }
        false
    }
}
