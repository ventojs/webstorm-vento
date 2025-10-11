/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.testFramework.ParsingTestCase
import org.js.vento.plugin.VentoParserDefinition

class HTMLParserTestCase : ParsingTestCase("", "vto", VentoParserDefinition()) {
    fun testParsingTestData() {
        doCodeTest("""<div>{{ content }}</div>""")
    }

    /**
     * @return path to test data file directory relative to root of this module.
     */
    override fun getTestDataPath(): String = "src/test/resources/testdata/html"

    override fun includeRanges(): Boolean = true
}
