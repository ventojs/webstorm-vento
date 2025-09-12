/*
 * Copyright (c) 2023 Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.highlighting

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import org.js.vento.plugin.VentoTypes
import org.js.vento.plugin.lexer.VentoLexerAdapter


class VentoSyntaxHighlighter : SyntaxHighlighterBase() {

    override fun getHighlightingLexer(): Lexer {
        return VentoLexerAdapter()
    }

    override fun getTokenHighlights(type: IElementType?): Array<out TextAttributesKey?> {

        val highlight = when (type) {
            VentoTypes.COMMENTED_START -> COMMENT
            VentoTypes.COMMENTED_END -> COMMENT
            VentoTypes.TRIMMED_COMMENTED_START -> COMMENT
            VentoTypes.TRIMMED_COMMENTED_END -> COMMENT
            VentoTypes.COMMENTED_CONTENT -> COMMENTED_CONTENT
            VentoTypes.JAVASCRIPT_START -> JAVASCRIPT
            VentoTypes.JAVASCRIPT_END -> JAVASCRIPT
            VentoTypes.VARIABLE_START -> VARIABLE
            VentoTypes.VARIABLE_ELEMENT -> VARIABLE_ELEMENT
            VentoTypes.VARIABLE_PIPES -> VARIABLE_PIPES
            VentoTypes.VARIABLE_END -> VARIABLE
            else -> null
        }

        return when {
            highlight === null -> {
                EMPTY_KEYS
            }

            else -> {
                arrayOf<TextAttributesKey>(highlight)
            }
        }
    }

    companion object {
        val COMMENT =
            createTextAttributesKey(
                "VENTO_COMMENTED",
                DefaultLanguageHighlighterColors.DOC_COMMENT
            )

        val COMMENTED_CONTENT =
            createTextAttributesKey(
                "VENTO_COMMENT",
                DefaultLanguageHighlighterColors.DOC_COMMENT_MARKUP
            )

        val JAVASCRIPT =
            createTextAttributesKey(
                "VENTO_JAVASCRIPT",
                DefaultLanguageHighlighterColors.TEMPLATE_LANGUAGE_COLOR
            )

        val VARIABLE =
            createTextAttributesKey(
                "VENTO_VARIABLE",
                DefaultLanguageHighlighterColors.TEMPLATE_LANGUAGE_COLOR
            )

        val VARIABLE_ELEMENT =
            createTextAttributesKey(
                "VENTO_VARIABLE_ELEMENT",
                DefaultLanguageHighlighterColors.GLOBAL_VARIABLE
            )

        val VARIABLE_PIPES =
            createTextAttributesKey(
                "VENTO_VARIABLE_PIPES",
                DefaultLanguageHighlighterColors.KEYWORD
            )

        val EMPTY_KEYS: Array<TextAttributesKey?> = arrayOfNulls<TextAttributesKey>(0)

    }
}