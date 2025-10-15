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
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors.KEYWORD
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors.LOCAL_VARIABLE
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors.STATIC_FIELD
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import com.intellij.ui.JBColor
import org.js.vento.plugin.lexer.LexerAdapter
import org.js.vento.plugin.lexer.LexerTokens.COMMENT_CONTENT
import org.js.vento.plugin.lexer.LexerTokens.COMMENT_END
import org.js.vento.plugin.lexer.LexerTokens.COMMENT_START
import org.js.vento.plugin.lexer.LexerTokens.EXPORT_CLOSE_END
import org.js.vento.plugin.lexer.LexerTokens.EXPORT_CLOSE_KEY
import org.js.vento.plugin.lexer.LexerTokens.EXPORT_CLOSE_START
import org.js.vento.plugin.lexer.LexerTokens.EXPORT_END
import org.js.vento.plugin.lexer.LexerTokens.EXPORT_EQ
import org.js.vento.plugin.lexer.LexerTokens.EXPORT_FUNCTION_ARGS
import org.js.vento.plugin.lexer.LexerTokens.EXPORT_FUNCTION_END
import org.js.vento.plugin.lexer.LexerTokens.EXPORT_FUNCTION_KEY
import org.js.vento.plugin.lexer.LexerTokens.EXPORT_FUNCTION_START
import org.js.vento.plugin.lexer.LexerTokens.EXPORT_KEY
import org.js.vento.plugin.lexer.LexerTokens.EXPORT_START
import org.js.vento.plugin.lexer.LexerTokens.EXPORT_VALUE
import org.js.vento.plugin.lexer.LexerTokens.EXPORT_VAR
import org.js.vento.plugin.lexer.LexerTokens.FILE
import org.js.vento.plugin.lexer.LexerTokens.FOR_CLOSE_KEY
import org.js.vento.plugin.lexer.LexerTokens.FOR_COLLECTION
import org.js.vento.plugin.lexer.LexerTokens.FOR_END
import org.js.vento.plugin.lexer.LexerTokens.FOR_KEY
import org.js.vento.plugin.lexer.LexerTokens.FOR_OF
import org.js.vento.plugin.lexer.LexerTokens.FOR_START
import org.js.vento.plugin.lexer.LexerTokens.FOR_VALUE
import org.js.vento.plugin.lexer.LexerTokens.IDENTIFIER
import org.js.vento.plugin.lexer.LexerTokens.IMPORT_END
import org.js.vento.plugin.lexer.LexerTokens.IMPORT_FILE
import org.js.vento.plugin.lexer.LexerTokens.IMPORT_FROM
import org.js.vento.plugin.lexer.LexerTokens.IMPORT_KEY
import org.js.vento.plugin.lexer.LexerTokens.IMPORT_START
import org.js.vento.plugin.lexer.LexerTokens.IMPORT_VALUES
import org.js.vento.plugin.lexer.LexerTokens.JAVASCRIPT_END
import org.js.vento.plugin.lexer.LexerTokens.JAVASCRIPT_START
import org.js.vento.plugin.lexer.LexerTokens.LAYOUT_CLOSE_END
import org.js.vento.plugin.lexer.LexerTokens.LAYOUT_CLOSE_KEY
import org.js.vento.plugin.lexer.LexerTokens.LAYOUT_CLOSE_START
import org.js.vento.plugin.lexer.LexerTokens.LAYOUT_END
import org.js.vento.plugin.lexer.LexerTokens.LAYOUT_KEY
import org.js.vento.plugin.lexer.LexerTokens.LAYOUT_START
import org.js.vento.plugin.lexer.LexerTokens.PIPE
import org.js.vento.plugin.lexer.LexerTokens.SET_CLOSE_END
import org.js.vento.plugin.lexer.LexerTokens.SET_CLOSE_KEY
import org.js.vento.plugin.lexer.LexerTokens.SET_CLOSE_START
import org.js.vento.plugin.lexer.LexerTokens.SET_END
import org.js.vento.plugin.lexer.LexerTokens.SET_KEY
import org.js.vento.plugin.lexer.LexerTokens.SET_START
import org.js.vento.plugin.lexer.LexerTokens.TEXT
import org.js.vento.plugin.lexer.LexerTokens.TRIM_COMMENT_END
import org.js.vento.plugin.lexer.LexerTokens.TRIM_COMMENT_START
import org.js.vento.plugin.lexer.LexerTokens.VARIABLE_ELEMENT
import org.js.vento.plugin.lexer.LexerTokens.VARIABLE_END
import org.js.vento.plugin.lexer.LexerTokens.VARIABLE_START
import java.awt.Color
import org.js.vento.plugin.lexer.LexerTokens.STRING as STRING_TOKEN

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
class SyntaxHighlighter : SyntaxHighlighterBase() {
    override fun getHighlightingLexer(): Lexer = LexerAdapter()

