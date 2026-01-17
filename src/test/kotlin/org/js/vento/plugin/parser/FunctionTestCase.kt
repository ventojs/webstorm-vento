/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

class FunctionTestCase : ParsingTestCase() {
    fun testComplexFunction() = doCodeTest("{{ function hello ({name = \"World\"} = {}) }}")

    fun testSimpleFunction() = doCodeTest("{{ function hello }}")

    fun testFunctionWithoutName() = doCodeTest("{{ function (name) }}")

    fun testAsyncFunction() = doCodeTest("{{ async function hello }}")

    fun testSimpleFunctionClose() = doCodeTest("{{ /for }}")

    override fun getTestDataPath(): String = "src/test/resources/testdata/function"

    override fun includeRanges(): Boolean = true
}
