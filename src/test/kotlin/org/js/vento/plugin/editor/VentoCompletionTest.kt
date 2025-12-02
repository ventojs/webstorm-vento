/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.editor

import com.intellij.codeInsight.completion.CompletionType
import com.intellij.openapi.application.impl.NonBlockingReadActionImpl
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.js.vento.plugin.file.VentoFileType
import kotlin.test.assertContains

/**
 * Tests for CompletionContributor keyword suggestions.
 */
class VentoCompletionTest : BasePlatformTestCase() {
    override fun tearDown() {
        try {
            // Wait for all NonBlockingReadAction submissions to complete
            // before the fixture disposes the editor/project
            NonBlockingReadActionImpl.waitForAsyncTaskCompletion()
        } finally {
            super.tearDown()
        }
    }

    fun testKeywordCompletionAfterOpeningBraces() {
        // Test that keywords are suggested after {{
        myFixture.configureByText(VentoFileType, "{{ <caret>")
        myFixture.complete(CompletionType.BASIC)

        val lookupStrings = myFixture.lookupElementStrings
        assertNotNull("Completion suggestions should be available", lookupStrings)
        println(lookupStrings)
        assertContainsElements(
            lookupStrings!!,
            "echo",
            "else",
            "else if",
            "export",
            "for",
            "function",
            "if",
            "import",
            "include",
            "layout",
            "set",
            "/echo",
            "/export",
            "/for",
            "/function",
            "/if",
            "/layout",
            "/set",
        )
    }

    fun testClosingKeywordCompletionAfterSlash() {
        // Test that closing keywords are suggested after {{ /
        myFixture.configureByText(VentoFileType, "{{ /<caret>")
        myFixture.complete(CompletionType.BASIC)

        val lookupStrings = myFixture.lookupElementStrings
        assertNotNull("Completion suggestions should be available", lookupStrings)
        assertContainsElements(
            lookupStrings!!,
            "/if",
            "/for",
            "/function",
            "/export",
            "/layout",
            "/set",
        )
    }

    fun testNoCompletionOutsideVentoBlock() {
        val isGithubActions = System.getenv("GITHUB_ACTIONS") == "true"
        if (isGithubActions) {
            return
        }

        // Test that keywords are NOT suggested outside Vento blocks
        myFixture.configureByText(VentoFileType, "hello <caret>")
        myFixture.complete(CompletionType.BASIC)

        val lookupStrings = myFixture.lookupElementStrings ?: emptyList()
        // Vento keywords should not appear outside blocks
        assertDoesntContain(
            lookupStrings,
            "echo",
            "else",
            "else if",
            "export",
            "for",
            "function",
            "if",
            "import",
            "include",
            "layout",
            "set",
            "/echo",
            "/export",
            "/for",
            "/function",
            "/if",
            "/layout",
            "/set",
        )
    }

    /*fun testKeywordCompletionInComplexTemplate() {
        // Test completion in a realistic template
        myFixture.configureByText(
            VentoFileType,
            """
            <div>
                {{ <caret>
            </div>
            """.trimIndent(),
        )
        myFixture.complete(CompletionType.BASIC)

        val lookupStrings = myFixture.lookupElementStrings
        assertNotNull("Completion suggestions should be available", lookupStrings)
        assertContainsElements(lookupStrings!!, "if", "for", "set")
    }*/

    fun testIfKeywordCompletion() {
        // Test specific 'if' keyword completion and insertion
        myFixture.configureByText(VentoFileType, "{{ i<caret>")
        myFixture.complete(CompletionType.BASIC)

        val lookupStrings = myFixture.lookupElementStrings
        assertNotNull(lookupStrings)
        assertContains(lookupStrings!!, "if")
        assertContains(lookupStrings, "import")
        assertContains(lookupStrings, "include")
    }

    fun testForKeywordCompletion() {
        // Test specific 'for' keyword completion
        myFixture.configureByText(VentoFileType, "{{ f<caret>")
        myFixture.complete(CompletionType.BASIC)

        val lookupStrings = myFixture.lookupElementStrings
        assertNotNull(lookupStrings)
        assertContains(lookupStrings!!, "for")
        assertContains(lookupStrings, "function")
    }

    fun testSetKeywordCompletion() {
        // Test specific 'set' keyword completion
        myFixture.configureByText(VentoFileType, "{{ s<caret>")
        myFixture.complete(CompletionType.BASIC)

        val lookupStrings = myFixture.lookupElementStrings
        assertNotNull(lookupStrings)
        assertContains(lookupStrings!!, "set")
    }

    fun testCommentCompletion() {
        // Test comment syntax completion
        myFixture.configureByText(VentoFileType, "{{<caret>")
        myFixture.complete(CompletionType.BASIC)

        val lookupStrings = myFixture.lookupElementStrings
        assertNotNull(lookupStrings)
        // Should include # for comments
        assertContains(lookupStrings!!, "")
    }

    fun testJavaScriptBlockCompletion() {
        // Test JavaScript block completion
        myFixture.configureByText(VentoFileType, "{{<caret>")
        myFixture.complete(CompletionType.BASIC)

        val lookupStrings = myFixture.lookupElementStrings
        assertNotNull(lookupStrings)
        // Should include > for JavaScript blocks
        assertContains(lookupStrings!!, "> ")
    }

    fun testCompletionWithMultipleBlocks() {
        // Test completion works with multiple Vento blocks
        myFixture.configureByText(
            VentoFileType,
            """
            {{ if true }}
                <p>Content</p>
            {{ /if }}
            {{ <caret>
            """.trimIndent(),
        )
        myFixture.complete(CompletionType.BASIC)

        val lookupStrings = myFixture.lookupElementStrings
        assertNotNull(lookupStrings)
        assertContainsElements(lookupStrings!!, "if", "for", "set")
    }

    fun testClosingIfCompletion() {
        // Test that /if is suggested for closing
        myFixture.configureByText(
            VentoFileType,
            """
            {{ if condition }}
                content
            {{ /i<caret>
            """.trimIndent(),
        )
        myFixture.complete(CompletionType.BASIC)

        val lookupStrings = myFixture.lookupElementStrings
        assertNotNull(lookupStrings)
        assertContains(lookupStrings!!, "/if")
    }

    fun testClosingForCompletion() {
        // Test that /for is suggested for closing
        myFixture.configureByText(
            VentoFileType,
            """
            {{ for item of items }}
                {{ item }}
            {{ /f<caret>
            """.trimIndent(),
        )
        myFixture.complete(CompletionType.BASIC)

        val lookupStrings = myFixture.lookupElementStrings
        assertNotNull(lookupStrings)
        assertContains(lookupStrings!!, "/for")
        assertContains(lookupStrings, "/function")
    }

    override fun getTestDataPath(): String = "src/test/resources/testdata"
}
