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

    fun testAutoInsertCloseForIf() {
        myFixture.configureByText(VentoFileType, "<caret>")
        myFixture.type("{{ if x }}")
        myFixture.checkResult("{{ if x }}\n<caret>\n{{ /if }}")
    }

    fun testAutoInsertCloseForFor() {
        myFixture.configureByText(VentoFileType, "<caret>")
        myFixture.type("{{ for item of items }}")
        myFixture.checkResult("{{ for item of items }}\n<caret>\n{{ /for }}")
    }

    fun testAutoInsertCloseForFunction() {
        myFixture.configureByText(VentoFileType, "<caret>")
        myFixture.type("{{ function hello }}")
        myFixture.checkResult("{{ function hello }}\n<caret>\n{{ /function }}")
    }

    fun testAutoInsertCloseForFragment() {
        myFixture.configureByText(VentoFileType, "<caret>")
        myFixture.type("{{ fragment list }}")
        myFixture.checkResult("{{ fragment list }}\n<caret>\n{{ /fragment }}")
    }

    fun testAutoInsertCloseForSlot() {
        myFixture.configureByText(VentoFileType, "<caret>")
        myFixture.type("{{ slot header }}")
        myFixture.checkResult("{{ slot header }}\n<caret>\n{{ /slot }}")
    }

    fun testAutoInsertCloseForExportBlock() {
        myFixture.configureByText(VentoFileType, "<caret>")
        myFixture.type("{{ export message }}")
        myFixture.checkResult("{{ export message }}\n<caret>\n{{ /export }}")
    }

    fun testAutoInsertCloseForEchoBlock() {
        myFixture.configureByText(VentoFileType, "<caret>")
        myFixture.type("{{ echo }}")
        myFixture.checkResult("{{ echo }}\n<caret>\n{{ /echo }}")
    }

    fun testNoAutoInsertForSetInline() {
        myFixture.configureByText(VentoFileType, "<caret>")
        myFixture.type("{{ set foo = \"bar\" }}")
        myFixture.checkResult("{{ set foo = \"bar\" }}<caret>")
    }

    fun testAutoInsertForSetBlock() {
        myFixture.configureByText(VentoFileType, "<caret>")
        myFixture.type("{{ set foo }}")
        myFixture.checkResult("{{ set foo }}\n<caret>\n{{ /set }}")
    }

    fun testNoAutoInsertForDefaultInline() {
        myFixture.configureByText(VentoFileType, "<caret>")
        myFixture.type("{{ default foo = \"bar\" }}")
        myFixture.checkResult("{{ default foo = \"bar\" }}<caret>")
    }

    fun testAutoInsertForDefaultBlock() {
        myFixture.configureByText(VentoFileType, "<caret>")
        myFixture.type("{{ default foo }}")
        myFixture.checkResult("{{ default foo }}\n<caret>\n{{ /default }}")
    }

    fun testNoAutoInsertForEchoInline() {
        myFixture.configureByText(VentoFileType, "<caret>")
        myFixture.type("{{ echo \"hi\" }}")
        myFixture.checkResult("{{ echo \"hi\" }}<caret>")
    }

    fun testNoAutoInsertForExportInline() {
        myFixture.configureByText(VentoFileType, "<caret>")
        myFixture.type("{{ export message = \"hi\" }}")
        myFixture.checkResult("{{ export message = \"hi\" }}<caret>")
    }

    fun testNoAutoInsertForLayout() {
        myFixture.configureByText(VentoFileType, "<caret>")
        myFixture.type("{{ layout \"file.vto\" }}")
        myFixture.checkResult("{{ layout \"file.vto\" }}<caret>")
    }

    fun testNoAutoInsertForImport() {
        myFixture.configureByText(VentoFileType, "<caret>")
        myFixture.type("{{ import { a } from \"file.vto\" }}")
        myFixture.checkResult("{{ import { a } from \"file.vto\" }}<caret>")
    }

    fun testNoAutoInsertForInclude() {
        myFixture.configureByText(VentoFileType, "<caret>")
        myFixture.type("{{ include \"file.vto\" }}")
        myFixture.checkResult("{{ include \"file.vto\" }}<caret>")
    }

    fun testAutoInsertRespectsIndentation() {
        myFixture.configureByText(
            VentoFileType,
            """
            <div>
                <caret>
            </div>
            """.trimIndent(),
        )
        myFixture.type("{{ if x }}")
        myFixture.checkResult(
            """
            <div>
                {{ if x }}
                <caret>
                {{ /if }}
            </div>
            """.trimIndent(),
        )
    }

    fun testNoAutoClosingWhenBracesExistWithSpaceBefore() {
        // A real block always has a space before its closer (`{{ if x }}`) - only our own
        // auto-close leaves the caret directly against `}}` with no gap. Typing `{{` right
        // before an existing space-padded closer must reuse it, not double-close into
        // `{{}} }}`.
        myFixture.configureByText(VentoFileType, "<caret> }}")
        myFixture.type("{{")
        myFixture.checkResult("{{<caret> }}")
    }

    fun testNoAutoClosingWhenBracesExistWithTabBefore() {
        myFixture.configureByText(VentoFileType, "<caret>\t}}")
        myFixture.type("{{")
        myFixture.checkResult("{{<caret>\t}}")
    }

    override fun getTestDataPath(): String = "src/test/resources/testdata"
}
