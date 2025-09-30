/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.testFramework.ParsingTestCase

class ForTestCase : ParsingTestCase("", "vto", VentoParserDefinition()) {
    fun testSimpleFor() {
        val code = "{{ for item of items }}"
        doCodeTest(code)
    }

    fun testSimpleForClose() {
        val code = "{{ /for }}"
        doCodeTest(code)
    }

    fun testCompleteFor() {
        val code =
            """
            |{{ for item of items }}
            |{{ /for }}
            """.trimMargin()
        doCodeTest(code)
    }

    fun testCompleteForObject() {
        val code =
            """
            |{{ for item of {a:1,b:{c:2}} }}
            |{{ /for }}
            """.trimMargin()
        doCodeTest(code)
    }

    fun testCompleteForBrokenObject() {
        val code =
            """
            |{{ for item of {a:1,b:{c:2} }}
            |{{ /for }}
            """.trimMargin()
        doCodeTest(code)
    }

    fun testForNoValue() {
        val code = "{{ for of values }} "
        doCodeTest(code)
    }

    override fun getTestDataPath(): String = "src/test/resources/testdata/for"

    override fun includeRanges(): Boolean = true
}
