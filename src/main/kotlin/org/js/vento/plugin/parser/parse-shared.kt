/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import org.js.vento.plugin.lexer.LexerToken
import org.js.vento.plugin.lexer.LexerTokens

fun parseUnknown(builder: PsiBuilder) {
    val m = builder.mark()
    expect(builder, LexerTokens.UNKNOWN, "Unexpected token", true)
    builder.error("Unexpected token")
    m.done(ParserElements.UNKNOWN_ELEMENT)
}

fun parsePipe(builder: PsiBuilder) {
    if (optional(builder, LexerTokens.PIPE, "Expected pipe (|>)")) {
        parseJavaScriptExpression(builder)
    }
}

fun closeOrError(builder: PsiBuilder, errorMsg: String) {
    while (builder.tokenType != LexerTokens.VBLOCK_CLOSE && !builder.eof()) {
        if (builder.tokenType == LexerTokens.UNKNOWN) {
            parseUnknown(builder)
        } else {
            builder.error(errorMsg)
            builder.advanceLexer()
        }
    }
}

fun nextTokenIs(
    builder: PsiBuilder,
    token: LexerToken,
    equals: String = "",
): Boolean = builder.tokenType == token && (equals == "" || builder.tokenText?.trim() == equals)

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

fun optional(
    builder: PsiBuilder,
    expected: IElementType,
    message: String,
    expectMultipleTokens: Boolean = false,
    test: (text: String) -> Boolean = { true },
): Boolean {
    return if (builder.tokenType == expected && test(builder.tokenText?.trim() ?: "")) {
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
