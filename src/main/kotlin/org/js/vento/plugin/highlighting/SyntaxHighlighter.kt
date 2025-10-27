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
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors.KEYWORD
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors.LOCAL_VARIABLE
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors.STATIC_FIELD
import com.intellij.openapi.editor.HighlighterColors.BAD_CHARACTER
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import com.intellij.ui.JBColor
import org.js.vento.plugin.lexer.LexerAdapter
import org.js.vento.plugin.lexer.LexerTokens.BOOLEAN
import org.js.vento.plugin.lexer.LexerTokens.BRACE
import org.js.vento.plugin.lexer.LexerTokens.BRACKET
import org.js.vento.plugin.lexer.LexerTokens.COLON
import org.js.vento.plugin.lexer.LexerTokens.COMMA
import org.js.vento.plugin.lexer.LexerTokens.COMMENT_CONTENT
import org.js.vento.plugin.lexer.LexerTokens.COMMENT_END
import org.js.vento.plugin.lexer.LexerTokens.COMMENT_START
import org.js.vento.plugin.lexer.LexerTokens.DOT
import org.js.vento.plugin.lexer.LexerTokens.EQUAL
import org.js.vento.plugin.lexer.LexerTokens.EXPORT_CLOSE_KEY
import org.js.vento.plugin.lexer.LexerTokens.EXPORT_EQ
import org.js.vento.plugin.lexer.LexerTokens.EXPORT_KEY
import org.js.vento.plugin.lexer.LexerTokens.EXPORT_VALUE
import org.js.vento.plugin.lexer.LexerTokens.EXPORT_VAR
import org.js.vento.plugin.lexer.LexerTokens.FILE
import org.js.vento.plugin.lexer.LexerTokens.FOR_CLOSE_KEY
import org.js.vento.plugin.lexer.LexerTokens.FOR_COLLECTION
import org.js.vento.plugin.lexer.LexerTokens.FOR_KEY
import org.js.vento.plugin.lexer.LexerTokens.FOR_OF
import org.js.vento.plugin.lexer.LexerTokens.FOR_VALUE
import org.js.vento.plugin.lexer.LexerTokens.FUNCTION_ARGS
import org.js.vento.plugin.lexer.LexerTokens.FUNCTION_KEY
import org.js.vento.plugin.lexer.LexerTokens.IMPORT_FILE
import org.js.vento.plugin.lexer.LexerTokens.IMPORT_FROM
import org.js.vento.plugin.lexer.LexerTokens.IMPORT_KEY
import org.js.vento.plugin.lexer.LexerTokens.IMPORT_VALUES
import org.js.vento.plugin.lexer.LexerTokens.INCLUDE_KEY
import org.js.vento.plugin.lexer.LexerTokens.JAVASCRIPT_END
import org.js.vento.plugin.lexer.LexerTokens.JAVASCRIPT_START
import org.js.vento.plugin.lexer.LexerTokens.LAYOUT_CLOSE_KEY
import org.js.vento.plugin.lexer.LexerTokens.LAYOUT_KEY
import org.js.vento.plugin.lexer.LexerTokens.LAYOUT_SLOT_CLOSE_KEY
import org.js.vento.plugin.lexer.LexerTokens.LAYOUT_SLOT_KEY
import org.js.vento.plugin.lexer.LexerTokens.NUMBER
import org.js.vento.plugin.lexer.LexerTokens.OBJECT
import org.js.vento.plugin.lexer.LexerTokens.PARENTHESIS
import org.js.vento.plugin.lexer.LexerTokens.PIPE
import org.js.vento.plugin.lexer.LexerTokens.SEMICOLON
import org.js.vento.plugin.lexer.LexerTokens.SET_CLOSE_KEY
import org.js.vento.plugin.lexer.LexerTokens.SET_KEY
import org.js.vento.plugin.lexer.LexerTokens.SYMBOL
import org.js.vento.plugin.lexer.LexerTokens.TEXT
import org.js.vento.plugin.lexer.LexerTokens.TRIM_COMMENT_END
import org.js.vento.plugin.lexer.LexerTokens.TRIM_COMMENT_START
import org.js.vento.plugin.lexer.LexerTokens.UNKNOWN
import org.js.vento.plugin.lexer.LexerTokens.VARIABLE_ELEMENT
import org.js.vento.plugin.lexer.LexerTokens.VBLOCK_CLOSE
import org.js.vento.plugin.lexer.LexerTokens.VBLOCK_OPEN
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
                BOOLEAN -> KEYWORDS
                BRACE -> BRACES
                BRACKET -> BRACKETS
                COLON -> OPERATIONS
                COMMA -> COMMAS
                COMMENT_CONTENT -> COMMENTED_CONTENT
                COMMENT_END -> CBLOCK
                COMMENT_START -> CBLOCK
                DOT -> DOTS
                EQUAL -> OPERATIONS
                EXPORT_CLOSE_KEY -> VENTO_KEYWORDS
                EXPORT_EQ -> KEYWORDS
                EXPORT_KEY -> VENTO_KEYWORDS
                EXPORT_VALUE -> VALUES
                EXPORT_VAR -> VALUES
                FILE -> STRING
                FOR_CLOSE_KEY -> VENTO_KEYWORDS
                FOR_COLLECTION -> VALUES
                FOR_KEY -> VENTO_KEYWORDS
                FOR_OF -> VENTO_KEYWORDS
                FOR_VALUE -> VALUES
                FUNCTION_ARGS -> ARGS
                FUNCTION_KEY -> VENTO_KEYWORDS
                IMPORT_FILE -> STRING
                IMPORT_FROM -> KEYWORDS
                IMPORT_KEY -> VENTO_KEYWORDS
                IMPORT_VALUES -> VALUES
                INCLUDE_KEY -> VENTO_KEYWORDS
                JAVASCRIPT_END -> JSBLOCK
                JAVASCRIPT_START -> JSBLOCK
                LAYOUT_CLOSE_KEY -> VENTO_KEYWORDS
                LAYOUT_KEY -> VENTO_KEYWORDS
                LAYOUT_SLOT_CLOSE_KEY -> KEYWORDS
                LAYOUT_SLOT_KEY -> KEYWORDS
                NUMBER -> NUMBERS
                OBJECT -> VALUES
                PARENTHESIS -> KEYWORDS
                PIPE -> VENTO_KEYWORDS
                SEMICOLON -> KEYWORDS
                SET_CLOSE_KEY -> VENTO_KEYWORDS
                SET_KEY -> VENTO_KEYWORDS
                STRING_TOKEN -> STRING
                SYMBOL -> SYMBOLS
                TEXT -> PLAIN_TEXT
                TRIM_COMMENT_END -> CBLOCK
                TRIM_COMMENT_START -> CBLOCK
                VARIABLE_ELEMENT -> SYMBOLS
                VBLOCK_CLOSE -> VBLOCK
                VBLOCK_OPEN -> VBLOCK
                UNKNOWN -> UNKNOWN_CONTENT
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

        val JSBLOCK = createTextAttributesKey("JS_BLOCK", TextAttributes(ventoPink, ventoGray, null, null, 0))
        val VBLOCK = createTextAttributesKey("VENTO_BLOCK", TextAttributes(ventoSky, ventoGray, null, null, 0))
        val VENTO_KEYWORDS = createTextAttributesKey("VENTO_VENTO_KEYWORDS", TextAttributes(ventoLightRed, ventoGray, null, null, 0))
        val CBLOCK = createTextAttributesKey("VENTO_COMMENTED", DOC_COMMENT)
        val COMMENTED_CONTENT = createTextAttributesKey("VENTO_COMMENT", DOC_COMMENT_MARKUP)

        val ARGS = createTextAttributesKey("VENTO_ARGS", DefaultLanguageHighlighterColors.PARAMETER)
        val BRACES = createTextAttributesKey("VENTO_DOTS", DefaultLanguageHighlighterColors.BRACES)
        val BRACKETS = createTextAttributesKey("VENTO_DOTS", DefaultLanguageHighlighterColors.BRACKETS)
        val COMMAS = createTextAttributesKey("VENTO_COMMAS", DefaultLanguageHighlighterColors.COMMA)
        val DOTS = createTextAttributesKey("VENTO_DOTS", DefaultLanguageHighlighterColors.DOT)

        val KEYWORDS = createTextAttributesKey("VENTO_KEYWORDS", KEYWORD)
        val NUMBERS = createTextAttributesKey("VENTO_NUMBER", DefaultLanguageHighlighterColors.NUMBER)
        val OPERATIONS = createTextAttributesKey("VENTO_OPERANDS", DefaultLanguageHighlighterColors.OPERATION_SIGN)
        val PLAIN_TEXT = createTextAttributesKey("VENTO_TEXT", STATIC_FIELD)
        val STRING = createTextAttributesKey("VENTO_STRING", DefaultLanguageHighlighterColors.STRING)
        val VALUES = createTextAttributesKey("VENTO_EXPRESSION", LOCAL_VARIABLE)
        val SYMBOLS = createTextAttributesKey("VENTO_VARIABLE_ELEMENT", LOCAL_VARIABLE)
        val UNKNOWN_CONTENT = createTextAttributesKey("VENTO_UNKNOWN_CONTENT", BAD_CHARACTER)

        val EMPTY_KEYS: Array<TextAttributesKey?> = arrayOfNulls<TextAttributesKey>(0)
    }
}
