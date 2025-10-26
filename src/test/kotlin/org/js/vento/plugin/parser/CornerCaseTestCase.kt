/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.testFramework.ParsingTestCase
import org.js.vento.plugin.VentoParserDefinition

class CornerCaseTestCase : ParsingTestCase("", "vto", VentoParserDefinition()) {
    fun testOpenBlockExport() {
        doCodeTest(" {{ \n\n")
    }

    fun testNothing() {
        doCodeTest("")
    }

    fun testSetCausingHanging() {
        doCodeTest("{{ set myVar = [{a:1,b:2},{c:3,d:4}] |> JSON.stringify }}")
    }

    override fun getTestDataPath(): String = "src/test/resources/testdata/cornercase"

    override fun includeRanges(): Boolean = true
}
