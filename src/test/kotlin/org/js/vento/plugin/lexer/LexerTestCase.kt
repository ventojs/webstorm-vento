/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.lexer

class LexerTestCase(name: String) : BaseLexerTestCase(name) {
    fun `test lexing comment`() {
        lexAndTest(
            " {{# console.log('Hello World') #}} ",
            arrayOf(" ", "{{#", " console.log('Hello World') ", "#}}", " "),
        )
    }

    fun `test lexing trimmed comment `() {
        lexAndTest(
            " {{#- This is a comment -#}} ",
            arrayOf(" ", "{{#-", " This is a comment ", "-#}}", " "),
        )
    }

    fun `test lexing javascript`() {
        lexAndTest(
            " {{> if(true){console.log('Hello World')} }} ",
            arrayOf(
                " ",
                "{{>",
                " if(true)",
                "{",
                "console.log('Hello World')",
                "}",
                "}}",
                " ",
            ),
        )
    }

    fun `test lexing multiline javascript`() {
        lexAndTest(
            """
            {{> if(true){
                   console.log('Hello World')
                } }}
            """.trimIndent(),
            arrayOf(
                "{{>",
                " if(true)",
                "{",
                """
       console.log('Hello World')
    """,
                "}",
                "}}",
            ),
        )
    }

    fun `test lexing variables`() {
        lexAndTest("{{ variable }}", arrayOf("{{", "variable", "}}"))
    }

    fun `test lexing html `() {
        lexAndTest("hello <span>world</span>", arrayOf("hello <span>world</span>"))
    }
}
