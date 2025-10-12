/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.lexer

/**
 * Test cases for the Variable Pipe Lexer functionality.
 *
 * This class contains test cases that verify the lexical analysis of variable pipe expressions
 * in template syntax. It focuses on testing the correct tokenization of expressions containing
 * the pipeline operator (|>) and variable transformations.
 */
@Suppress("ktlint:standard:blank-line-before-declaration")
class VariablePipeTestCase(name: String) : BaseLexerTestCase(name) {
    /**
     *  Verifies that the lexer can recognize a simple variable pipe expression.
     *  Input: {{ "Hello, world!" |> toUpperCase }}
     *  Expected: {{, "Hello, world!", |>, toUpperCase, }}
     */
    fun `test pipe`() =
        lexAndTest("{{ \"Hello, world!\" |> toUpperCase  }}", arrayOf("{{", "\"", "Hello, world!", "\"", "|>", "toUpperCase", "}}"))

    /**
     * Verifies pipeline operator usage.
     * Input: {{ variable |> toUpperCase }}
     * Expected: {{, variable, |>, toUpperCase, }}
     */
    fun `test with pipes`() = lexAndTest("{{ variable |> toUpperCase }}", arrayOf("{{", "variable", "|>", "toUpperCase", "}}"))
}
