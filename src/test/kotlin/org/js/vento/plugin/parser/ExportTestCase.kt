/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.testFramework.ParsingTestCase
import org.js.vento.plugin.VentoParserDefinition

class ExportTestCase : ParsingTestCase("", "vto", VentoParserDefinition()) {
    fun testVariableExport() {
        val code = "{{ export message = \"Hello, world!\" }}"
        doCodeTest(code)
    }

    fun testVariableBlockExport() {
        val code =
            """
            {{ export message }}
            Hello, world!
            {{ /export }}
            """.trimIndent()
        doCodeTest(code)
    }

    override fun getTestDataPath(): String = "src/test/resources/testdata/import"

    override fun includeRanges(): Boolean = true
}
