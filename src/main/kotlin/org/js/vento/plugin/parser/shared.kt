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
    expect(builder, LexerTokens.UNKNOWN, "Unexpected token(3)", true)
    builder.error("Unexpected token(1)")
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
): Boolean =
    if (builder.tokenType == expected) {
        builder.tokenText?.let {
            if (!test(it)) builder.error("Unexpected token. found: '$it' ${builder.tokenType} ; expected: '$expected' ; $message")
        }

        builder.advanceLexer()
        if (expectMultipleTokens && builder.tokenType == expected) {
            expect(builder, expected, message, true)
        } else {
            true
        }
    } else {
        // Only UNKNOWN tokens are consumed here - the lexer only ever emits UNKNOWN as a
        // last resort, so nothing else will ever recognize and skip it. Any other token
        // type is deliberately left in place: many callers probe optimistically (e.g. an
        // `optional(EQUAL, ...)` check right after a failed `expect`) and expect the token
        // to still be there, and constructs that don't have such a follow-up check still
        // rely on `closeOrError`/the enclosing `expect(VBLOCK_CLOSE, ...)` to resync to the
        // next `}}` - advancing past a structurally significant token here (e.g. `}}` itself,
        // or `(` right before `parseFunctionArguments` looks for it) corrupts the tree instead
        // of recovering it. See parseFragmentClose/parseLayoutClose/parseSlotClose/
        // parseExportClose for the actual no-resync gap this construct can hide: those close-tag
        // parsers now call closeOrError themselves after the keyword `expect()`, rather than
        // relying on `expect()` to somehow do it.
        if (builder.tokenType == LexerTokens.UNKNOWN) {
            builder.advanceLexer()
        }
        builder.error(message)
        return false
    }

fun optional(
    builder: PsiBuilder,
    expected: IElementType,
    message: String,
    expectMultipleTokens: Boolean = false,
    test: (text: String) -> Boolean = { true },
): Boolean =
    if (builder.tokenType == expected && test(builder.tokenText?.trim() ?: "")) {
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
        }
        false
    }
