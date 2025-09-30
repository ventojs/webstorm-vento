/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.highlighting.validator

import com.intellij.lang.Language
import com.intellij.testFramework.fixtures.BasePlatformTestCase

/**
 * Tests for VentoJavaScriptExpressionValidator to verify JavaScript expression validation
 * within Vento templates. Ensures expressions are valid while statements are rejected.
 */
@Suppress("ktlint:standard:function-naming")
class JsExpressionValidatorTest : BasePlatformTestCase() {
    lateinit var validator: JsExpressionValidator

    override fun setUp() {
        super.setUp()
        assertTrue(Language.findLanguageByID("JavaScript") != null)
        validator = JsExpressionValidator()
    }

    //region Valid Expression Tests

    /** Tests basic arithmetic expression validation */
    fun `test arithmetic`() = assertValid("1 + 1")

    /** Tests simple variable reference validation */
    fun `test value`() = assertValid("foo")

    /** Tests regex pattern validation */
    fun `test regex`() = assertValid("!/[/\"]/.test('foo/bar')")

    /** Tests comments within expressions */
    fun `test comment and expression`() = assertValid("/** comment */ \"Hello \" +\"World!\"")

    /** Tests expression with Vento pipe operator */
    fun `test expression and vento pipe`() = assertValid(" '1'+foo |> toUpperCase")

    /** Tests object literal with Vento pipe operator */
    fun `test object and vento pipe`() = assertValid("{ a: 1, b:2} |> JSON.stringify")

    /** Tests JSON object with Vento pipe operator */
    fun `test json vento pipe`() = assertValid("{ \"a\": \"1\", \"b\":\"2\"} |> toUpperCase")
    //endregion

    //region Invalid Expression Tests

    /** Tests rejection of variable declaration statements */
    fun `test simple statement`() = assertNotValid("let total = 1 + 1")

    /** Tests rejection of if statements */
    fun `test if`() = assertNotValid("if(foo) {console.log('hello')}")

    /** Tests rejection of multiple expressions with pipe */
    fun `test too many expressions and pipe`() = assertNotValid("greeting;foo; |> JSON.stringify ")

    /** Tests rejection of scope block statements */
    fun `test scope block`() = assertNotValid("{ const foo=1 }")
    //endregion

    private fun assertNotValid(content: String): Unit = assertValid(content, false)

    private fun assertValid(content: String, isValid: Boolean = true) {
        val outcome = validator.isValidExpression(content, myFixture.project)
        assertEquals("$content: ${outcome.errorMessage}", isValid, outcome.isValid)
    }
}
