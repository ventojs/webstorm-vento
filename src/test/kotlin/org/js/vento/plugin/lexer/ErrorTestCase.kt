/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.lexer

class ErrorTestCase(name: String) : BaseLexerTestCase(name) {
    fun `test no keyword`() {
        lexAndTest(
            "\n{{ notkeyword something }}\n",
            arrayOf("\n", "{{", "notkeyword", "something", "}}", "\n"),
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

    fun `test incorrect import`() = countVentoBlocks("{{ import message = 10 }}", 1)

    fun `test unclosed block`() = countVentoBlocks("{{ /set {{ foo }}", 1)

    fun `test unclosed block2`() = countVentoBlocks("{{ foo {{ bar }}", 1)

    fun `test keyword typo`() = countVentoBlocks("{{ se foo = \"bar\" }}", 1)

    fun `test doesn't recover`() =
        countVentoBlocks(
            """
{{ for number of [1,2,3] }}
{{ "hello " + number }}
{{ / }}
{{ "hi!" }}
            """.trimIndent(),
            2,
        )

    fun `test open string`() = countVentoBlocks("{{ export foo = \" }}", 0)

    fun `test nothing`() = lexAndTest("", arrayOf(""))

    fun `test broken regex with unescaped forward slash`() = countVentoBlocks("{{ set myVar = /[Hh].*/.*[}]/ }}", 0)

    fun `test failing include`() = countVentoBlocks("""{{ include "/sub/my-file.vto" { salute: "Very" + "" + "Welcome" } }}""", 1)

    fun `test failing block crashes next one`() {
        countVentoBlocks(
            """{{ unknown "section.vto" {department: "Marketing", active: true} }}
            |{{ include "/my-file.vto" {salute: "Good bye", size: 10 } |> toUpperCase }}
            """.trimMargin(),
            2,
        )
    }
}
