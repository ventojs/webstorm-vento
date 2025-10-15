/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.testFramework.ParsingTestCase
import org.js.vento.plugin.VentoParserDefinition

/**
 * Test case for parsing Vento `set` blocks.
 *
 * Tests the parsing of variable assignment blocks, including both valid syntax
 * and various error conditions such as malformed tags, missing identifiers,
 * and invalid string escaping.
 */
class LayoutTestCase : ParsingTestCase("", "vto", VentoParserDefinition()) {
    // Valid Cases

    /**
     * Tests parsing of a valid set block that captures content between opening and closing tags.
     */
    fun testSetBlockWithContent() {
        val code =
            """
            {{ layout "section.vto" {department: "Marketing"} }}
             {{ slot header |> toUpperCase }}
             <h1>Section title</h1>
             {{ /slot }}
             <p>Content of the section</p>
             {{ /layout }}
            """.trimIndent()
        doCodeTest(code)
    }

    /**
     * @return path to test data file directory relative to root of this module.
     */
    override fun getTestDataPath(): String = "src/test/resources/testdata/layout"

    override fun includeRanges(): Boolean = true
}
