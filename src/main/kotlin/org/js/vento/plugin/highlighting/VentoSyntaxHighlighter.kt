/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

@file:Suppress("ktlint:standard:no-wildcard-imports")

package org.js.vento.plugin.highlighting

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors.DOC_COMMENT
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors.DOC_COMMENT_MARKUP
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors.GLOBAL_VARIABLE
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors.INVALID_STRING_ESCAPE
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors.KEYWORD
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors.LOCAL_VARIABLE
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors.STATIC_FIELD
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors.TEMPLATE_LANGUAGE_COLOR
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import org.js.vento.plugin.lexer.VentoLexerAdapter
import org.js.vento.plugin.lexer.VentoLexerTypes.CLOSE_FOR_KEY
import org.js.vento.plugin.lexer.VentoLexerTypes.COMMENT_CONTENT
import org.js.vento.plugin.lexer.VentoLexerTypes.COMMENT_END
import org.js.vento.plugin.lexer.VentoLexerTypes.COMMENT_START
import org.js.vento.plugin.lexer.VentoLexerTypes.ERROR
import org.js.vento.plugin.lexer.VentoLexerTypes.FOR_COLLECTION
import org.js.vento.plugin.lexer.VentoLexerTypes.FOR_END
import org.js.vento.plugin.lexer.VentoLexerTypes.FOR_KEY
import org.js.vento.plugin.lexer.VentoLexerTypes.FOR_OF
import org.js.vento.plugin.lexer.VentoLexerTypes.FOR_START
import org.js.vento.plugin.lexer.VentoLexerTypes.FOR_VALUE
import org.js.vento.plugin.lexer.VentoLexerTypes.IMPORT_END
import org.js.vento.plugin.lexer.VentoLexerTypes.IMPORT_FILE
import org.js.vento.plugin.lexer.VentoLexerTypes.IMPORT_FROM
import org.js.vento.plugin.lexer.VentoLexerTypes.IMPORT_KEY
import org.js.vento.plugin.lexer.VentoLexerTypes.IMPORT_START
import org.js.vento.plugin.lexer.VentoLexerTypes.IMPORT_VALUES
import org.js.vento.plugin.lexer.VentoLexerTypes.JAVASCRIPT_END
import org.js.vento.plugin.lexer.VentoLexerTypes.JAVASCRIPT_START
import org.js.vento.plugin.lexer.VentoLexerTypes.PIPE_ELEMENT
import org.js.vento.plugin.lexer.VentoLexerTypes.TEXT
import org.js.vento.plugin.lexer.VentoLexerTypes.TRIM_COMMENT_END
import org.js.vento.plugin.lexer.VentoLexerTypes.TRIM_COMMENT_START
import org.js.vento.plugin.lexer.VentoLexerTypes.VARIABLE_ELEMENT
import org.js.vento.plugin.lexer.VentoLexerTypes.VARIABLE_END
import org.js.vento.plugin.lexer.VentoLexerTypes.VARIABLE_START

/**
 * Handles syntax highlighting for the Vento language in the IntelliJ Platform.
 *
 * This class is responsible for defining the colorful representation of various
 * Vento language constructs in the editor, such as comments, JavaScript blocks,
 * and variable elements. It extends the `SyntaxHighlighterBase` class to implement
 * custom highlighting behavior tailored to the Vento language.
 *
 * Key functionalities:
 * - Provides a lexer for tokenizing Vento code.
 * - Maps token types to their respective highlighting attributes.
 */
class VentoSyntaxHighlighter : SyntaxHighlighterBase() {
    override fun getHighlightingLexer(): Lexer = VentoLexerAdapter()

    override fun getTokenHighlights(type: IElementType?): Array<out TextAttributesKey?> {
        val highlight =
            when (type) {
                COMMENT_START -> COMMENT
                COMMENT_END -> COMMENT
                TRIM_COMMENT_START -> COMMENT
                TRIM_COMMENT_END -> COMMENT
                COMMENT_CONTENT -> COMMENTED_CONTENT
                JAVASCRIPT_START -> JAVASCRIPT
                JAVASCRIPT_END -> JAVASCRIPT
                VARIABLE_START -> BLOCK
                VARIABLE_ELEMENT -> VARIABLE
                VARIABLE_END -> BLOCK
                TEXT -> PLAIN_TEXT
                ERROR -> HIGHLIGHT_ERROR
                PIPE_ELEMENT -> KEY_WORD
                FOR_START -> BLOCK
                FOR_KEY -> KEY_WORD
                FOR_VALUE -> VALUES
                CLOSE_FOR_KEY -> KEY_WORD
                FOR_OF -> KEY_WORD
                FOR_COLLECTION -> VALUES
                FOR_END -> BLOCK
                IMPORT_START -> BLOCK
                IMPORT_END -> BLOCK
                IMPORT_KEY -> KEY_WORD
                IMPORT_VALUES -> VALUES
                IMPORT_FROM -> KEY_WORD
                IMPORT_FILE -> STRING
                else -> null
            }

        return when {
            highlight === null -> {
                EMPTY_KEYS
            }

            else -> {
                arrayOf(highlight)
            }
        }
    }

    companion object {
        val COMMENT = createTextAttributesKey("VENTO_COMMENTED", DOC_COMMENT)
        val COMMENTED_CONTENT = createTextAttributesKey("VENTO_COMMENT", DOC_COMMENT_MARKUP)
        val JAVASCRIPT = createTextAttributesKey("VENTO_JAVASCRIPT", TEMPLATE_LANGUAGE_COLOR)
        val BLOCK = createTextAttributesKey("VENTO_VARIABLE", TEMPLATE_LANGUAGE_COLOR)
        val VARIABLE = createTextAttributesKey("VENTO_VARIABLE_ELEMENT", GLOBAL_VARIABLE)
        val PLAIN_TEXT = createTextAttributesKey("VENTO_TEXT", STATIC_FIELD)
        val EMPTY_KEYS: Array<TextAttributesKey?> = arrayOfNulls<TextAttributesKey>(0)
        val HIGHLIGHT_ERROR = createTextAttributesKey("VENTO_ERROR", INVALID_STRING_ESCAPE)
        val KEY_WORD = createTextAttributesKey("VENTO_PIPE", KEYWORD)
        val VALUES = createTextAttributesKey("VENTO_EXPRESSION", LOCAL_VARIABLE)
        val STRING = createTextAttributesKey("VENTO_STRING", DefaultLanguageHighlighterColors.STRING)
    }
}
