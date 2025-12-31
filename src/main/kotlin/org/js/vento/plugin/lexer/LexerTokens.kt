/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.lexer

import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet

/**
 * Defines token and element types for the Vento language.
 * This includes both basic token types (like COMMENT, KEYWORD, etc.)
 * and any custom element types for the PSI structure.
 */
object LexerTokens {
    @JvmField
    val VENTO_OUTER = LexerToken("VENTO_OUTER")

    @JvmField
    val JSBLOCK_OPEN = LexerToken("JSBLOCK_OPEN")

    @JvmField
    val JSBLOCK_CLOSE = LexerToken("JSBLOCK_CLOSE")

    @JvmField
    val FRONTMATTER_OPEN = LexerToken("FRONTMATTER_OPEN")

    @JvmField
    val FRONTMATTER_KEY = LexerToken("FRONTMATTER_KEY")

    @JvmField
    val FRONTMATTER_VALUE = LexerToken("FRONTMATTER_VALUE")

    @JvmField
    val FRONTMATTER_FLAG = LexerToken("FRONTMATTER_FLAG")

    @JvmField
    val FRONTMATTER_CLOSE = LexerToken("FRONTMATTER_CLOSE")

    @JvmField
    val VBLOCK_OPEN = LexerToken("VBLOCK_OPEN")

    @JvmField
    val VBLOCK_CLOSE = LexerToken("VBLOCK_CLOSE")

    // Comment tokens
    @JvmField
    var COMMENT_CONTENT: IElementType = LexerToken("COMMENT_CONTENT")

    @JvmField
    var COMMENT_END: IElementType = LexerToken("COMMENT_END")

    @JvmField
    var COMMENT_START: IElementType = LexerToken("COMMENT_START")

    // JavaScript tokens
    @JvmField
    var JAVASCRIPT: IElementType = LexerToken("JAVASCRIPT")

    @JvmField
    var STATEMENT: IElementType = LexerToken("STATEMENT")

    // For loop tokens
    @JvmField
    var FOR_CLOSE_KEY: IElementType = LexerToken("FOR_CLOSE_KEY")

    @JvmField
    var FOR_KEY: IElementType = LexerToken("FOR_KEY")

    @JvmField
    var FOR_OF: IElementType = LexerToken("FOR_OF")

    // Import tokens
    @JvmField
    val IMPORT_FROM = LexerToken("IMPORT_FROM")

    @JvmField
    val IMPORT_KEY = LexerToken("IMPORT_KEY")

    @JvmField
    val IMPORT_VALUES = LexerToken("IMPORT_VALUES")

    // Export tokens
    @JvmField
    val EXPORT_CLOSE_KEY = LexerToken("EXPORT_CLOSE_KEY")

    @JvmField
    val FUNCTION_ARG = LexerToken("FUNCTION_ARG")

    @JvmField
    val FUNCTION_KEY = LexerToken("FUNCTION_KEY")

    @JvmField
    val FUNCTION_NAME = LexerToken("FUNCTION_NAME")

    @JvmField
    val ASYNC_KEY = LexerToken("ASYNC_KEY")

    @JvmField
    val AWAIT_KEY = LexerToken("AWAIT_KEY")

    @JvmField
    val FUNCTION_CLOSE_KEY = LexerToken("FUNCTION_CLOSE_KEY")

    @JvmField
    val EXPORT_KEY = LexerToken("EXPORT_KEY")

    @JvmField
    val FRAGMENT_KEY = LexerToken("FRAGMENT_KEY")

    @JvmField
    val FRAGMENT_CLOSE_KEY = LexerToken("FRAGMENT_CLOSE_KEY")

    // Set tokens
    @JvmField
    val SET_CLOSE_KEY = LexerToken("SET_CLOSE_KEY")

    @JvmField
    val SET_KEY = LexerToken("SET_KEY")

    @JvmField
    val LAYOUT_KEY = LexerToken("LAYOUT_KEY")

    @JvmField
    val LAYOUT_CLOSE_KEY = LexerToken("LAYOUT_CLOSE_KEY")

    @JvmField
    val LAYOUT_SLOT_KEY = LexerToken("LAYOUT_SLOT_KEY")

    @JvmField
    val LAYOUT_SLOT_CLOSE_KEY = LexerToken("LAYOUT_SLOT_CLOSE")

    @JvmField
    val INCLUDE_KEY = LexerToken("INCLUDE_KEY")

    @JvmField
    val ECHO_KEY = LexerToken("ECHO_KEY")

    @JvmField
    val ECHO_CLOSE_KEY = LexerToken("ECHO_CLOSE_KEY")

    // General tokens
    @JvmField
    val BRACKET = LexerToken("BRACKET")

    @JvmField
    val PARENTHESIS = LexerToken("PARENTHESIS")

    @JvmField
    val DOT = LexerToken("DOT")

    @JvmField
    val COMMA = LexerToken("COMMA")

    @JvmField
    val EQUAL = LexerToken("EQUAL")

    @JvmField
    val SYMBOL = LexerToken("SYMBOL")

    @JvmField
    val PIPE = LexerToken("PIPE")

    @JvmField
    val IF_KEY = LexerToken("IF_KEY")

    @JvmField
    val ELSE_KEY = LexerToken("ELSE_KEY")

    @JvmField
    val ELSEIF_KEY = LexerToken("ELSEIF_KEY")

    @JvmField
    val IF_CLOSE_KEY = LexerToken("IF_CLOSE_KEY")

    @JvmField
    val REGEX = LexerToken("REGEX")

    @JvmField
    val STRING = LexerToken("STRING")

    @JvmField
    val NUMBER = LexerToken("NUMBER")

    @JvmField
    val BOOLEAN = LexerToken("BOOLEAN")

    @JvmField
    val COLON = LexerToken("COLON")

    @JvmField
    val SEMICOLON = LexerToken("SEMICOLON")

    @JvmField
    val HTML = LexerToken("HTML")

    @JvmField
    val UNKNOWN = LexerToken("UNKNOWN")

    @JvmField
    val FILE = LexerToken("FILE")

    @JvmField
    val BRACE = LexerToken("BRACE")

    @JvmField
    val LAMBDA_ARROW = LexerToken("LAMBDA_ARROW")

    @JvmField
    val PLUS = LexerToken("PLUS")

    @JvmField
    val ASTERISK = LexerToken("ASTERISK")

    @JvmField
    val MINUS = LexerToken("MINUS")

    @JvmField
    val NEW = LexerToken("NEW")

    @JvmField
    val INSTANCEOF = LexerToken("INSTANCEOF")

    @JvmField
    val TAGS: TokenSet =
        TokenSet.create(VBLOCK_OPEN, VBLOCK_CLOSE, JSBLOCK_OPEN, JSBLOCK_CLOSE, COMMENT_START, COMMENT_END)
}
