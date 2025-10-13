/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.testFramework.ParsingTestCase
import org.js.vento.plugin.VentoParserDefinition

class SetTestCase : ParsingTestCase("", "vto", VentoParserDefinition()) {
    fun testSetBlock() {
        val code =
            """
            {{ set myVar }}
            <h1>Hello, world!</h1>
            {{ /set }}
            """.trimIndent()
        doCodeTest(code)
    }

    /**
     * @return path to test data file directory relative to root of this module.
     */
    override fun getTestDataPath(): String = "src/test/resources/testdata/set"

    override fun includeRanges(): Boolean = true
}
