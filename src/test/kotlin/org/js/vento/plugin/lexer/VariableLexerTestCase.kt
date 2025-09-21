/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.lexer

class VariableLexerTestCase(name: String) : BaseLexerTestCase(name) {
    fun `test lexing variables`() {
        lexAndTest("{{ variable }}", arrayOf("{{", " variable ", "}}"))
    }

    fun `test lexing variables with pipes`() {
        lexAndTest("{{ variable || \"default\" }}", arrayOf("{{", " variable ", "||", " \"default\" ", "}}"))
    }
}
