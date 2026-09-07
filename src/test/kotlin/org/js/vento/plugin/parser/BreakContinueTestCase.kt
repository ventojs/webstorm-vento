/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

class BreakContinueTestCase : ParsingTestCase() {
    fun testSimpleBreak() {
        val code =
            """
            |{{ for item of items }}
            |{{ break }}
            |{{ /for }}
            """.trimMargin()
        doCodeTest(code)
    }

    fun testSimpleContinue() {
        val code =
            """
            |{{ for item of items }}
            |{{ continue }}
            |{{ /for }}
            """.trimMargin()
        doCodeTest(code)
    }

    override fun getTestDataPath(): String = "src/test/resources/testdata/breakcontinue"

    override fun includeRanges(): Boolean = true
}
