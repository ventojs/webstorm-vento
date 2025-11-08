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
class IncludeTestCase(name: String) : BaseLexerTestCase(name) {
    fun `test simple include`() {
        lexAndTest(
            """{{ include "myfile.vto" }}""".trimIndent(),
            arrayOf(
                "{{",
                "include",
                "\"",
                "myfile.vto",
                "\"",
                "}}",
            ),
        )
    }

    fun `test include with pipe`() {
        lexAndTest(
            """{{ include "myfile.vto" |> toUpperCase }}""".trimIndent(),
            arrayOf(
                "{{",
                "include",
                "\"",
                "myfile.vto",
                "\"",
                "|>",
                "toUpperCase",
                "}}",
            ),
        )
    }

    fun `test include with data`() {
        lexAndTest(
            """{{ include "myfile.vto" {salutation: "Good"+" bye"} }}""".trimIndent(),
            arrayOf(
                "{{",
                "include",
                "\"",
                "myfile.vto",
                "\"",
                "{",
                "salutation",
                ":",
                "\"",
                "Good",
                "\"",
                "+",
                "\"",
                " bye",
                "\"",
                "}",
                "}}",
            ),
        )
    }

    fun `test include with data then pipe`() {
        lexAndTest(
            """{{ include "myfile.vto" {salutation: "Good bye"} |> toUpperCase }}""".trimIndent(),
            arrayOf(
                "{{",
                "include",
                "\"",
                "myfile.vto",
                "\"",
                "{",
                "salutation",
                ":",
                "\"",
                "Good bye",
                "\"",
                "}",
                "|>",
                "toUpperCase",
                "}}",
            ),
        )
    }

    fun `test broken include with pipe then data`() {
        lexAndTest(
            """{{ include "myfile.vto" |> toUpperCase {salutation: "Good bye"} }}""".trimIndent(),
            arrayOf(
                "{{",
                "include",
                "\"",
                "myfile.vto",
                "\"",
                "|>",
                "toUpperCase",
                "{",
                "salutation",
                ":",
                "\"",
                "Good bye",
                "\"",
                "}",
                "}}",
            ),
        )
    }

    fun `test literal string with interpolation`() {
        lexAndTest(
            $$"""{{ include `${'$'}{file}.vto` { name: name } }}""",
            arrayOf(
                "{{",
                "include",
                "`",
                "$",
                "{'",
                "$",
                "'}{file}.vto",
                "`",
                "{",
                "name",
                ":",
                "name",
                "}",
                "}}",
            ),
        )
    }

    fun `test function`() {
        lexAndTest(
            $$"""{{ include resolve({ path: "/my-file.vto" }) { name } }}""",
            arrayOf(
                "{{",
                "include",
                "resolve",
                "(",
                "{",
                "path",
                ":",
                "\"",
                "/my-file.vto",
                "\"",
                "}",
                ")",
                "{",
                "name",
                "}",
                "}}",
            ),
        )
    }
}
