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
        lexAndTest("{{ set greeting = \"Hello Worlds\"}}", arrayOf("{{", "set", "greeting", "=", "\"Hello Worlds\"", "}}"))
}
