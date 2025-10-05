/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.lexer

@Suppress("ktlint:standard:blank-line-before-declaration")
class ExportLexerTestCase(name: String) : BaseLexerTestCase(name) {
    fun `test simple import`() =
        lexAndTest(
            "{{ export message = \"Hello, world!\" }}",
            arrayOf("{{", "export", "message", "=", "\"Hello, world!\"", "}}"),
        )

    fun `test function import`() =
        lexAndTest(
            "{{ export function message (name) }}foo{{/export}}",
            arrayOf("{{", "export", "function", "message", "(name)", "}}", "foo", "{{", "/export", "}}"),
        )
}
