/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.testFramework.ParsingTestCase
import org.js.vento.plugin.VentoParserDefinition

class IncludeTestCase : ParsingTestCase("", "vto", VentoParserDefinition()) {
    // Valid Cases

    fun testInclude() {
        val code =
            """{{ include "myfile.vto" {salutation: "Good bye"} |> toUpperCase }}""".trimIndent()
        doCodeTest(code)
    }

    fun testBrockenInclude() {
        val code =
            """{{ include "myfile.vto" |> toUpperCase {salutation: "Good bye"} }}""".trimIndent()
        doCodeTest(code)
    }

    /**
     * @return path to test data file directory relative to root of this module.
     */
    override fun getTestDataPath(): String = "src/test/resources/testdata/include"

    override fun includeRanges(): Boolean = true
}
