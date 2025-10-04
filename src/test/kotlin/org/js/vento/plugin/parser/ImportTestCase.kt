/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.testFramework.ParsingTestCase
import org.js.vento.plugin.VentoParserDefinition

class ImportTestCase : ParsingTestCase("", "vto", VentoParserDefinition()) {
    fun testSimpleImport() {
        val code = "{{ import { foo } from \"./afile.vto\" }}"
        doCodeTest(code)
    }

    fun testBareImport() {
        val code = "{{ import foo from \"./afile.vto\" }}"
        doCodeTest(code)
    }

    override fun getTestDataPath(): String = "src/test/resources/testdata/import"

    override fun includeRanges(): Boolean = true
}
