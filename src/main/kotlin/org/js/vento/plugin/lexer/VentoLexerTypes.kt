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
    var OPEN_COMMENT_CLAUSE: IElementType = VentoLexerTokenType("VENTO_COMMENTED_START")

    @JvmField
    var CLOSE_COMMENT_CLAUSE: IElementType = VentoLexerTokenType("VENTO_COMMENTED_END")

    @JvmField
    var OPEN_TRIM_COMMENT_CLAUSE: IElementType = VentoLexerTokenType("VENTO_TRIMMED_COMMENTED_START")

    @JvmField
    var CLOSE_TRIM_COMMENT_CLAUSE: IElementType = VentoLexerTokenType("VENTO_TRIMMED_COMMENTED_END")

    @JvmField
    var COMMENTED_CONTENT: IElementType = VentoLexerTokenType("VENTO_COMMENTED_CONTENT")

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
}
