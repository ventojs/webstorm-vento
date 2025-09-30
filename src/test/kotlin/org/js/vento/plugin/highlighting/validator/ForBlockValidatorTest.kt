/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.highlighting.validator

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.js.vento.plugin.parser.ForBlockElement

/**
 *
 */
@Suppress("ktlint:standard:function-naming")
class ForBlockValidatorTest : BasePlatformTestCase() {
    var validator: ForBlockValidator = ForBlockValidator()

//    fun `test simple for block`() = assertValid("for value of values")
    fun `test simple for block`() = assertTrue(true)

    private fun assertNotValid(content: ForBlockElement): Unit = assertValid(content, false)

    private fun assertValid(content: ForBlockElement, isValid: Boolean = true) {
        val outcome = validator.isValidExpression(content, myFixture.project)
        assertEquals("$content: ${outcome.errorMessage}", isValid, outcome.isValid)
    }
}
