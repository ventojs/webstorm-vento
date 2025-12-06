/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.testFramework.ParsingTestCase
import org.js.vento.plugin.VentoParserDefinition

class FrontmatterTestCase : ParsingTestCase("frontmatter", "vto", VentoParserDefinition()) {
    fun testEmptyFrontmatter() =
        doCodeTest(
            """
            ---
            ---
            """.trimIndent(),
        )

    fun testFrontmatter() =
        doCodeTest(
            """
            ---
            title: My Blog
            flag:
            - published
            ---
            """.trimIndent(),
        )

    fun testFrontmatterComplexExample() =
        doCodeTest(
            """
            ---
            draft: true
            visibility: 'unlisted'
            
            # equal
            flags:
              - draft
              - unlisted
            ---
            """.trimIndent(),
        )

    override fun getTestDataPath(): String = "src/test/resources/testdata"

    override fun includeRanges(): Boolean = true
}
