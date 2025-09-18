/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.highlighting

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import org.js.vento.plugin.lexer.VentoLexerAdapter
import org.js.vento.plugin.lexer.VentoLexerTypes
import org.js.vento.plugin.parser.VentoParserTypes

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
                VentoLexerTypes.COMMENTED_START -> COMMENT
                VentoLexerTypes.CLOSE_COMMENT_CLAUSE -> COMMENT
                VentoLexerTypes.TRIMMED_COMMENTED_START -> COMMENT
                VentoLexerTypes.CLOSE_COMMENT_TRIM_CLAUSE -> COMMENT
                VentoLexerTypes.COMMENTED_CONTENT -> COMMENTED_CONTENT
                VentoLexerTypes.JAVASCRIPT_START -> JAVASCRIPT
                VentoLexerTypes.JAVASCRIPT_END -> JAVASCRIPT
                VentoLexerTypes.VARIABLE_START -> VARIABLE
                VentoLexerTypes.VARIABLE_ELEMENT -> VARIABLE_ELEMENT
                VentoParserTypes.VARIABLE_PIPES -> VARIABLE_PIPES
                VentoLexerTypes.VARIABLE_END -> VARIABLE
                VentoLexerTypes.TEXT -> TEXT
                VentoLexerTypes.HTML_TAG -> HTML
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
        val COMMENT =
            createTextAttributesKey(
                "VENTO_COMMENTED",
                DefaultLanguageHighlighterColors.DOC_COMMENT,
            )

        val COMMENTED_CONTENT =
            createTextAttributesKey(
                "VENTO_COMMENT",
                DefaultLanguageHighlighterColors.DOC_COMMENT_MARKUP,
            )

        val JAVASCRIPT =
            createTextAttributesKey(
                "VENTO_JAVASCRIPT",
                DefaultLanguageHighlighterColors.TEMPLATE_LANGUAGE_COLOR,
            )

        val VARIABLE =
            createTextAttributesKey(
                "VENTO_VARIABLE",
                DefaultLanguageHighlighterColors.TEMPLATE_LANGUAGE_COLOR,
            )

        val VARIABLE_ELEMENT =
            createTextAttributesKey(
                "VENTO_VARIABLE_ELEMENT",
                DefaultLanguageHighlighterColors.GLOBAL_VARIABLE,
            )

        val VARIABLE_PIPES =
            createTextAttributesKey(
                "VENTO_VARIABLE_PIPES",
                DefaultLanguageHighlighterColors.KEYWORD,
            )

        val TEXT =
            createTextAttributesKey(
                "VENTO_TEXT",
                DefaultLanguageHighlighterColors.STATIC_FIELD,
            )

        val HTML =
            createTextAttributesKey(
                "VENTO_HTML",
                DefaultLanguageHighlighterColors.MARKUP_TAG,
            )

        val EMPTY_KEYS: Array<TextAttributesKey?> = arrayOfNulls<TextAttributesKey>(0)
    }
}
