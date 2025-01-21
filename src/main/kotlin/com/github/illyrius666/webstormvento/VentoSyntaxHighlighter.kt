package com.github.illyrius666.webstormvento

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType

class VentoSyntaxHighlighter : SyntaxHighlighterBase() {
    override fun getHighlightingLexer(): Lexer {
        return VentoLexerAdapter()
    }

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        return when (tokenType) {
            VentoTypes.KEYWORD -> arrayOf(KEYWORD)
            VentoTypes.STRING -> arrayOf(STRING)
            else -> EMPTY
        }
    }

    companion object {
        val KEYWORD =
            TextAttributesKey.createTextAttributesKey("VENTO_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)
        val STRING = TextAttributesKey.createTextAttributesKey("VENTO_STRING", DefaultLanguageHighlighterColors.STRING)
        val EMPTY = arrayOf<TextAttributesKey>()
    }
}