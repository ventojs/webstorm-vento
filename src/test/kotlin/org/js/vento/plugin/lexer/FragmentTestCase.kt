/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.lexer

@Suppress("ktlint:standard:blank-line-before-declaration")
class FragmentTestCase(name: String) : BaseLexerTestCase(name) {
    fun `test simple fragment`() {
        lexAndTest(
            """
            {{ fragment name }}
            <h1>Hello {{name}}</h1>
            {{ /fragment }}
            """.trimIndent(),
            arrayOf(
                "{{",
                "fragment",
                "name",
                "}}",
                "\n<h1>Hello ",
                "{{",
                "name",
                "}}",
                "</h1>\n",
                "{{",
                "/fragment",
                "}}",
            ),
        )
    }
}
