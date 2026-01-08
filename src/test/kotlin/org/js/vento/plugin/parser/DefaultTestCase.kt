/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.testFramework.ParsingTestCase
import org.js.vento.plugin.VentoParserDefinition

class DefaultTestCase : ParsingTestCase("", "vto", VentoParserDefinition()) {
    // Valid Cases

    fun testDefaultBlockWithContent() {
        val code =
            """
            {{ default myVar }}
            <h1>Hello, world!</h1>
            {{ /default }}
            """.trimIndent()
        doCodeTest(code)
    }

    fun testDefaultBlockWithCss() {
        val code =
            """
            {{ default css }}
                body::after {
                    content: "Hello, the CSS world!";
                }
            {{ /default }}
            """.trimIndent()
        doCodeTest(code)
    }

    // Error Cases - Invalid Syntax

    fun testDefaultError_MissingAssignmentOperator() = doCodeTest("""{{ default myVar  "Hello, world!" }}""")

    fun testDefaultError_MissingValueAfterAssignment() = doCodeTest("""{{ default myVar =  }}""")

    fun testDefaultError_MissingVariableName() = doCodeTest("""{{ default = "Hello, world!" }}""")

    fun testDefaultError_InvalidStringEscaping() = doCodeTest("""{{ default myVar = "Hello World of "vento\"!" }}""")

    fun testDefaultError_InvalidFunctionCall() = doCodeTest("""{{ default myVar = JSON stringify(data) }}""")

    fun testObject() =
        doCodeTest(
            """
            {{ default myVar = {"a": 1, "b": true, "c":{"d":"hello"}} }}
            {{ default myVar = {"a": 1, "b": true, "c":{"d":"hello"} } }}
            """.trimIndent(),
        )

    // Error Cases - Malformed Closing Tags

    fun testDefaultError_IncompleteClosingTag_Se() = doCodeTest("""{{ /se }}""")

    fun testDefaultError_IncompleteClosingTag_S() = doCodeTest("""{{ /s }}""")

    fun testDefaultError_IncompleteClosingTag_Et() = doCodeTest("""{{ /et }}""")

    fun testDefaultError_UnclosedClosingTag() = doCodeTest("""{{ /default {{ foo }}""")

    override fun getTestDataPath(): String = "src/test/resources/testdata/default/"

    override fun includeRanges(): Boolean = true
}
