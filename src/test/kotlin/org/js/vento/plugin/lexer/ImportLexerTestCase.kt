/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.lexer

@Suppress("ktlint:standard:blank-line-before-declaration")
class ImportLexerTestCase(name: String) : BaseLexerTestCase(name) {
    fun `test simple import`() =
        lexAndTest(
            "{{ import { foo } from \"./afile.vto\" }}",
            arrayOf("{{", "import", "{", "foo", "}", "from", "\"./afile.vto\"", "}}"),
        )

    fun `test bare import`() =
        lexAndTest(
            "{{ import foo from \"./afile.vto\" }}",
            arrayOf("{{", "import", "foo", "from", "\"./afile.vto\"", "}}"),
        )
}
