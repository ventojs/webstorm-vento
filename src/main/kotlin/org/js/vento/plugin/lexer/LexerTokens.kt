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

    // Control flow tokens (if/else, for)
    @JvmField
    val ELSE_KEY = LexerToken("ELSE_KEY")

    @JvmField
    val ELSEIF_KEY = LexerToken("ELSEIF_KEY")

    @JvmField
    var FOR_CLOSE_KEY: IElementType = LexerToken("FOR_CLOSE_KEY")

    @JvmField
    var FOR_KEY: IElementType = LexerToken("FOR_KEY")

    @JvmField
    var FOR_OF: IElementType = LexerToken("FOR_OF")

    @JvmField
    val IF_CLOSE_KEY = LexerToken("IF_CLOSE_KEY")

    @JvmField
    val IF_KEY = LexerToken("IF_KEY")

    // Function tokens
    @JvmField
    val ASYNC_KEY = LexerToken("ASYNC_KEY")

    @JvmField
    val AWAIT_KEY = LexerToken("AWAIT_KEY")

    @JvmField
    val FUNCTION_ARG = LexerToken("FUNCTION_ARG")

    @JvmField
    val FUNCTION_CLOSE_KEY = LexerToken("FUNCTION_CLOSE_KEY")

    @JvmField
    val FUNCTION_KEY = LexerToken("FUNCTION_KEY")

    @JvmField
    val FUNCTION_NAME = LexerToken("FUNCTION_NAME")

    // Import/Export tokens
    @JvmField
    val EXPORT_CLOSE_KEY = LexerToken("EXPORT_CLOSE_KEY")

    @JvmField
    val EXPORT_KEY = LexerToken("EXPORT_KEY")

    @JvmField
    val IMPORT_FROM = LexerToken("IMPORT_FROM")

    @JvmField
    val IMPORT_KEY = LexerToken("IMPORT_KEY")

    @JvmField
    val IMPORT_VALUES = LexerToken("IMPORT_VALUES")

    // Layout tokens
    @JvmField
    val LAYOUT_CLOSE_KEY = LexerToken("LAYOUT_CLOSE_KEY")

    @JvmField
    val LAYOUT_KEY = LexerToken("LAYOUT_KEY")

    @JvmField
    val LAYOUT_SLOT_CLOSE_KEY = LexerToken("LAYOUT_SLOT_CLOSE")

    @JvmField
    val LAYOUT_SLOT_KEY = LexerToken("LAYOUT_SLOT_KEY")

    // Block tokens
    @JvmField
    val JSBLOCK_CLOSE = LexerToken("JSBLOCK_CLOSE")

    @JvmField
    val JSBLOCK_OPEN = LexerToken("JSBLOCK_OPEN")

    @JvmField
    val VBLOCK_CLOSE = LexerToken("VBLOCK_CLOSE")

    @JvmField
    val VBLOCK_OPEN = LexerToken("VBLOCK_OPEN")

    // Frontmatter tokens
    @JvmField
    val FRONTMATTER_CLOSE = LexerToken("FRONTMATTER_CLOSE")

    @JvmField
    val FRONTMATTER_FLAG = LexerToken("FRONTMATTER_FLAG")

    @JvmField
    val FRONTMATTER_KEY = LexerToken("FRONTMATTER_KEY")

    @JvmField
    val FRONTMATTER_OPEN = LexerToken("FRONTMATTER_OPEN")

    @JvmField
    val FRONTMATTER_VALUE = LexerToken("FRONTMATTER_VALUE")

    // Echo tokens
    @JvmField
    val ECHO_CLOSE_KEY = LexerToken("ECHO_CLOSE_KEY")

    @JvmField
    val ECHO_KEY = LexerToken("ECHO_KEY")

    // Set tokens
    @JvmField
    val SET_CLOSE_KEY = LexerToken("SET_CLOSE_KEY")

    @JvmField
    val SET_KEY = LexerToken("SET_KEY")

    // Other keyword tokens
    @JvmField
    val DEFAULT_CLOSE_KEY = LexerToken("DEFAULT_CLOSE_KEY")

    @JvmField
    val DEFAULT_KEY = LexerToken("DEFAULT_KEY")

    @JvmField
    val FRAGMENT_CLOSE_KEY = LexerToken("FRAGMENT_CLOSE_KEY")

    @JvmField
    val FRAGMENT_KEY = LexerToken("FRAGMENT_KEY")

    @JvmField
    val INCLUDE_KEY = LexerToken("INCLUDE_KEY")

    // Punctuation and operators
    @JvmField
    val ASTERISK = LexerToken("ASTERISK")

    @JvmField
    val COLON = LexerToken("COLON")

    @JvmField
    val COMMA = LexerToken("COMMA")

    @JvmField
    val DOT = LexerToken("DOT")

    @JvmField
    val EQUAL = LexerToken("EQUAL")

    @JvmField
    val EXPAND = LexerToken("EXPAND")

    @JvmField
    val INSTANCEOF = LexerToken("INSTANCEOF")

    @JvmField
    val LAMBDA_ARROW = LexerToken("LAMBDA_ARROW")

    @JvmField
    val MINUS = LexerToken("MINUS")

    @JvmField
    val NEW = LexerToken("NEW")

    @JvmField
    val PIPE = LexerToken("PIPE")

    @JvmField
    val PLUS = LexerToken("PLUS")

    @JvmField
    val SEMICOLON = LexerToken("SEMICOLON")

    // Literal values
    @JvmField
    val BOOLEAN = LexerToken("BOOLEAN")

    @JvmField
    val NUMBER = LexerToken("NUMBER")

    @JvmField
    val REGEX = LexerToken("REGEX")

    @JvmField
    val STRING = LexerToken("STRING")

    // Destructuring tokens
    @JvmField
    val DESTRUCTURE_BRACE = LexerToken("DESTRUCTURE_BRACE")

    @JvmField
    val DESTRUCTURE_BRACKET = LexerToken("DESTRUCTURE_BRACKET")

    @JvmField
    val DESTRUCTURE_KEY = LexerToken("DESTRUCTURE_KEY")

    // General tokens
    @JvmField
    val BRACE = LexerToken("BRACE")

    @JvmField
    val BRACKET = LexerToken("BRACKET")

    @JvmField
    val FILE = LexerToken("FILE")

    @JvmField
    val HTML = LexerToken("HTML")

    @JvmField
    val PARENTHESIS = LexerToken("PARENTHESIS")

    @JvmField
    val SYMBOL = LexerToken("SYMBOL")

    @JvmField
    val UNKNOWN = LexerToken("UNKNOWN")

    @JvmField
    val VENTO_OUTER = LexerToken("VENTO_OUTER")

    @JvmField
    val TAGS: TokenSet =
        TokenSet.create(VBLOCK_OPEN, VBLOCK_CLOSE, JSBLOCK_OPEN, JSBLOCK_CLOSE, COMMENT_START, COMMENT_END)
}
