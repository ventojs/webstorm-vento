
/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import org.js.vento.plugin.lexer.LexerTokens

/**
 * Validates if the next token in the PsiBuilder matches the expected token type.
 * Advances the lexer if the token matches and validates multiple consecutive tokens if specified.
 *
 * @param builder The PSI builder containing the token stream
 * @param expected The expected token type to match
 * @param message Error message to display if validation fails
 * @param expectMultipleTokens If true, validates multiple consecutive tokens of the same type
 * @param test Optional predicate to perform additional validation on the token text
 * @return true if validation succeeds, false otherwise
 */
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
