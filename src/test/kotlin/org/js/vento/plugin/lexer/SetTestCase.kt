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
class SetTestCase(name: String) : BaseLexerTestCase(name) {
    fun `test simple set`() =
        lexAndTest(
            "{{ set greeting = \"Hello Worlds\"}}",
            arrayOf(
                "{{",
                "set",
                "greeting",
                "=",
                "\"",
                "Hello Worlds",
                "\"",
                "}}",
            ),
        )

    fun `test close set`() = lexAndTest("{{ /set }}", arrayOf("{{", "/set", "}}"))

    fun `test set function`() =
        lexAndTest(
            "{{ set foo = function(){return 'Hi'} }}",
            arrayOf(
                "{{",
                "set",
                "foo",
                "=",
                "function",
                "(",
                ")",
                "{",
                "r",
                "e",
                "t",
                "u",
                "r",
                "n",
                " ",
                "'",
                "H",
                "i",
                "'",
                "}",
                "}}",
            ),
        )

    fun `test set lambda with body`() =
        lexAndTest(
            "{{ set x = () => {return true} }}",
            arrayOf(
                "{{",
                "set",
                "x",
                "=",
                "(",
                ")",
                "=>",
                "{",
                "r",
                "e",
                "t",
                "u",
                "r",
                "n",
                " ",
                "t",
                "r",
                "u",
                "e",
                "}",
                "}}",
            ),
        )

    fun `test set lambda`() =
        lexAndTest(
            "{{ set x = () => true }}",
            arrayOf(
                "{{",
                "set",
                "x",
                "=",
                "(",
                ")",
                "=>",
                "true",
                "}}",
            ),
        )
}
