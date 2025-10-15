/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.lexer

/**
 * Tests for variable expression lexing.
 *
 * The tests are grouped and ordered from simple to complex to make intent clear and maintenance easier.
 * Each test includes a short description of the scenario and the expected tokenization.
 */
@Suppress("ktlint:standard:blank-line-before-declaration")
class LayoutTestCase(name: String) : BaseLexerTestCase(name) {
    fun `test simple layout`() {
        lexAndTest(
            """
            {{ layout "myfile.vto" }}
            <h1>Hello Worlds</h1>
            {{ /layout }}
            """.trimIndent(),
            arrayOf("{{", "layout", "\"myfile.vto\"", "}}", "\n<h1>Hello Worlds</h1>\n", "{{", "/layout", "}}"),
        )
    }

    fun `test broken layout`() {
        lexAndTest(
            """
            {{ layout "myfile.vto }}
            <h1>Hello Worlds</h1>
            {{ /layout }}
            """.trimIndent(),
            arrayOf(
                "{{",
                "layout",
                "\"",
                "m",
                "y",
                "f",
                "i",
                "l",
                "e",
                ".",
                "v",
                "t",
                "o",
                "}}",
                "\n<h1>Hello Worlds</h1>\n",
                "{{",
                "/layout",
                "}}",
            ),
        )
    }

    fun `test broken layout close`() {
        lexAndTest(
            """
            {{ layout "myfile.vto" }}
            <h1>Hello Worlds</h1>
            {{ /layou }}
            """.trimIndent(),
            arrayOf(
                "{{",
                "layout",
                "\"myfile.vto\"",
                "}}",
                "\n<h1>Hello Worlds</h1>\n",
                "{{",
                "/",
                "layou }}",
            ),
        )
    }
}
