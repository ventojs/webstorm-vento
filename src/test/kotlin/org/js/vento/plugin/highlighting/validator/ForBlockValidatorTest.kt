/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.highlighting.validator

import com.intellij.psi.PsiFile
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.js.vento.plugin.ForBlockElement

/**
 * Test suite for ForBlockValidator functionality.
 *
 * This test class validates the parsing and validation logic for Vento template "for" blocks,
 * ensuring proper syntax validation for various for block patterns and error detection
 * for malformed expressions.
 *
 * Test Categories:
 * - Valid Syntax Tests: Various valid for block patterns with different data sources
 * - Invalid Syntax Tests: Malformed for blocks that should be rejected by the validator
 */
@Suppress("ktlint:standard:function-naming")
class ForBlockValidatorTest : BasePlatformTestCase() {
    var validator: ForBlockValidator = ForBlockValidator()

    // =================================
    // Valid Syntax Tests
    // =================================

    /**
     * Tests basic for block syntax with a simple variable iteration.
     * Pattern: {{for variable of collection}}
     */
    fun `test simple for block`() = assertValid(returnFirstAsForBlockElement("{{for value of values}}"))

    /**
     * Tests for block with key-value destructuring from object literal.
     * Pattern: {{for key,value of object}}
     */
    fun `test for block with collection`() = assertValid(returnFirstAsForBlockElement("{{for key,value of {a:1,b:2} }}"))

    /**
     * Tests for block iteration over array literal.
     * Pattern: {{for item of array}}
     */
    fun `test for block with array`() = assertValid(returnFirstAsForBlockElement("{{for item of [{a:1},{b:2},{c:3}] }}"))

    /**
     * Tests for block with numeric range iteration.
     * Pattern: {{for variable of number}}
     */
    fun `test for block with range`() = assertValid(returnFirstAsForBlockElement("{{ for count of 10 }}"))

    /**
     * Tests for block with function call as data source.
     * Pattern: {{for variable of function()}}
     */
    fun `test for block with function`() = assertValid(returnFirstAsForBlockElement("{{ for item of getItems() }}"))

    /**
     * Tests for block with async function call using await.
     * Pattern: {{for await variable of asyncFunction()}}
     */
    fun `test for block with await function`() = assertValid(returnFirstAsForBlockElement("{{ for await item of getItems() }}"))

    /**
     * Tests for block iteration over string literal characters.
     * Pattern: {{for variable of "string"}}
     */
    fun `test for block with string`() = assertValid(returnFirstAsForBlockElement("{{ for letter of \"abcd\" }}"))

    /**
     * Tests for block iteration over string literal characters.
     * Pattern: {{for variable of "string"}}
     */
    fun `test complex for`() = assertValid(returnFirstAsForBlockElement("{{ for even_number of [1, 2, 3] |> filter((n) => n % 2 === 0) }}"))

    // =================================
    // Invalid Syntax Tests
    // =================================

    /**
     * Tests that for blocks without variable names are rejected.
     * Invalid pattern: {{for of collection}}
     */
    fun `test for block without value`() = assertNotValid(returnFirstAsForBlockElement("{{for of values}}"))

    /**
     * Tests that for blocks missing the "of" keyword are rejected.
     * Invalid pattern: {{for variable collection}}
     */
    fun `test for block without of`() = assertNotValid(returnFirstAsForBlockElement("{{for value values}}"))

    /**
     * Tests that for blocks without a data source are rejected.
     * Invalid pattern: {{for variable of }}
     */
    fun `test for block without collection`() = assertNotValid(returnFirstAsForBlockElement("{{for value of }}"))

    /**
     * Tests that for blocks without proper spacing are rejected.
     * Invalid pattern: {{forvalueofcollection}}
     */
    fun `test for block without spaces`() = assertNotValid(returnFirstAsForBlockElement("{{forvalueofcollection}}"))

    /**
     * Tests that for blocks without proper spacing are rejected.
     * Invalid pattern: {{forvalueofcollection}}
     */
    fun `test for block merged for and value`() = assertNotValid(returnFirstAsForBlockElement("{{ forletter of \"abcd\" }}"))

    // =================================
    // Invalid Syntax Tests
    // =================================

    /**
     * Tests closing for blocks with proper spacing.
     * Valid pattern: {{ /for }}
     */
    fun `test closing for block`() = assertValid(returnFirstAsForBlockElement("{{ /for }}"))

    /**
     * Tests closing for blocks without proper spacing.
     * Valid pattern: {{/for}}
     */
    fun `test trimmed closing for block`() = assertValid(returnFirstAsForBlockElement("{{/for}}"))

    /**
     * Tests broken closing for block.
     * Invalid pattern: {{/fr}}
     */
    fun `test broken closing for block`() = assertNotValid(returnFirstAsForBlockElement("{{/fr}}"))

    // =================================
    // Helper Methods
    // =================================

    /**
     * Creates a ForBlockElement from a template string for testing.
     *
     * @param block The template string containing the for block to parse
     * @return ForBlockElement created from the first child node of the parsed template
     */
    private fun returnFirstAsForBlockElement(block: String): ForBlockElement {
        val file: PsiFile? = myFixture.configureByText("test.vto", block)
        file?.node?.getChildren(null)?.forEach { println(it.text) }
        val element = ForBlockElement(file?.node?.firstChildNode!!)
        return element
    }

    /**
     * Asserts that a ForBlockElement is invalid.
     *
     * @param content The ForBlockElement to validate
     */
    private fun assertNotValid(content: ForBlockElement): Unit = assertValid(content, false)

    /**
     * Asserts the validity of a ForBlockElement.
     *
     * @param content The ForBlockElement to validate
     * @param isValid Expected validity state (default: true)
     */
    private fun assertValid(content: ForBlockElement, isValid: Boolean = true) {
        val outcome = validator.isValidExpression(content, myFixture.project)
        assertEquals("$content: ${outcome.errorMessage}", isValid, outcome.isValid)
    }
}
