/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.testFramework.ParsingTestCase
import org.js.vento.plugin.VentoParserDefinition

/**
 * Test suite for parsing export statements in Vento templates.
 *
 * This test case validates the parser's ability to correctly handle various forms of export syntax:
 * - Simple inline variable exports
 * - Block-level variable exports
 * - Function exports with parameters
 * - Exports with pipe operators for transformations
 * - Complex expressions including method calls, regex, and object literals
 *
 * Each test verifies that the parser generates the correct AST structure for the given export syntax.
 */
class ExportTestCase : ParsingTestCase("", "vto", VentoParserDefinition()) {
    // ========================================
    // Simple Variable Exports
    // ========================================

    /**
     * Tests parsing of a simple inline variable export with a string literal value.
     * Verifies basic export syntax: `{{ export variableName = value }}`
     */
    fun testSimpleVariableExport() {
        val code = "{{ export message = \"Hello, world!\" }}"
        doCodeTest(code)
    }

    /**
     * Tests parsing of a block-level variable export where content is captured between tags.
     * Verifies syntax: `{{ export variableName }} content {{ /export }}`
     */
    fun testBlockVariableExport() {
        val code =
            """
            {{ export message }}
            Hello, world!
            {{ /export }}
            """.trimIndent()
        doCodeTest(code)
    }

    // ========================================
    // Function Exports
    // ========================================

    /**
     * Tests parsing of a function export with parameters using block syntax.
     * Verifies that function declarations with parameters and template content are correctly parsed.
     */
    fun testFunctionExportWithParameter() {
        val code =
            """
            {{ export function sayHello(name) }}
            Hello, {{ name }}!
            {{ /export }}
            """.trimIndent()
        doCodeTest(code)
    }

    // ========================================
    // Exports with Pipe Operators
    // ========================================

    /**
     * Tests parsing of an export statement with a single pipe operator for value transformation.
     * Verifies that pipe operator syntax `|>` is correctly parsed in export statements.
     */
    fun testExportWithSinglePipe() {
        val code =
            """
            {{ export message = "Hello, world!" |> JSON.stringify }}
            """.trimIndent()
        doCodeTest(code)
    }

    fun testExportBlockWithSinglePipe() {
        val code =
            """
            {{ export message |> toUpperCase }}
            <h1>Hello World</h1>
            {{ /export }}
            """.trimIndent()
        doCodeTest(code)
    }

    /**
     * Tests parsing of an export with multiple chained pipe operators.
     * Verifies complex transformation chains: value |> transform1 |> transform2 |> transform3
     */
    fun testExportWithChainedPipes() {
        val code =
            """
            {{ export match = JSON.stringify({a:1,b:{c:2}.d:"hello"}) |> toUpperCase |> /HELLO/.test }}
            """.trimIndent()
        doCodeTest(code)
    }

    // ========================================
    // Complex Expression Exports
    // ========================================

    /**
     * Tests parsing of an export with a method call expression.
     * Verifies that function invocations (e.g., JSON.parse) are correctly parsed as export values.
     */
    fun testExportWithMethodCall() {
        val code =
            """
            {{ export data = JSON.parse("{}") }}
            """.trimIndent()
        doCodeTest(code)
    }

    /**
     * Tests parsing of an export with a regex test expression.
     * Verifies that regex literals and method calls on them are correctly parsed.
     */
    fun testExportWithRegexExpression() {
        val code =
            """
            {{ export match = /foo/.test(message) }}
            """.trimIndent()
        doCodeTest(code)
    }

    /**
     * Tests parsing of an export with a complex object literal expression.
     * Verifies nested object syntax with property access chains are correctly parsed.
     */
    fun testExportWithObjectLiteral() {
        val code =
            """
            {{ export match = {a:1,b:{c:2}.d:"hello"} }}
            """.trimIndent()
        doCodeTest(code)
    }

    // ========================================
    // ERROR Expression Exports
    // ========================================

    fun testExportValueNoExpression() {
        val code =
            """
            {{ export message = }}
            """.trimIndent()
        doCodeTest(code)
    }

    fun testExportNoValue() {
        val code =
            """
            {{ export  = "foo" }}
            """.trimIndent()
        doCodeTest(code)
    }

    fun testExportNoEqual() {
        val code =
            """
            {{ export message "foo" }}
            """.trimIndent()
        doCodeTest(code)
    }

    fun testExportNothing() {
        val code =
            """
            {{ export  }}
            """.trimIndent()
        doCodeTest(code)
    }

    override fun getTestDataPath(): String = "src/test/resources/testdata/export"

    override fun includeRanges(): Boolean = true
}
