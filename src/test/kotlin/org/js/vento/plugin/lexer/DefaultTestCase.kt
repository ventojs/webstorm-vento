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
class DefaultTestCase(name: String) : BaseLexerTestCase(name) {
    fun `test simple set`() =
        lexAndTest(
            "{{ default greeting = \"Hello Worlds\"}}",
            arrayOf(
                "{{",
                "default",
                "greeting",
                "=",
                "\"",
                "Hello Worlds",
                "\"",
                "}}",
            ),
        )

    fun `test close default`() = lexAndTest("{{ /default }}", arrayOf("{{", "/default", "}}"))

    fun `test default function`() =
        lexAndTest(
            "{{ default foo = function(){return 'Hi'} }}",
            arrayOf(
                "{{",
                "default",
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

    fun `test default lambda with body`() =
        lexAndTest(
            "{{ default x = () => {return true} }}",
            arrayOf(
                "{{",
                "default",
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

    fun `test default lambda`() =
        lexAndTest(
            "{{ default x = () => true }}",
            arrayOf(
                "{{",
                "default",
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
