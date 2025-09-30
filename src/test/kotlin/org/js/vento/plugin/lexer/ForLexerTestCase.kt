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
    fun `test for array`() = lexAndTest("{{ for number of [1, 2, 3] }}", arrayOf("{{", "for", "number", "of", " [1, 2, 3] ", "}}"))
    fun `test for object`() =
        lexAndTest("{{ for key,value of {a:1,b:2} }}", arrayOf("{{", "for", "key,value", "of", "{", "a:1,b:2", "}", "}}"))

    fun `test close for`() = lexAndTest("{{ /for }}", arrayOf("{{", "/for", "}}"))
    fun `test complete for`() =
        lexAndTest(
            """
            |{{ for item of items }}
            |{{ /for }}
            """.trimMargin(),
            arrayOf("{{", "for", "item", "of", " items ", "}}", "\n", "{{", "/for", "}}"),
        )

    fun `test for of object`() =
        lexAndTest(
            """
            |{{ for item of {a:1,b:{c:2}} }}
            |{{ /for }}
            """.trimMargin(),
            arrayOf("{{", "for", "item", "of", "{", "a:1,b:", "{", "c:2", "}", "}", "}}", "\n", "{{", "/for", "}}"),
        )

    fun `test for of broken object`() =
        lexAndTest(
            """
            |{{ for item of {a:1,b:{c:2} }}
            |{{ /for }}
            """.trimMargin(),
            arrayOf("{{", "for", "item", "of", "{", "a:1,b:", "{", "c:2", "}", "}", "}", "\n", "{{", "/for", "}}"),
        )

    fun `test for of with missing value `() =
        lexAndTest(
            "{{ for of values }}",
            arrayOf("{{", "for", "of", " values ", "}}"),
        )
}
