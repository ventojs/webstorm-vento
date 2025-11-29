/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.editor

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.js.vento.plugin.file.VentoFileType

/**
 * Tests for VentoTypedHandler auto-closing functionality.
 */
class VentoTypedHandlerTest : BasePlatformTestCase() {
    fun testAutoClosingBraces() {
        // Test basic auto-closing of {{
        myFixture.configureByText(VentoFileType, "<caret>")
        myFixture.type("{{")
        myFixture.checkResult("{{<caret>}}")
    }

    fun testAutoClosingInMiddleOfContent() {
        // Test auto-closing when there's content after
        myFixture.configureByText(VentoFileType, "<caret>hello world")
        myFixture.type("{{")
        myFixture.checkResult("{{<caret>}}hello world")
    }

    fun testAutoClosingWithExistingContent() {
        // Test auto-closing with HTML content
        myFixture.configureByText(
            VentoFileType,
            """
            <div>
                <caret>
            </div>
            """.trimIndent(),
        )
        myFixture.type("{{")
        myFixture.checkResult(
            """
            <div>
                {{<caret>}}
            </div>
            """.trimIndent(),
        )
    }

    fun testNoAutoClosingWhenBracesExist() {
        // Should not auto-close if closing braces already exist
        myFixture.configureByText(VentoFileType, "{<caret>}}")
        myFixture.type("{")
        myFixture.checkResult("{{<caret>}}")
    }

    fun testAutoClosingWithKeyword() {
        // Test typing a keyword after auto-closing
        myFixture.configureByText(VentoFileType, "<caret>")
        myFixture.type("{{")
        myFixture.type(" if ")
        myFixture.checkResult("{{ if <caret>}}")
    }

    fun testAutoClosingMultipleTimes() {
        // Test multiple auto-closes in sequence
        myFixture.configureByText(VentoFileType, "<caret>")
        myFixture.type("{{")
        myFixture.type(" if true ")
        // Move to end and add more
        myFixture.editor.caretModel.moveToOffset(myFixture.editor.document.textLength)
        myFixture.type("\n{{")
        myFixture.type(" /if ")

        val result = myFixture.editor.document.text
        assertTrue("Should contain two Vento blocks", result.contains("{{ if true }}"))
        assertTrue("Should contain closing block", result.contains("{{ /if }}"))
    }

    fun testAutoClosingWithForLoop() {
        // Test realistic for loop scenario
        myFixture.configureByText(VentoFileType, "<caret>")
        myFixture.type("{{")
        myFixture.type(" for item of items ")
        myFixture.checkResult("{{ for item of items <caret>}}")
    }

    fun testAutoClosingWithSet() {
        // Test realistic set statement scenario
        myFixture.configureByText(VentoFileType, "<caret>")
        myFixture.type("{{")
        myFixture.type(" set foo = \"bar\" ")
        myFixture.checkResult("{{ set foo = \"bar\" <caret>}}")
    }

    override fun getTestDataPath(): String = "src/test/resources/testdata"
}
