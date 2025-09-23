/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.lexer

@Suppress("ktlint:standard:blank-line-before-declaration")
class VariableLexerTestCase(name: String) : BaseLexerTestCase(name) {
    fun `test string variable`() = lexAndTest("{{ \"Hello World\" }}", arrayOf("{{", " \"Hello World\" ", "}}"))
    fun `test undefined`() = lexAndTest("{{ undefined }}", arrayOf("{{", " undefined ", "}}"))
    fun `test null`() = lexAndTest("{{ null }}", arrayOf("{{", " null ", "}}"))
    fun `test lexing variables`() = lexAndTest("{{ variable }}", arrayOf("{{", " variable ", "}}"))
    fun `test with or`() = lexAndTest("{{ variable || \"default\" }}", arrayOf("{{", " variable || \"default\" ", "}}"))
    fun `test with pipes`() = lexAndTest("{{ variable |> toUpperCase }}", arrayOf("{{", " variable |> toUpperCase ", "}}"))
    fun `test with trimming`() = lexAndTest("Hello {{- \"World\" }}", arrayOf("Hello ", "{{-", " \"World\" ", "}}"))
    fun `test with ternary operator`() = lexAndTest("{{ message ? \"yes\" : \"no\" }}", arrayOf("{{", " message ? \"yes\" : \"no\" ", "}}"))
    fun `test moustache string`() = lexAndTest("{{ \"}}\" }}", arrayOf("{{", " \"}}\" ", "}}"))
    fun `test dashes`() = lexAndTest("{{ foo--}}", arrayOf("{{", " foo-", "-}}"))
    fun `test many dashes`() = lexAndTest("{{-foo---}}", arrayOf("{{-", "foo--", "-}}"))
    fun `test complex`() {
        lexAndTest(
            "{{> let foo = -2 }}\n1 + 1 = {{--foo}}",
            arrayOf("{{>", " let foo = -2 ", "}}", "\n1 + 1 = ", "{{-", "-foo", "}}"),
        )
    }
}
