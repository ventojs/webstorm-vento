/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.lexer

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

    fun `test layout with data`() {
        lexAndTest(
            """
            {{ layout "myfile.vto" {name:"\"World\""} }}
            <h1>Hello {{ name }}!</h1>
            {{ /layout }}
            """.trimIndent(),
            arrayOf(
                "{{",
                "layout",
                "\"myfile.vto\"",
                "{",
                "name",
                ":",
                "\"",
                "\\\"World\\\"",
                "\"",
                "}",
                "}}",
                "\n<h1>Hello ",
                "{{",
                "name",
                "}}",
                "!</h1>\n",
                "{{",
                "/layout",
                "}}",
            ),
        )
    }

    fun `test layout with slot`() {
        lexAndTest(
            """
            {{ layout "section.vto" }}
            {{- slot header |> toUpperCase }}
            <h1>Section title</h1>
            {{ /slot -}}
            <p>Content of the section</p>
            {{ /layout }}
            """.trimIndent(),
            arrayOf(
                "{{",
                "layout",
                "\"section.vto\"",
                "}}",
                "\n",
                "{{-",
                "slot",
                "header",
                "|>",
                "toUpperCase",
                "}}",
                "\n<h1>Section title</h1>\n",
                "{{",
                "/slot",
                "-}}",
                "\n<p>Content of the section</p>\n",
                "{{",
                "/layout",
                "}}",
            ),
        )
    }

    fun `test layout with pipe`() {
        lexAndTest(
            """
            {{ layout "myfile.vto" |> toUpperCase}}
            hello world!
            {{ /layout }}
            """.trimIndent(),
            arrayOf(
                "{{",
                "layout",
                "\"myfile.vto\"",
                "|>",
                "toUpperCase",
                "}}",
                "\nhello world!\n",
                "{{",
                "/layout",
                "}}",
            ),
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
                "\"myfile.vto }}\n<h1>Hello Worlds</h1>\n{{ /layout ",
                "}}",
            ),
            false,
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

    fun `test layout with missing space`() {
        lexAndTest(
            """
            {{ layout "myfile.vto"}}
            """.trimIndent(),
            arrayOf(
                "{{",
                "layout",
                "\"myfile.vto\"",
                "}}",
            ),
        )
    }

    fun `test layout with data reference`() {
        lexAndTest(
            """
            {{ layout "myfile.vto" dataSymbol }}
            """.trimIndent(),
            arrayOf(
                "{{",
                "layout",
                "\"myfile.vto\"",
                "dataSymbol",
                "}}",
            ),
        )
    }
}
