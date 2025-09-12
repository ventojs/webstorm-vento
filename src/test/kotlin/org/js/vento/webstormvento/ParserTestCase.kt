/*
 * Copyright (c) 2023 Óscar Otero
 * All rights reserved.
 */

package org.js.vento.webstormvento

import com.intellij.testFramework.ParsingTestCase
import org.js.vento.plugin.parser.VentoParserDefinition

class ParserTestCase() : ParsingTestCase("", "vto", VentoParserDefinition()) {

    fun testParsingTestData() {
        doTest(true)
    }

    /**
     * @return path to test data file directory relative to root of this module.
     */
    override fun getTestDataPath(): String {
        return "src/test/resources/testdata"
    }

    override fun includeRanges(): Boolean {
        return true
    }

}