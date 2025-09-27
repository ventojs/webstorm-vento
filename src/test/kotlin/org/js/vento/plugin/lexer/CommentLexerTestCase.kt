/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.lexer

class CommentLexerTestCase(name: String) : BaseLexerTestCase(name) {
    fun `test lexing comment`() {
        lexAndTest(
            "\n{{# \"Hello world\" #}}\n",
            arrayOf("\n", "{{#", " \"Hello world\" ", "#}}", "\n"),
        )
    }

    fun `test comment with vento content `() {
        lexAndTest(
            "<h1> {{# {{ title }} {{#}}",
            arrayOf("<h1> ", "{{#", " {{ title }} {{", "#}}"),
        )
    }

    fun `test comment with left trim`() {
        lexAndTest(
            "<h1> {{#- #}} </h1>",
            arrayOf("<h1> ", "{{#-", " ", "#}}", " </h1>"),
        )
    }

    fun `test comment with trim`() {
        lexAndTest(
            "<h1> {{#- -#}} </h1>",
            arrayOf("<h1> ", "{{#-", " ", "-#}}", " </h1>"),
        )
    }

    fun `test comment with no content`() {
        lexAndTest(
            "<h1> {{#--#}}   </h1>",
            arrayOf("<h1> ", "{{#-", "-#}}", "   </h1>"),
        )
    }
}
