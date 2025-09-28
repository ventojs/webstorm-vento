/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.lexer

/**
 *
 */
@Suppress("ktlint:standard:blank-line-before-declaration")
class VariablePipeLexerTestCase(name: String) : BaseLexerTestCase(name) {
    fun `test undefined`() =
        lexAndTest("{{ \"Hello, world!\" |> toUpperCase  }}", arrayOf("{{", "\"", "Hello, world!", "\"", "|>", "toUpperCase", "}}"))
}
