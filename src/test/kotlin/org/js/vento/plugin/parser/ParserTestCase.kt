/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

class ParserTestCase : ParsingTestCase() {
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
