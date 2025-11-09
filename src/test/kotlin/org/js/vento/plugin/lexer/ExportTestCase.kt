/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.lexer

@Suppress("ktlint:standard:blank-line-before-declaration")
class ExportTestCase(name: String) : BaseLexerTestCase(name) {
    fun `test simple export string`() =
        lexAndTest(
            "{{ export message = \"Hello, world!\" }}",
            arrayOf(
                "{{",
                "export",
                "message",
                "=",
                "\"",
                "Hello, world!",
                "\"",
                "}}",
            ),
        )

    fun `test simple close export`() = lexAndTest("{{ /export }}", arrayOf("{{", "/export", "}}"))

    fun `test simple export number`() =
        lexAndTest(
            "{{ export score = 10 }}",
            arrayOf("{{", "export", "score", "=", "10", "}}"),
        )

    fun `test function export`() =
        lexAndTest(
            "{{ export function message (fname,lname) }}foo{{/export}}",
            arrayOf(
                "{{",
                "export",
                "function",
                "message",
                "(",
                "fname",
                ",",
                "lname",
                ")",
                "}}",
                "foo",
                "{{",
                "/export",
                "}}",
            ),
        )

    fun `test export with pipe`() =
        lexAndTest(
            "{{ export message = \"Hello, world!\" |> toUpperCase }}",
            arrayOf(
                "{{",
                "export",
                "message",
                "=",
                "\"",
                "Hello, world!",
                "\"",
                "|>",
                "toUpperCase",
                "}}",
            ),
        )

    fun `test export with pipe 2`() =
        lexAndTest(
            "{{ export message = \"Hello, world!\" |> filter((n) => n % 2 === 0).toString() }}",
            arrayOf(
                "{{",
                "export",
                "message",
                "=",
                "\"",
                "Hello, world!",
                "\"",
                "|>",
                "filter",
                "(",
                "(",
                "n",
                ")",
                "=>",
                "n",
                "%",
                "2",
                "=",
                "=",
                "=",
                "0",
                ")",
                ".",
                "toString",
                "(",
                ")",
                "}}",
            ),
        )

    fun `test export with pipe 3`() =
        lexAndTest(
            "{{ export message = \"Hello, world!\" |> !/[/\"\\]}]/.test('foo/bar') }}",
            arrayOf(
                "{{",
                "export",
                "message",
                "=",
                "\"",
                "Hello, world!",
                "\"",
                "|>",
                "!",
                "/",
                "[",
                "/\"\\",
                "]",
                "}]",
                "/",
                ".",
                "test",
                "(",
                "'",
                "foo/bar",
                "'",
                ")",
                "}}",
            ),
        )

    fun `test export with pipe 4`() =
        lexAndTest(
            "{{ export message = \"Hello, world!\" |> JSON.stringify }}",
            arrayOf(
                "{{",
                "export",
                "message",
                "=",
                "\"",
                "Hello, world!",
                "\"",
                "|>",
                "JSON",
                ".",
                "stringify",
                "}}",
            ),
        )

    fun `test export with expression`() =
        lexAndTest(
            "{{ export foo = JSON.parse(\"{}\") }}",
            arrayOf(
                "{{",
                "export",
                "foo",
                "=",
                "JSON",
                ".",
                "parse",
                "(",
                "\"",
                "{}",
                "\"",
                ")",
                "}}",
            ),
        )
}
