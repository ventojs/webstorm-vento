/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.lexer

class IfTestCase(name: String) : BaseLexerTestCase(name) {
    fun `test if`() = lexAndTest("{{ if true }}", arrayOf("{{", "if", "true", "}}"))

    fun `test else`() = lexAndTest("{{ else }}", arrayOf("{{", "else", "}}"))

    fun `test else if`() = lexAndTest("{{ else if false }}", arrayOf("{{", "else if", "false", "}}"))

    fun `test close if`() = lexAndTest("{{ /if }}", arrayOf("{{", "/if", "}}"))
}
