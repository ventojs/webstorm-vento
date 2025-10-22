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

    fun `test incorrect import`() {
        lexAndTest(
            "{{ import message = 10 }}",
            arrayOf("\n", "{{", "export", "1", "2", "3", "4", "5", "}}", "\n"),
        )
    }
}
