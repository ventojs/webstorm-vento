/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.lang.PsiBuilder
import org.js.vento.plugin.lexer.LexerTokens.COLON
import org.js.vento.plugin.lexer.LexerTokens.COMMENT_CONTENT
import org.js.vento.plugin.lexer.LexerTokens.FRONTMATTER_CLOSE
import org.js.vento.plugin.lexer.LexerTokens.FRONTMATTER_FLAG
import org.js.vento.plugin.lexer.LexerTokens.FRONTMATTER_KEY
import org.js.vento.plugin.lexer.LexerTokens.FRONTMATTER_OPEN
import org.js.vento.plugin.lexer.LexerTokens.FRONTMATTER_VALUE
import org.js.vento.plugin.lexer.LexerTokens.STRING

fun parseFrontmatter(builder: PsiBuilder) {
    val m = builder.mark()
    if (builder.rawTokenIndex() != 0) builder.error("Frontmatter must be the first line of the file")

    expect(builder, FRONTMATTER_OPEN, "Expected frontmatter open")
    while (!builder.eof() &&
        (
            builder.tokenType == FRONTMATTER_KEY ||
                builder.tokenType == COMMENT_CONTENT
        )
    ) {
        parseLine(builder)
    }
    expect(builder, FRONTMATTER_CLOSE, "Expected frontmatter close")
    m.done(ParserElements.FRONTMATTER_BLOCK)
}

fun parseLine(builder: PsiBuilder) {
    val m = builder.mark()
    if (optional(builder, FRONTMATTER_KEY, "Expected frontmatter key")) {
        expect(builder, COLON, "Expect :")
        when (builder.tokenType) {
            STRING -> parseString(builder)
            FRONTMATTER_VALUE -> builder.advanceLexer()
            FRONTMATTER_FLAG -> expect(builder, FRONTMATTER_FLAG, "Expect flag", true)
            else -> builder.error("Unexpected token")
        }
    } else {
        expect(builder, COMMENT_CONTENT, "Expect comment")
    }
    m.done(ParserElements.FRONTMATTER_LINE)
}
