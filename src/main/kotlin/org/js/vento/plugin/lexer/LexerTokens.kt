/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.lexer

import com.intellij.psi.tree.IElementType

/**
 * Defines token and element types for the Vento language.
 * This includes both basic token types (like COMMENT, KEYWORD, etc.)
 * and any custom element types for the PSI structure.
 */
object LexerTokens {
    // Comment tokens
    @JvmField
    val COMMENT = LexerToken("VENTO_COMMENT")

    @JvmField
    var COMMENT_START: IElementType = LexerToken("VENTO_COMMENT_START")

    @JvmField
    var COMMENT_END: IElementType = LexerToken("VENTO_COMMENT_END")

    @JvmField
    var COMMENT_CONTENT: IElementType = LexerToken("VENTO_COMMENT_CONTENT")

    @JvmField
    var TRIM_COMMENT_START: IElementType = LexerToken("VENTO_TRIM_COMMENT_START")

    @JvmField
    var TRIM_COMMENT_END: IElementType = LexerToken("VENTO_TRIM_COMMENT_END")

    // JavaScript tokens
    @JvmField
    var JAVASCRIPT_START: IElementType = LexerToken("VENTO_JAVASCRIPT_START")

    @JvmField
    var JAVASCRIPT_END: IElementType = LexerToken("VENTO_JAVASCRIPT_END")

    // Variable tokens
    @JvmField
    var VARIABLE_START: IElementType = LexerToken("VENTO_VARIABLE_START")

    @JvmField
    val VARIABLE_ELEMENT = LexerToken("VENTO_VARIABLE_ELEMENT")

    @JvmField
    var VARIABLE_END: IElementType = LexerToken("VENTO_VARIABLE_END")

    // Expression and content type tokens
    @JvmField
    val EXPRESSION = LexerToken("VENTO_EXPRESSION")

    @JvmField
    val STRING = LexerToken("VENTO_STRING")

    @JvmField
    val REGEX = LexerToken("VENTO_REGEX")

    @JvmField
    val TEXT = LexerToken("VENTO_HTML_TAG")

    @JvmField
    val EMPTY_LINE = LexerToken("VENTO_EMPTY_LINE")

    // FOR loop tokens
    @JvmField
    var FOR_START: IElementType = LexerToken("VENTO_FOR_START")

    @JvmField
    var FOR_KEY: IElementType = LexerToken("VENTO_FOR_KEY")

    @JvmField
    var FOR_VALUE: IElementType = LexerToken("VENTO_FOR_VALUE")

    @JvmField
    var FOR_OF: IElementType = LexerToken("VENTO_FOR_OF")

    @JvmField
    var FOR_COLLECTION: IElementType = LexerToken("VENTO_FOR_COLLECTION")

    @JvmField
    var FOR_END: IElementType = LexerToken("VENTO_FOR_END")

    @JvmField
    var CLOSE_FOR_KEY: IElementType = LexerToken("VENTO_CLOSE_FOR_KEY")

    // Import tokens
    @JvmField
    val IMPORT_START = LexerToken("VENTO_IMPORT_START")

    @JvmField
    val IMPORT_KEY = LexerToken("VENTO_IMPORT_KEY")

    @JvmField
    val IMPORT_VALUES = LexerToken("VENTO_IMPORT_VALUES")

    @JvmField
    val IMPORT_FROM = LexerToken("VENTO_IMPORT_FROM")

    @JvmField
    val IMPORT_FILE = LexerToken("VENTO_IMPORT_FILE")

    @JvmField
    val IMPORT_END = LexerToken("VENTO_IMPORT_END")

    // Export tokens
    @JvmField
    val EXPORT_START = LexerToken("VENTO_EXPORT_START")

    @JvmField
    val EXPORT_KEY = LexerToken("VENTO_EXPORT_KEY")

    @JvmField
    val EXPORT_VAR = LexerToken("VENTO_EXPORT_VAR")

    @JvmField
    val EXPORT_EQ = LexerToken("VENTO_EXPORT_EQUAL")

    @JvmField
    val EXPORT_VALUE = LexerToken("VENTO_EXPORT_VALUE")

    @JvmField
    val EXPORT_END = LexerToken("VENTO_EXPORT_END")

    @JvmField
    val EXPORT_FUNCTION_START = LexerToken("VENTO_EXPORT_FUNCTION_START")

    @JvmField
    val EXPORT_FUNCTION_KEY = LexerToken("VENTO_EXPORT_FUNCTION_KEY")

    @JvmField
    val EXPORT_FUNCTION_ARGS = LexerToken("VENTO_FUNCTION_ARGS")

    @JvmField
    val EXPORT_FUNCTION_END = LexerToken("VENTO_EXPORT_FUNCTION_END")

    @JvmField
    val EXPORT_CLOSE_START = LexerToken("VENTO_CLOSE_EXPORT_START")

    @JvmField
    val EXPORT_CLOSE_KEY = LexerToken("VENTO_EXPORT_KEY")

    @JvmField
    val EXPORT_CLOSE_END = LexerToken("VENTO_CLOSE_EXPORT_END")

    // Set tokens
    @JvmField
    val SET_START = LexerToken("VENTO_SET_START")

    @JvmField
    val SET_KEY = LexerToken("VENTO_SET_KEY")

    @JvmField
    val SET_END = LexerToken("VENTO_SET_END")

    @JvmField
    val SET_CLOSE_START = LexerToken("VENTO_SET_CLOSE_START")

    @JvmField
    val SET_CLOSE_KEY = LexerToken("VENTO_SET_CLOSE_KEY")

    @JvmField
    val SET_CLOSE_END = LexerToken("VENTO_SET_CLOSE_END")

    // Utility tokens
    @JvmField
    val PIPE = LexerToken("VENTO_PIPE")

    @JvmField
    val BRACKET = LexerToken("VENTO_BRACKET")

    @JvmField
    val DOT = LexerToken("VENTO_DOT")

    @JvmField
    val EQUAL = LexerToken("VENTO_EQUAL")

    @JvmField
    val IDENTIFIER = LexerToken("VENTO_IDENTIFIER")

    // unknown tokens
    @JvmField
    val UNKNOWN = LexerToken("VENTO_UNKNOWN")
}
