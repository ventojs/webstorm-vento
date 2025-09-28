/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.lexer

/**
 *
 */
@Suppress("ktlint:standard:blank-line-before-declaration")
class ForLexerTestCase(name: String) : BaseLexerTestCase(name) {
    fun `test for`() = lexAndTest("{{ for number of [1, 2, 3] }}", arrayOf("{{", " for ", "number", " of ", "[1, 2, 3]", " ", "}}"))
    fun `test close for`() = lexAndTest("{{ /for }}", arrayOf("{{", " /for ", "}}"))
}
