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
import org.js.vento.plugin.highlighting.VentoTextAttributes.Companion.VENTO_COMMENT
import org.js.vento.plugin.lexer.VentoLexerAdapter
import org.js.vento.plugin.lexer.VentoTypes


class VentoSyntaxHighlighter : SyntaxHighlighterBase() {

    override fun getHighlightingLexer(): Lexer {
        return VentoLexerAdapter()
    }

    override fun getTokenHighlights(type: IElementType?): Array<out TextAttributesKey?> {

        val highlight = when (type) {
            VentoTypes.COMMENTED_CODE_START -> COMMENTED_CODE_START
            VentoTypes.COMMENTED_CODE_END -> COMMENTED_CODE_END
            VentoTypes.TRIMMED_COMMENTED_CODE_START -> TRIMMED_COMMENTED_CODE_START
            VentoTypes.TRIMMED_COMMENTED_CODE_END -> TRIMMED_COMMENTED_CODE_END
            VentoTypes.COMMENTED_CODE_CONTENT -> COMMENTED_CODE_CONTENT
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
        val COMMENTED_CODE_START =
            createTextAttributesKey(
                "VENTO_COMMENTED_CODE_START",
                DefaultLanguageHighlighterColors.TEMPLATE_LANGUAGE_COLOR
            )
        val COMMENTED_CODE_END =
            createTextAttributesKey(
                "VENTO_COMMENTED_CODE_END",
                DefaultLanguageHighlighterColors.TEMPLATE_LANGUAGE_COLOR
            )
        val TRIMMED_COMMENTED_CODE_START =
            createTextAttributesKey(
                "VENTO_TRIMMED_COMMENTED_CODE_START",
                DefaultLanguageHighlighterColors.TEMPLATE_LANGUAGE_COLOR
            )
        val TRIMMED_COMMENTED_CODE_END =
            createTextAttributesKey(
                "VENTO_TRIMMED_COMMENTED_CODE_END",
                DefaultLanguageHighlighterColors.TEMPLATE_LANGUAGE_COLOR
            )
        val COMMENTED_CODE_CONTENT = VENTO_COMMENT

        val EMPTY_KEYS: Array<TextAttributesKey?> = arrayOfNulls<TextAttributesKey>(0)

    }
}