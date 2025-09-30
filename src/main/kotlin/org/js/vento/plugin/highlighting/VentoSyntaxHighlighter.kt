/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

@file:Suppress("ktlint:standard:no-wildcard-imports")

package org.js.vento.plugin.highlighting

import com.intellij.lexer.Lexer
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
import org.js.vento.plugin.lexer.VentoLexerTypes

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
                VentoLexerTypes.OPEN_COMMENT_CLAUSE -> COMMENT
                VentoLexerTypes.CLOSE_COMMENT_CLAUSE -> COMMENT
                VentoLexerTypes.OPEN_TRIM_COMMENT_CLAUSE -> COMMENT
                VentoLexerTypes.CLOSE_TRIM_COMMENT_CLAUSE -> COMMENT
                VentoLexerTypes.COMMENTED_CONTENT -> COMMENTED_CONTENT
                VentoLexerTypes.JAVASCRIPT_START -> JAVASCRIPT
                VentoLexerTypes.JAVASCRIPT_END -> JAVASCRIPT
                VentoLexerTypes.VARIABLE_START -> VARIABLE
                VentoLexerTypes.VARIABLE_ELEMENT -> VARIABLE_ELEMENT
                VentoLexerTypes.VARIABLE_END -> VARIABLE
                VentoLexerTypes.TEXT -> TEXT
                VentoLexerTypes.ERROR -> ERROR
                VentoLexerTypes.PIPE_ELEMENT -> PIPE
                VentoLexerTypes.FOR_START -> VARIABLE
                VentoLexerTypes.FOR_KEY -> PIPE
                VentoLexerTypes.FOR_VALUE -> VENTO_EXPRESSION
                VentoLexerTypes.CLOSE_FOR_KEY -> PIPE
                VentoLexerTypes.FOR_OF -> PIPE
                VentoLexerTypes.FOR_COLLECTION -> VENTO_EXPRESSION
                VentoLexerTypes.FOR_END -> VARIABLE
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
        val VARIABLE = createTextAttributesKey("VENTO_VARIABLE", TEMPLATE_LANGUAGE_COLOR)
        val VARIABLE_ELEMENT = createTextAttributesKey("VENTO_VARIABLE_ELEMENT", GLOBAL_VARIABLE)
        val TEXT = createTextAttributesKey("VENTO_TEXT", STATIC_FIELD)
        val EMPTY_KEYS: Array<TextAttributesKey?> = arrayOfNulls<TextAttributesKey>(0)
        val ERROR = createTextAttributesKey("VENTO_ERROR", INVALID_STRING_ESCAPE)
        val PIPE = createTextAttributesKey("VENTO_PIPE", KEYWORD)
        val VENTO_EXPRESSION = createTextAttributesKey("VENTO_EXPRESSION", LOCAL_VARIABLE)
    }
}
