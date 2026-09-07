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

    /**
     * Stray tokens after `/export` should be resynced to the next `}}` as a single grouped
     * error, rather than leaking out as top-level HTML content once `}}` is eventually reached.
     */
    fun testExportCloseWithTrailingGarbage() {
        doCodeTest("{{ /export garbage }}")
    }

    override fun getTestDataPath(): String = "src/test/resources/testdata/cornercase"

    override fun includeRanges(): Boolean = true
}
