/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.lang.PsiBuilder
import org.js.vento.plugin.lexer.LexerTokens
import org.js.vento.plugin.lexer.LexerTokens.FRONTMATTER_CLOSE
import org.js.vento.plugin.lexer.LexerTokens.FRONTMATTER_OPEN

fun parseFrontmatter(builder: PsiBuilder) {
    val m = builder.mark()
    expect(builder, FRONTMATTER_OPEN, "Expected frontmatter open")
    while (!builder.eof() &&
        (
            builder.tokenType == LexerTokens.FRONTMATTER_KEY ||
                builder.tokenType == LexerTokens.FRONTMATTER_FLAG ||
                builder.tokenType == LexerTokens.FRONTMATTER_VALUE ||
                builder.tokenType == LexerTokens.COMMENT_CONTENT ||
                builder.tokenType == LexerTokens.STRING ||
                builder.tokenType == LexerTokens.COLON
        )
    ) {
        builder.advanceLexer()
    }
    expect(builder, FRONTMATTER_CLOSE, "Expected frontmatter close")
    m.done(ParserElements.FRONTMATTER_BLOCK)
}
