/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

@file:Suppress("ktlint:standard:no-wildcard-imports")

package org.js.vento.plugin.highlighting

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.psi.tree.IElementType
import org.js.vento.plugin.lexer.LexerTokens.FRONTMATTER_CLOSE
import org.js.vento.plugin.lexer.LexerTokens.FRONTMATTER_FLAG
import org.js.vento.plugin.lexer.LexerTokens.FRONTMATTER_KEY
import org.js.vento.plugin.lexer.LexerTokens.FRONTMATTER_OPEN
import org.js.vento.plugin.lexer.LexerTokens.FRONTMATTER_VALUE

/**
 * Syntax highlighter that disables frontmatter highlighting.
 * This extends the standard SyntaxHighlighter but returns null for frontmatter tokens.
 */
class SyntaxHighlighterWithoutFrontmatter : SyntaxHighlighter() {
    override fun getTokenHighlights(type: IElementType?): Array<out TextAttributesKey?> {
        // Disable highlighting for frontmatter tokens
        return when (type) {
            FRONTMATTER_OPEN,
            FRONTMATTER_CLOSE,
            FRONTMATTER_KEY,
            FRONTMATTER_FLAG,
            FRONTMATTER_VALUE,
            -> pack(null)
            else -> super.getTokenHighlights(type)
        }
    }
}
