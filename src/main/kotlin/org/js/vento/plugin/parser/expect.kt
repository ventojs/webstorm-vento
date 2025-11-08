
/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import org.js.vento.plugin.lexer.LexerTokens

fun expect(
    builder: PsiBuilder,
    expected: IElementType,
    message: String,
    expectMultipleTokens: Boolean = false,
    test: (text: String) -> Boolean = { true },
): Boolean {
    return if (builder.tokenType == expected) {
        builder.tokenText?.let {
            if (!test(it)) builder.error("Unexpected token. found: '$it' ${builder.tokenType} ; expected: '$expected' ; $message")
        }

        builder.advanceLexer()
        return if (expectMultipleTokens && builder.tokenType == expected) {
            expect(builder, expected, message, true)
        } else {
            true
        }
    } else {
        // TODO: not sure why I am only handling unknown tokens here. I should handl anything that is not expected
        if (builder.tokenType == LexerTokens.UNKNOWN) {
            builder.advanceLexer()
        }
        builder.error(message)
        false
    }
}
