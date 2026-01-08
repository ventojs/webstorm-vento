/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

class CornerCaseTestCase : ParsingTestCase() {
    fun testOpenBlockExport() {
        doCodeTest(" {{ \n\n")
    }

    fun testNothing() {
        doCodeTest("")
    }

    fun testSetCausingHanging() {
        doCodeTest("{{ set myVar = [{a:1,b:2},{c:3,d:4}] |> JSON.stringify }}")
    }

    fun testBrokenRegex() {
        doCodeTest("{{ set myVar = /[Hh].*/.*[}]/ }}")
    }

    override fun getTestDataPath(): String = "src/test/resources/testdata/cornercase"

    override fun includeRanges(): Boolean = true
}