    override fun getTokenHighlights(type: IElementType?): Array<out TextAttributesKey?> {
        val highlight =
            when (type) {
                COMMENT_CONTENT -> COMMENTED_CONTENT
                COMMENT_END -> COMMENT
                COMMENT_START -> COMMENT
                EXPORT_CLOSE_END -> BLOCK
                EXPORT_CLOSE_KEY -> KEY_WORD
                EXPORT_CLOSE_START -> BLOCK
                EXPORT_END -> BLOCK
                EXPORT_EQ -> KEY_WORD
                EXPORT_FUNCTION_ARGS -> ARGS
                EXPORT_FUNCTION_END -> BLOCK
                EXPORT_FUNCTION_KEY -> KEY_WORD
                EXPORT_FUNCTION_START -> BLOCK
                EXPORT_KEY -> KEY_WORD
                EXPORT_START -> BLOCK
                EXPORT_VALUE -> VALUES
                EXPORT_VAR -> VALUES
                FOR_CLOSE_KEY -> KEY_WORD
                FOR_COLLECTION -> VALUES
                FOR_END -> BLOCK
                FOR_KEY -> KEY_WORD
                FOR_OF -> KEY_WORD
                FOR_START -> BLOCK
                FOR_VALUE -> VALUES
                IDENTIFIER -> VALUES
                IMPORT_END -> BLOCK
                IMPORT_FILE -> STRING
                FILE -> STRING
                IMPORT_FROM -> KEY_WORD
                IMPORT_KEY -> KEY_WORD
                IMPORT_START -> BLOCK
                IMPORT_VALUES -> VALUES
                JAVASCRIPT_END -> JAVASCRIPT
                JAVASCRIPT_START -> JAVASCRIPT
                PIPE -> KEY_WORD
                SET_CLOSE_END -> BLOCK
                SET_CLOSE_KEY -> KEY_WORD
                SET_CLOSE_START -> BLOCK
                SET_END -> BLOCK
                SET_KEY -> KEY_WORD
                SET_START -> BLOCK
                STRING_TOKEN -> STRING
                TEXT -> PLAIN_TEXT
                TRIM_COMMENT_END -> COMMENT
                TRIM_COMMENT_START -> COMMENT
                VARIABLE_ELEMENT -> VARIABLE
                VARIABLE_END -> BLOCK
                VARIABLE_START -> BLOCK
                LAYOUT_START -> BLOCK
                LAYOUT_END -> BLOCK
                LAYOUT_KEY -> KEY_WORD
                LAYOUT_CLOSE_START -> BLOCK
                LAYOUT_CLOSE_END -> BLOCK
                LAYOUT_CLOSE_KEY -> KEY_WORD
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
        val ventoSky = JBColor("sky", Color(12, 138, 183))
        val ventoNight = JBColor("night", Color(2, 2, 124))
        val ventoPink = JBColor("pink", Color(255, 0, 124))
        val ventoGray = JBColor("gray", Color(28, 32, 40))
        val ventoLightRed = JBColor("light-red", Color(244, 100, 99))
        val ventoLightGray = JBColor("light-gray", Color(157, 166, 187))
        val VBLOCK = createTextAttributesKey("VENTO_BLOCK", TextAttributes(ventoSky, ventoGray, null, null, 0))
        val JSBLOCK = createTextAttributesKey("JS_BLOCK", TextAttributes(ventoPink, ventoGray, null, null, 0))
        val COMMENT = createTextAttributesKey("VENTO_COMMENTED", DOC_COMMENT)
        val COMMENTED_CONTENT = createTextAttributesKey("VENTO_COMMENT", DOC_COMMENT_MARKUP)
        val JAVASCRIPT = createTextAttributesKey("VENTO_JAVASCRIPT", JSBLOCK)
        val BLOCK = createTextAttributesKey("VENTO_VARIABLE", VBLOCK)
        val VARIABLE = createTextAttributesKey("VENTO_VARIABLE_ELEMENT", GLOBAL_VARIABLE)
        val PLAIN_TEXT = createTextAttributesKey("VENTO_TEXT", STATIC_FIELD)
        val EMPTY_KEYS: Array<TextAttributesKey?> = arrayOfNulls<TextAttributesKey>(0)
        val KEY_WORD = createTextAttributesKey("VENTO_PIPE", KEYWORD)
        val ARGS = createTextAttributesKey("VENTO_ARGS", DefaultLanguageHighlighterColors.PARAMETER)
        val VALUES = createTextAttributesKey("VENTO_EXPRESSION", LOCAL_VARIABLE)
        val STRING = createTextAttributesKey("VENTO_STRING", DefaultLanguageHighlighterColors.STRING)
    }
}
