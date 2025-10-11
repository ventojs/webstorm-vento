/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.testFramework.ParsingTestCase
import org.js.vento.plugin.VentoParserDefinition

class ParserTestCase : ParsingTestCase("", "vto", VentoParserDefinition()) {
    fun testParsingTestData() {
        doCodeTest(
            """{{# comment #}}
{{#- comment -#}}
{{> console.log('test') }}""",
        )
    }

    /**
     * @return path to test data file directory relative to root of this module.
     */
    override fun getTestDataPath(): String = "src/test/resources/testdata"

    override fun includeRanges(): Boolean = true
}
