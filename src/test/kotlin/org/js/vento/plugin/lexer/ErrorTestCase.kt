/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.lexer

class ErrorTestCase(name: String) : BaseLexerTestCase(name) {
    fun `test no keyword`() {
        lexAndTest(
            "\n{{ notkeyword something }}\n",
            arrayOf(
                "\n",
                "{{",
                "n",
                "o",
                "t",
                "k",
                "e",
                "y",
                "w",
                "o",
                "r",
                "d",
                " ",
                "s",
                "o",
                "m",
                "e",
                "t",
                "h",
                "i",
                "n",
                "g",
                " ",
                "}}",
                "\n",
            ),
        )
    }

    fun `test keyword plus 1 nonesense`() {
        lexAndTest(
            "\n{{ export 100 }}\n",
            arrayOf("\n", "{{", "export", "100", "}}", "\n"),
        )
    }

    fun `test keyword plus many nonesense`() {
        lexAndTest(
            "\n{{ export 1 2 3 4 5 }}\n",
            arrayOf("\n", "{{", "export", "1", "2", "3", "4", "5", "}}", "\n"),
        )
    }

    fun `test incorrect import`() {
        lexAndTest(
            "{{ import message = 10 }}",
            arrayOf(
                "{{",
                "import",
                "message",
                "=",
                "1",
                "0",
                " ",
                "}}",
            ),
        )
    }

    fun `test unclosed block`() {
        lexAndTest(
            "{{ /set {{ foo }}",
            arrayOf(
                "{{",
                "/set",
                "{{",
                "f",
                "o",
                "o",
                " ",
                "}}",
            ),
        )
    }

    fun `test unclosed block2`() {
        lexAndTest(
            "{{ foo {{ bar }}",
            arrayOf(
                "{{",
                "f",
                "o",
                "o",
                " ",
                "{",
                "{",
                " ",
                "b",
                "a",
                "r",
                " ",
                "}}",
            ),
        )
    }

    fun `test keyword typo`() {
        lexAndTest(
            "{{ se foo = \"bar\" }}",
            arrayOf(
                "{{",
                "s",
                "e",
                " ",
                "f",
                "o",
                "o",
                " ",
                "=",
                " ",
                "\"",
                "b",
                "a",
                "r",
                "\"",
                " ",
                "}}",
            ),
        )
    }

    fun `test recover`() {
        lexAndTest(
            """
{{ for number of [1,2,3] }}
{{ "hello " + number }}
{{ / }}
{{ "hi!" }}
            """.trimIndent(),
            arrayOf(
                "{{",
                "for",
                "number",
                "of",
                "[",
                "1",
                ",",
                "2",
                ",",
                "3",
                "]",
                "}}",
                "\n",
                "{{",
                "\"",
                "h",
                "e",
                "l",
                "l",
                "o",
                " ",
                "\"",
                " ",
                "+",
                " ",
                "n",
                "u",
                "m",
                "b",
                "e",
                "r",
                " ",
                "}}",
                "\n",
                "{{",
                "/",
                " ",
                "}}",
                "\n",
                "{{",
                "\"",
                "h",
                "i",
                "!",
                "\"",
                " ",
                "}}",
            ),
        )
    }

    fun `test open string`() {
        lexAndTest(
            "{{ export foo = \" }}",
            arrayOf(
                "{{",
                "export",
                "foo",
                "=",
                "\"",
                " }}",
            ),
        )
    }

    fun `test nothing`() {
        lexAndTest(
            "",
            arrayOf(
                "",
            ),
        )
    }

    fun `test broken regex that hangs IDE`() {
        lexAndTest(
            "{{ set myVar = /[Hh].*/.*[}]/ }}",
            arrayOf(
                "{{",
                "set",
                "myVar",
                "=",
                "/",
                "[Hh].*",
                "/",
                ".",
                "*",
                "[",
                "}",
                "]",
                "/",
                "}}",
            ),
        )
    }

    fun `test failing include`() {
        lexAndTest(
            """{{ include "/sub/my-file.vto" { salute: "Very" + "" + "Welcome" } }}""",
            arrayOf(
                "{{",
                "include",
                "\"",
                "/sub/my-file.vto",
                "\"",
                "{",
                "salute",
                ":",
                "\"",
                "Very",
                "\"",
                "+",
                "\"",
                "\"",
                "+",
                "\"",
                "Welcome",
                "\"",
                "}",
                "}}",
            ),
        )
    }
}
