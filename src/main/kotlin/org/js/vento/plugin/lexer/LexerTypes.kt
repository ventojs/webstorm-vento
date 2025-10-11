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
object LexerTypes {
    @JvmField
    var COMMENT_START: IElementType = LexerTokenType("VENTO_COMMENT_START")

    @JvmField
    var COMMENT_END: IElementType = LexerTokenType("VENTO_COMMENT_END")

    @JvmField
    var TRIM_COMMENT_START: IElementType = LexerTokenType("VENTO_TRIM_COMMENT_START")

    @JvmField
    var TRIM_COMMENT_END: IElementType = LexerTokenType("VENTO_TRIM_COMMENT_END")

    @JvmField
    var COMMENT_CONTENT: IElementType = LexerTokenType("VENTO_COMMENT_CONTENT")

    @JvmField
    var JAVASCRIPT_START: IElementType = LexerTokenType("VENTO_JAVASCRIPT_START")

    @JvmField
    var JAVASCRIPT_END: IElementType = LexerTokenType("VENTO_JAVASCRIPT_END")

    @JvmField
    val EXPRESSION = LexerTokenType("VENTO_EXPRESSION")

    @JvmField
    var VARIABLE_START: IElementType = LexerTokenType("VENTO_VARIABLE_START")

    @JvmField
    val VARIABLE_ELEMENT = LexerTokenType("VENTO_VARIABLE_ELEMENT")

    @JvmField
    var VARIABLE_END: IElementType = LexerTokenType("VENTO_VARIABLE_END")

    @JvmField
    var ERROR = LexerTokenType("VENTO_ERROR")

    @JvmField
    val COMMENT = LexerTokenType("VENTO_COMMENT")

    @JvmField
    val STRING = LexerTokenType("VENTO_STRING")

    @JvmField
    val REGEX = LexerTokenType("VENTO_REGEX")

    @JvmField
    val TEXT = LexerTokenType("VENTO_HTML_TAG")

    @JvmField
    val EMPTY_LINE = LexerTokenType("VENTO_EMPTY_LINE")

    @JvmField
    var FOR_START: IElementType = LexerTokenType("VENTO_FOR_START")

    @JvmField
    var FOR_KEY: IElementType = LexerTokenType("VENTO_FOR_KEY")

    @JvmField
    var CLOSE_FOR_KEY: IElementType = LexerTokenType("VENTO_CLOSE_FOR_KEY")

    @JvmField
    var FOR_VALUE: IElementType = LexerTokenType("VENTO_FOR_VALUE")

    @JvmField
    var FOR_OF: IElementType = LexerTokenType("VENTO_FOR_OF")

    @JvmField
    var FOR_COLLECTION: IElementType = LexerTokenType("VENTO_FOR_COLLECTION")

    @JvmField
    var FOR_END: IElementType = LexerTokenType("VENTO_FOR_END")

    @JvmField
    val PIPE_ELEMENT = LexerTokenType("VENTO_PIPE")

    @JvmField
    val IMPORT_START = LexerTokenType("VENTO_IMPORT_START")

    @JvmField
    val IMPORT_END = LexerTokenType("VENTO_IMPORT_END")

    @JvmField
    val IMPORT_KEY = LexerTokenType("VENTO_IMPORT_KEY")

    @JvmField
    val IMPORT_VALUES = LexerTokenType("VENTO_IMPORT_VALUES")

    @JvmField
    val IMPORT_FROM = LexerTokenType("VENTO_IMPORT_FROM")

    @JvmField
    val IMPORT_FILE = LexerTokenType("VENTO_IMPORT_FILE")

    @JvmField
    val EXPORT_START = LexerTokenType("VENTO_EXPORT_START")

    @JvmField
    val EXPORT_END = LexerTokenType("VENTO_EXPORT_END")

    @JvmField
    val EXPORT_FUNCTION_START = LexerTokenType("VENTO_EXPORT_FUNCTION_START")

    @JvmField
    val EXPORT_FUNCTION_END = LexerTokenType("VENTO_EXPORT_FUNCTION_END")

    @JvmField
    val EXPORT_CLOSE_START = LexerTokenType("VENTO_CLOSE_EXPORT_START")

    @JvmField
    val EXPORT_CLOSE_END = LexerTokenType("VENTO_CLOSE_EXPORT_END")

    @JvmField
    val EXPORT_KEY = LexerTokenType("VENTO_EXPORT_KEY")

    @JvmField
    val EXPORT_CLOSE_KEY = LexerTokenType("VENTO_EXPORT_KEY")

    @JvmField
    val EXPORT_VAR = LexerTokenType("VENTO_EXPORT_VAR")

    @JvmField
    val EXPORT_FUNCTION_ARGS = LexerTokenType("VENTO_FUNCTION_ARGS")

    @JvmField
    val EXPORT_FUNCTION_KEY = LexerTokenType("VENTO_EXPORT_FUNCTION_KEY")

    @JvmField
    val EXPORT_EQ = LexerTokenType("VENTO_EXPORT_EQUAL")

    @JvmField
    val EXPORT_VALUE = LexerTokenType("VENTO_EXPORT_VALUE")

    @JvmField
    val UNKNOWN = LexerTokenType("VENTO_UNKNOWN")

    @JvmField
    val BRACKET = LexerTokenType("VENTO_BRACKET")

    @JvmField
    val DOT = LexerTokenType("VENTO_DOT")
}
