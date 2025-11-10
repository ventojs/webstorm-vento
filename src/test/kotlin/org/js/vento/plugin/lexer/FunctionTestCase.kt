/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.lexer

class FunctionTestCase(name: String) : BaseLexerTestCase(name) {
    fun `test simple function`() =
        lexAndTest(
            "{{ function hello }}\nHello, world!\n{{ /function }}",
            arrayOf("{{", "function", "hello", "}}", "\nHello, world!\n", "{{", "/function", "}}"),
        )

    fun `test function with args`() =
        lexAndTest(
            "{{ function hello(name = \"world\", foo) }}",
            arrayOf("{{", "function", "hello", "(", "name", "=", "\"", "world", "\"", ",", "foo", ")", "}}"),
        )

    fun `test async function`() =
        lexAndTest(
            "{{ async function hello }}",
            arrayOf("{{", "async", "function", "hello", "}}"),
        )
}
