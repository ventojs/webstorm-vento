/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.testFramework.ParsingTestCase
import org.js.vento.plugin.VentoParserDefinition

/**
 * Test case for parsing Vento `set` blocks.
 *
 * Tests the parsing of variable assignment blocks, including both valid syntax
 * and various error conditions such as malformed tags, missing identifiers,
 * and invalid string escaping.
 */
class SetTestCase : ParsingTestCase("", "vto", VentoParserDefinition()) {
    // Valid Cases

    /**
     * Tests parsing of a valid set block that captures content between opening and closing tags.
     */
    fun testSetBlockWithContent() {
        val code =
            """
            {{ set myVar }}
            <h1>Hello, world!</h1>
            {{ /set }}
            """.trimIndent()
        doCodeTest(code)
    }

    /**
     *
     */
    fun testSetBlockWithCss() {
        val code =
            """
            {{ set css }}
                body::after {
                    content: "Hello, the CSS world!";
                }
            {{ /set }}
            """.trimIndent()
        doCodeTest(code)
    }

    // Error Cases - Invalid Syntax

    /**
     * Tests parsing error when assignment operator is missing between variable name and value.
     */
    fun testSetError_MissingAssignmentOperator() = doCodeTest("""{{ set myVar  "Hello, world!" }}""")

    /**
     * Tests parsing error when value is missing after assignment operator.
     */
    fun testSetError_MissingValueAfterAssignment() = doCodeTest("""{{ set myVar =  }}""")

    /**
     * Tests parsing error when variable name is missing before assignment operator.
     */
    fun testSetError_MissingVariableName() = doCodeTest("""{{ set = "Hello, world!" }}""")

    /**
     * Tests parsing error with invalid string escaping inside quoted string.
     * TODO: NOTE: This is not valid Vento syntax, but it doesn't currently break parsing.
     */
    fun testSetError_InvalidStringEscaping() = doCodeTest("""{{ set myVar = "Hello World of "vento\"!" }}""")

    /**
     * Tests parsing error when function call is missing dot notation.
     * TODO: NOTE: This is not valid Vento syntax, but it doesn't currently break parsing.
     */
    fun testSetError_InvalidFunctionCall() = doCodeTest("""{{ set myVar = JSON stringify(data) }}""")

    // Error Cases - Malformed Closing Tags

    /**
     * Tests parsing error with incomplete closing tag `/se`.
     */
    fun testSetError_IncompleteClosingTag_Se() = doCodeTest("""{{ /se }}""")

    /**
     * Tests parsing error with incomplete closing tag `/s`.
     */
    fun testSetError_IncompleteClosingTag_S() = doCodeTest("""{{ /s }}""")

    /**
     * Tests parsing error with incomplete closing tag `/et`.
     */
    fun testSetError_IncompleteClosingTag_Et() = doCodeTest("""{{ /et }}""")

    /**
     * Tests parsing error with unclosed closing tag followed by another expression.
     */
    fun testSetError_UnclosedClosingTag() = doCodeTest("""{{ /set {{ foo }}""")

    /**
     * @return path to test data file directory relative to root of this module.
     */
    override fun getTestDataPath(): String = "src/test/resources/testdata/set"

    override fun includeRanges(): Boolean = true
}
