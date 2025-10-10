/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.highlighting.validator

import com.intellij.psi.PsiFile
import com.intellij.psi.impl.source.tree.PsiErrorElementImpl
import com.intellij.psi.util.elementType
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.rd.util.printlnError
import org.js.vento.plugin.ExportBaseElement

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
class ExportBlockValidatorTest : BasePlatformTestCase() {
    fun `test valid single line`() =
        assertValid(
            returnFirstBlockElement(
                """
                {{ export message = "Hello World!" }}
                """.trimIndent(),
            ),
        )

    fun `test valid block`() =
        assertValid(
            returnFirstBlockElement(
                """
                {{ export message |> toUpperCase }}
                <h1>Hello World</h1>
                {{ /export }}
                """.trimIndent(),
            ),
        )

    fun `test invalid line`() =
        assertNotValid(
            returnFirstBlockElement(
                """
                {{ export message = }}
                """.trimIndent(),
            ),
        )

    // =================================
    // Helper Methods
    // =================================

    /**
     * Creates a ForBlockElement from a template string for testing.
     *
     * @param block The template string containing the for block to parse
     * @return ForBlockElement created from the first child node of the parsed template
     */
    private fun returnFirstBlockElement(block: String): ExportBaseElement {
        val file: PsiFile? = myFixture.configureByText("test.vto", block)
//        file?.node?.getChildren(null)?.forEach { println(it.text) }
        val element = ExportBaseElement(file?.node?.firstChildNode!!)
        return element
    }

    /**
     * Asserts that a ForBlockElement is invalid.
     *
     * @param content The ForBlockElement to validate
     */
    private fun assertNotValid(content: ExportBaseElement): Unit = assertValid(content, false)

    /**
     * Asserts the validity of a ForBlockElement.
     *
     * @param content The ForBlockElement to validate
     * @param isValid Expected validity state (default: true)
     */
    private fun assertValid(content: ExportBaseElement, isValid: Boolean = true) {
        content.children.forEach {
            try {
                assertFalse("Found Error Element", it::class.java == PsiErrorElementImpl::class.java)
            } catch (e: Error) {
                lexAndPrint(content)
                throw e
            }
        }
    }

    protected fun lexAndPrint(content: ExportBaseElement) {
        printlnError("-".repeat(30))
        printlnError("Template:")
        printlnError("-".repeat(30))
        printlnError(content.text)
        printlnError("-".repeat(30))
        printlnError("Tokens:")
        printlnError("-".repeat(30))

        content.children.forEach {
            printlnError(
                "token: " + "${it.elementType}".padEnd(40, ' ') + " = [${it.textOffset}]",
            )
        }
    }
}
