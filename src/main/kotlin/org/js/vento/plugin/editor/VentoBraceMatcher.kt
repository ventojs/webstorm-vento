/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.editor

import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import org.js.vento.plugin.lexer.LexerTokens

/**
 * Provides brace matching for Vento template delimiters.
 * Highlights matching {{ and }} pairs when the cursor is positioned on either.
 */
class VentoBraceMatcher : PairedBraceMatcher {
    private val pairs =
        arrayOf(
            BracePair(LexerTokens.VBLOCK_OPEN, LexerTokens.VBLOCK_CLOSE, true),
            BracePair(LexerTokens.JSBLOCK_OPEN, LexerTokens.JSBLOCK_CLOSE, true),
        )

    override fun getPairs(): Array<BracePair> = pairs

    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?): Boolean {
        // Allow braces before any token type
        return true
    }

    override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int): Int {
        // Return the position where the code construct starts
        // For Vento, this is typically the opening brace itself
        return openingBraceOffset
    }
}
