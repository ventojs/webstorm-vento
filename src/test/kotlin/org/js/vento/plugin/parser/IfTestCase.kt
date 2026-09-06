/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

/**
 * Tests for parsing Vento `if` blocks, including open/close tag matching.
 */
class IfTestCase : ParsingTestCase() {
    fun testSimpleIf() {
        val code = "{{ if condition }}"
        doCodeTest(code)
    }

    fun testCompleteIf() {
        val code =
            """
            |{{ if condition }}
            |{{ /if }}
            """.trimMargin()
        doCodeTest(code)
    }

    fun testIfElse() {
        val code =
            """
            |{{ if a }}
            |{{ else }}
            |{{ /if }}
            """.trimMargin()
        doCodeTest(code)
    }

    /**
     * An `if` block with no matching `/if` before EOF should be flagged.
     */
    fun testUnclosedIf() {
        val code =
            """
            |{{ if condition }}
            |<p>content</p>
            """.trimMargin()
        doCodeTest(code)
    }

    /**
     * A stray `/if` with no matching open `if` block should be flagged.
     */
    fun testOrphanIfClose() {
        val code = "{{ /if }}"
        doCodeTest(code)
    }

    /**
     * A closing tag that doesn't match the innermost open block (e.g. `/if` closing a
     * `for`) should be flagged, and the still-unclosed `for` should also be flagged.
     */
    fun testMismatchedCloseTag() {
        val code =
            """
            |{{ for item of items }}
            |{{ /if }}
            """.trimMargin()
        doCodeTest(code)
    }

    override fun getTestDataPath(): String = "src/test/resources/testdata/if"

    override fun includeRanges(): Boolean = true
}
