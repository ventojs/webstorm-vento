/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

/**
 * Test case for parsing Vento `set` blocks.
 *
 * Tests the parsing of variable assignment blocks, including both valid syntax
 * and various error conditions such as malformed tags, missing identifiers,
 * and invalid string escaping.
 */
class LayoutTestCase : ParsingTestCase() {
    // Valid Cases

    /**
     * Tests parsing of a valid set block that captures content between opening and closing tags.
     */
    fun testLayoutWithContent() {
        val code =
            """
            {{ layout "section.vto" {department: "Marketing"} }}
             {{- slot header |> toUpperCase }}
                <h1>Section title</h1>
             {{ /slot -}}
                <p>Content of the section</p>
             {{ /layout }}
            """.trimIndent()
        doCodeTest(code)
    }

    /**
     * Tests parsing of a valid set block that captures content between opening and closing tags.
     */
    fun testLayoutWithData() {
        val code =
            """
            {{ layout "section.vto" {name:"true"} }}
                <p>Content of the section</p>
            {{ /layout }}

            {{ layout "myfile.vto" }}
            """.trimIndent()
        doCodeTest(code)
    }

    /**
     * Tests parsing of a valid set block that captures content between opening and closing tags.
     */
    fun testLayoutWithoutSpace() {
        val code =
            """
            {{ layout "myfile.vto"}}
            """.trimIndent()
        doCodeTest(code)
    }

    fun testLayoutWithSingleQuote() {
        val code =
            """
            {{ layout 'myfile.vto' }}
            """.trimIndent()
        doCodeTest(code)
    }

    fun testLayoutWithDataRef() {
        val code =
            """
            {{ layout "myfile.vto" data }}
            """.trimIndent()
        doCodeTest(code)
    }

    // Error Cases

    /**
     * A `slot` block with no matching `/slot` before EOF should be flagged.
     */
    fun testUnclosedSlot() {
        val code =
            """
            {{ layout "section.vto" }}
             {{ slot header }}
                <h1>Section title</h1>
             {{ /layout }}
            """.trimIndent()
        doCodeTest(code)
    }

    /**
     * A stray `/slot` with no matching open `slot` block should be flagged.
     */
    fun testOrphanSlotClose() {
        val code = "{{ /slot }}"
        doCodeTest(code)
    }

    /**
     * A closing tag that doesn't match the innermost open block (e.g. `/layout` closing a
     * `slot`) should be flagged, and the still-unclosed `slot` should also be flagged.
     */
    fun testMismatchedSlotCloseTag() {
        val code =
            """
            |{{ slot header }}
            |{{ /layout }}
            """.trimMargin()
        doCodeTest(code)
    }

    /**
     * Stray tokens after `/layout` should be resynced to the next `}}` as a single grouped
     * error, rather than leaking out as top-level HTML content once `}}` is eventually reached.
     */
    fun testLayoutCloseWithTrailingGarbage() {
        val code = "{{ /layout garbage }}"
        doCodeTest(code)
    }

    /**
     * @return path to test data file directory relative to root of this module.
     */
    override fun getTestDataPath(): String = "src/test/resources/testdata/layout"

    override fun includeRanges(): Boolean = true
}
