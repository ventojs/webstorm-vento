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

    /**
     * A block-form `function` with a matching `/function` should parse cleanly with no error.
     */
    fun testCompleteFunction() =
        doCodeTest(
            """
            |{{ function hello }}
            |{{ /function }}
            """.trimMargin(),
        )

    /**
     * A stray `/function` with no matching open `function` block should be flagged.
     */
    fun testOrphanFunctionClose() = doCodeTest("{{ /function }}")

    /**
     * A closing tag that doesn't match the innermost open block (e.g. `/if` closing a
     * `function`) should be flagged, and the still-unclosed `function` should also be flagged.
     */
    fun testMismatchedFunctionCloseTag() =
        doCodeTest(
            """
            |{{ function hello }}
            |{{ /if }}
            """.trimMargin(),
        )

    override fun getTestDataPath(): String = "src/test/resources/testdata/function"

    override fun includeRanges(): Boolean = true
}
