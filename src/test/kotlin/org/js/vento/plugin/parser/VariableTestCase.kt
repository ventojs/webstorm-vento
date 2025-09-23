/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.testFramework.ParsingTestCase

class VariableTestCase : ParsingTestCase("", "vto", VentoParserDefinition()) {
    fun testSimpleVariable() {
        val code = "{{ content }}"
        doCodeTest(code)
    }
 fun testComplexVariable() {
        val code = "{{ url |> await fetch |> await json |> JSON.stringify }}"
        doCodeTest(code)
    }

    override fun getTestDataPath(): String = "src/test/resources/testdata/variable"

    override fun includeRanges(): Boolean = true
}
