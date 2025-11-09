/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.lexer

class PipeTestCase(name: String) : BaseLexerTestCase(name) {
    fun `test escape pipe with string`() =
        lexAndTest(
            "{{ \"<h1>Hello, world!</h1>\" |> escape }}",
            arrayOf(
                "{{",
                "\"",
                "<h1>Hello, world!</h1>",
                "\"",
                "|>",
                "escape",
                "}}",
            ),
        )

    fun `test safe pipe with method call`() =
        lexAndTest(
            "{{ myTrustedSource.getHtml() |> safe }}",
            arrayOf(
                "{{",
                "myTrustedSource",
                ".",
                "getHtml",
                "(",
                ")",
                "|>",
                "safe",
                "}}",
            ),
        )

    fun `test custom filter with arguments`() =
        lexAndTest(
            "{{ \"<h1>Hello, world!</h1>\" |> filter_name(arg1, arg2) }}",
            arrayOf(
                "{{",
                "\"",
                "<h1>Hello, world!</h1>",
                "\"",
                "|>",
                "filter_name",
                "(",
                "arg1",
                ",",
                "arg2",
                ")",
                "}}",
            ),
        )

    fun `test json stringify of object`() =
        lexAndTest(
            "{{ { name: \"Óscar\", surname: \"Otero\" } |> JSON.stringify }}",
            arrayOf(
                "{{",
                "{",
                "name",
                ":",
                "\"",
                "Óscar",
                "\"",
                ",",
                "surname",
                ":",
                "\"",
                "Otero",
                "\"",
                "}",
                "|>",
                "JSON",
                ".",
                "stringify",
                "}}",
            ),
        )

    fun `test json stringify of object with spacing`() =
        lexAndTest(
            "{{ { name: \"Óscar\", surname: \"Otero\" } |> JSON.stringify(null, 2) }}",
            arrayOf(
                "{{",
                "{",
                "name",
                ":",
                "\"",
                "Óscar",
                "\"",
                ",",
                "surname",
                ":",
                "\"",
                "Otero",
                "\"",
                "}",
                "|>",
                "JSON",
                ".",
                "stringify",
                "(",
                "null",
                ",",
                "2",
                ")",
                "}}",
            ),
        )

    fun `test toUpperCase pipe`() =
        lexAndTest(
            "{{ \"Hello, world!\" |> toUpperCase }}",
            arrayOf(
                "{{",
                "\"",
                "Hello, world!",
                "\"",
                "|>",
                "toUpperCase",
                "}}",
            ),
        )

    fun `test await fetch json stringify pipeline`() =
        lexAndTest(
            (
                """
                {{
                   "https://example.com/data.json"
                      |> await fetch
                      |> await json
                      |> JSON.stringify
                }}
                """
            ).trimIndent(),
            arrayOf(
                "{{",
                "\"",
                "https://example.com/data.json",
                "\"",
                "|>",
                "await",
                "fetch",
                "|>",
                "await",
                "json",
                "|>",
                "JSON",
                ".",
                "stringify",
                "}}",
            ),
        )
}
