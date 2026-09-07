/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.lexer

/**
 * A bare identifier that merely starts with a Vento keyword's text (e.g. `formatDate` starting
 * with "for") must lex as that identifier, not as the keyword followed by a mangled remainder.
 *
 * `<BLOCK>`'s top-level dispatch previously matched `{KEYWORDS}` as a plain prefix, with no
 * check for what followed - "for" would win against the single-character fallback rule purely
 * on match length, regardless of whether the input actually continued as "formatDate" or ended
 * cleanly at a word boundary. Some keywords (if/import/default/layout/echo/else/for) then had no
 * inner lookahead guard of their own either, so the input fell into a `<KEYWORDS>` sub-state
 * with no matching rule and no fallback, producing a lexer/parser error (or, worse, a *silent*
 * mis-parse for constructs like `set`/`include`/`slot`/`function`/`break`/`continue`, whose
 * grammar happened to still accept the leftover characters as if they were legitimate content).
 */
@Suppress("ktlint:standard:blank-line-before-declaration")
class KeywordPrefixCollisionTestCase(name: String) : BaseLexerTestCase(name) {
    fun `test identifier starting with for`() =
        lexAndTest("{{ formatDate(x) }}", arrayOf("{{", "formatDate", "(", "x", ")", "}}"))

    fun `test identifier starting with if`() =
        lexAndTest("{{ ifExists }}", arrayOf("{{", "ifExists", "}}"))

    fun `test identifier starting with import`() =
        lexAndTest("{{ importantValue }}", arrayOf("{{", "importantValue", "}}"))

    fun `test identifier starting with default`() =
        lexAndTest("{{ defaultValue }}", arrayOf("{{", "defaultValue", "}}"))

    fun `test identifier starting with layout`() =
        lexAndTest("{{ layoutManager }}", arrayOf("{{", "layoutManager", "}}"))

    fun `test identifier starting with echo`() =
        lexAndTest("{{ echoService }}", arrayOf("{{", "echoService", "}}"))

    fun `test identifier starting with else`() =
        lexAndTest("{{ elsewhere }}", arrayOf("{{", "elsewhere", "}}"))

    /**
     * `settings`/`includesGroup`/`slotMachine`/`functional` didn't error before the fix - they
     * were *silently* mis-parsed as `set`/`include`/`slot`/`function` followed by whatever
     * remained (`tings`, `sGroup`, `Machine`, `al`), since those constructs' grammar happily
     * accepted the leftover text as if it were real content. Lock in the correct tokenization
     * so a regression here doesn't slip back in unnoticed.
     */
    fun `test identifier starting with set`() =
        lexAndTest("{{ settings }}", arrayOf("{{", "settings", "}}"))

    fun `test identifier starting with include`() =
        lexAndTest("{{ includesGroup }}", arrayOf("{{", "includesGroup", "}}"))

    fun `test identifier starting with slot`() =
        lexAndTest("{{ slotMachine }}", arrayOf("{{", "slotMachine", "}}"))

    fun `test identifier starting with function`() =
        lexAndTest("{{ functional }}", arrayOf("{{", "functional", "}}"))

    fun `test real for keyword still lexes correctly`() =
        lexAndTest(
            "{{ for item of items }}",
            arrayOf("{{", "for", "item", "of", "items", "}}"),
        )

    fun `test real if keyword still lexes correctly`() =
        lexAndTest("{{ if x }}", arrayOf("{{", "if", "x", "}}"))
}
