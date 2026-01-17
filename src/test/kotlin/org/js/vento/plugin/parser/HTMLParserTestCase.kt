/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

class HTMLParserTestCase : ParsingTestCase() {
    fun testParsingTestData() {
        doCodeTest("""<div>{{ content }}</div>""")
    }

    /**
     * @return path to test data file directory relative to root of this module.
     */
    override fun getTestDataPath(): String = "src/test/resources/testdata/html"

    override fun includeRanges(): Boolean = true
}
