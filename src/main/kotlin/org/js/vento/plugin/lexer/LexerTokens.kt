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
    val COMMENT = LexerToken("COMMENT")

    @JvmField
    var COMMENT_CONTENT: IElementType = LexerToken("COMMENT_CONTENT")

    @JvmField
    var COMMENT_END: IElementType = LexerToken("COMMENT_END")

    @JvmField
    var COMMENT_START: IElementType = LexerToken("COMMENT_START")

    @JvmField
    var TRIM_COMMENT_END: IElementType = LexerToken("TRIM_COMMENT_END")

    @JvmField
    var TRIM_COMMENT_START: IElementType = LexerToken("TRIM_COMMENT_START")

    // JavaScript tokens
    @JvmField
    val EXPRESSION = LexerToken("EXPRESSION")

    @JvmField
    var JAVASCRIPT_END: IElementType = LexerToken("JAVASCRIPT_END")

    @JvmField
    var JAVASCRIPT_START: IElementType = LexerToken("JAVASCRIPT_START")

    // Variable tokens
    @JvmField
    val VARIABLE_ELEMENT = LexerToken("VARIABLE_ELEMENT")

    @JvmField
    var VARIABLE_END: IElementType = LexerToken("VARIABLE_END")

    @JvmField
    var VARIABLE_START: IElementType = LexerToken("VARIABLE_START")

    // For loop tokens
    @JvmField
    var FOR_CLOSE_KEY: IElementType = LexerToken("FOR_CLOSE_KEY")

    @JvmField
    var FOR_COLLECTION: IElementType = LexerToken("FOR_COLLECTION")

    @JvmField
    var FOR_END: IElementType = LexerToken("FOR_END")

    @JvmField
    var FOR_KEY: IElementType = LexerToken("FOR_KEY")

    @JvmField
    var FOR_OF: IElementType = LexerToken("FOR_OF")

    @JvmField
    var FOR_START: IElementType = LexerToken("FOR_START")

    @JvmField
    var FOR_VALUE: IElementType = LexerToken("FOR_VALUE")

    // Import tokens
    @JvmField
    val IMPORT_END = LexerToken("IMPORT_END")

    @JvmField
    val IMPORT_FILE = LexerToken("IMPORT_FILE")

    @JvmField
    val IMPORT_FROM = LexerToken("IMPORT_FROM")

    @JvmField
    val IMPORT_KEY = LexerToken("IMPORT_KEY")

    @JvmField
    val IMPORT_START = LexerToken("IMPORT_START")

    @JvmField
    val IMPORT_VALUES = LexerToken("IMPORT_VALUES")

    // Export tokens
    @JvmField
    val EXPORT_CLOSE_END = LexerToken("CLOSE_EXPORT_END")

    @JvmField
    val EXPORT_CLOSE_KEY = LexerToken("EXPORT_KEY")

    @JvmField
    val EXPORT_CLOSE_START = LexerToken("CLOSE_EXPORT_START")

    @JvmField
    val EXPORT_END = LexerToken("EXPORT_END")

    @JvmField
    val EXPORT_EQ = LexerToken("EXPORT_EQUAL")

    @JvmField
    val EXPORT_FUNCTION_ARGS = LexerToken("FUNCTION_ARGS")

    @JvmField
    val EXPORT_FUNCTION_END = LexerToken("EXPORT_FUNCTION_END")

    @JvmField
    val EXPORT_FUNCTION_KEY = LexerToken("EXPORT_FUNCTION_KEY")

    @JvmField
    val EXPORT_FUNCTION_START = LexerToken("EXPORT_FUNCTION_START")

    @JvmField
    val EXPORT_KEY = LexerToken("EXPORT_KEY")

    @JvmField
    val EXPORT_START = LexerToken("EXPORT_START")

    @JvmField
    val EXPORT_VALUE = LexerToken("EXPORT_VALUE")

    @JvmField
    val EXPORT_VAR = LexerToken("EXPORT_VAR")

    // Set tokens
    @JvmField
    val SET_CLOSE_END = LexerToken("SET_CLOSE_END")

    @JvmField
    val SET_CLOSE_KEY = LexerToken("SET_CLOSE_KEY")

    @JvmField
    val SET_CLOSE_START = LexerToken("SET_CLOSE_START")

    @JvmField
    val SET_END = LexerToken("SET_END")

    @JvmField
    val SET_KEY = LexerToken("SET_KEY")

    @JvmField
    val SET_START = LexerToken("SET_START")

    // layout tokens
    @JvmField
    val LAYOUT_START = LexerToken("LAYOUT_START")

    @JvmField
    val LAYOUT_CLOSE_START = LexerToken("LAYOUT_CLOSE_START")

    @JvmField
    val LAYOUT_KEY = LexerToken("LAYOUT_KEY")

    @JvmField
    val LAYOUT_CLOSE_KEY = LexerToken("LAYOUT_CLOSE_KEY")

    @JvmField
    val LAYOUT_CLOSE_END = LexerToken("LAYOUT_CLOSE_END")

    @JvmField
    val LAYOUT_END = LexerToken("LAYOUT_END")

    @JvmField
    val LAYOUT_SLOT_START = LexerToken("LAYOUT_SLOT_START")

    @JvmField
    val LAYOUT_SLOT_END = LexerToken("LAYOUT_SLOT_END")

    @JvmField
    val LAYOUT_SLOT_CLOSE_START = LexerToken("LAYOUT_SLOT_CLOSE_START")

    @JvmField
    val LAYOUT_SLOT_CLOSE_END = LexerToken("LAYOUT_SLOT_CLOSE_END")

    @JvmField
    val LAYOUT_SLOT_KEY = LexerToken("LAYOUT_SLOT_KEY")

    @JvmField
    val LAYOUT_SLOT_CLOSE_KEY = LexerToken("LAYOUT_SLOT_CLOSE")

    // General tokens
    @JvmField
    val BRACKET = LexerToken("BRACKET")

    @JvmField
    val DOT = LexerToken("DOT")

    @JvmField
    val EMPTY_LINE = LexerToken("EMPTY_LINE")

    @JvmField
    val EQUAL = LexerToken("EQUAL")

    @JvmField
    val IDENTIFIER = LexerToken("IDENTIFIER")

    @JvmField
    val PIPE = LexerToken("PIPE")

    @JvmField
    val REGEX = LexerToken("REGEX")

    @JvmField
    val STRING = LexerToken("STRING")

    @JvmField
    val TEXT = LexerToken("TEXT")

    @JvmField
    val HTML = LexerToken("HTML")

    @JvmField
    val UNKNOWN = LexerToken("UNKNOWN")

    @JvmField
    val FILE = LexerToken("FILE")

    @JvmField
    val OBJECT = LexerToken("OBJECT")
}
