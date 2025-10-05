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
object VentoLexerTypes {
    @JvmField
    var COMMENT_START: IElementType = VentoLexerTokenType("VENTO_COMMENT_START")

    @JvmField
    var COMMENT_END: IElementType = VentoLexerTokenType("VENTO_COMMENT_END")

    @JvmField
    var TRIM_COMMENT_START: IElementType = VentoLexerTokenType("VENTO_TRIM_COMMENT_START")

    @JvmField
    var TRIM_COMMENT_END: IElementType = VentoLexerTokenType("VENTO_TRIM_COMMENT_END")

    @JvmField
    var COMMENT_CONTENT: IElementType = VentoLexerTokenType("VENTO_COMMENT_CONTENT")

    @JvmField
    var JAVASCRIPT_START: IElementType = VentoLexerTokenType("VENTO_JAVASCRIPT_START")

    @JvmField
    var JAVASCRIPT_END: IElementType = VentoLexerTokenType("VENTO_JAVASCRIPT_END")

    @JvmField
    var VARIABLE_START: IElementType = VentoLexerTokenType("VENTO_VARIABLE_START")

    @JvmField
    val VARIABLE_ELEMENT = VentoLexerTokenType("VENTO_VARIABLE_ELEMENT")

    @JvmField
    var VARIABLE_END: IElementType = VentoLexerTokenType("VENTO_VARIABLE_END")

    @JvmField
    var ERROR = VentoLexerTokenType("VENTO_ERROR")

    @JvmField
    val COMMENT = VentoLexerTokenType("VENTO_COMMENT")

    @JvmField
    val STRING = VentoLexerTokenType("VENTO_STRING")

    @JvmField
    val TEXT = VentoLexerTokenType("VENTO_HTML_TAG")

    @JvmField
    val EMPTY_LINE = VentoLexerTokenType("VENTO_EMPTY_LINE")

    @JvmField
    var FOR_START: IElementType = VentoLexerTokenType("VENTO_FOR_START")

    @JvmField
    var FOR_KEY: IElementType = VentoLexerTokenType("VENTO_FOR_KEY")

    @JvmField
    var CLOSE_FOR_KEY: IElementType = VentoLexerTokenType("VENTO_CLOSE_FOR_KEY")

    @JvmField
    var FOR_VALUE: IElementType = VentoLexerTokenType("VENTO_FOR_VALUE")

    @JvmField
    var FOR_OF: IElementType = VentoLexerTokenType("VENTO_FOR_OF")

    @JvmField
    var FOR_COLLECTION: IElementType = VentoLexerTokenType("VENTO_FOR_COLLECTION")

    @JvmField
    var FOR_END: IElementType = VentoLexerTokenType("VENTO_FOR_END")

    @JvmField
    val PIPE_ELEMENT = VentoLexerTokenType("VENTO_PIPE")

    @JvmField
    val IMPORT_START = VentoLexerTokenType("VENTO_IMPORT_START")

    @JvmField
    val IMPORT_END = VentoLexerTokenType("VENTO_IMPORT_END")

    @JvmField
    val IMPORT_KEY = VentoLexerTokenType("VENTO_IMPORT_KEY")

    @JvmField
    val IMPORT_VALUES = VentoLexerTokenType("VENTO_IMPORT_VALUES")

    @JvmField
    val IMPORT_FROM = VentoLexerTokenType("VENTO_IMPORT_FROM")

    @JvmField
    val IMPORT_FILE = VentoLexerTokenType("VENTO_IMPORT_FILE")

    @JvmField
    val EXPORT_START = VentoLexerTokenType("VENTO_EXPORT_START")

    @JvmField
    val EXPORT_END = VentoLexerTokenType("VENTO_EXPORT_END")

    @JvmField
    val EXPORT_BLOCK_START = VentoLexerTokenType("VENTO_EXPORT_BLOCK_START")

    @JvmField
    val EXPORT_BLOCK_END = VentoLexerTokenType("VENTO_EXPORT_BLOCK_END")

    @JvmField
    val EXPORT_CLOSE_START = VentoLexerTokenType("VENTO_CLOSE_EXPORT_START")

    @JvmField
    val EXPORT_CLOSE_END = VentoLexerTokenType("VENTO_CLOSE_EXPORT_END")

    @JvmField
    val EXPORT_KEY = VentoLexerTokenType("VENTO_EXPORT_KEY")

    @JvmField
    val EXPORT_CLOSE_KEY = VentoLexerTokenType("VENTO_EXPORT_KEY")

    @JvmField
    val EXPORT_VAR = VentoLexerTokenType("VENTO_EXPORT_VAR")

    @JvmField
    val EXPORT_EQ = VentoLexerTokenType("VENTO_EXPORT_EQUAL")

    @JvmField
    val EXPORT_VALUE = VentoLexerTokenType("VENTO_EXPORT_VALUE")

    @JvmField
    val UNKNOWN = VentoLexerTokenType("VENTO_BAD_TOKEN")
}
