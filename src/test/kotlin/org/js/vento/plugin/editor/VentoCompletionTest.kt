/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.editor

import com.intellij.codeInsight.completion.CompletionType
import com.intellij.openapi.application.impl.NonBlockingReadActionImpl
import com.intellij.testFramework.LoggedErrorProcessor
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.js.vento.plugin.file.VentoFileType
import kotlin.test.assertContains

/**
 * Tests for CompletionContributor keyword suggestions.
 */
@Suppress("ktlint:standard:no-consecutive-comments")
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

    /*
        fun testKeywordCompletionAfterOpeningBraces() {
            // Test that keywords are suggested after {{
            myFixture.configureByText(VentoFileType, "{{ <caret>")
            completeBasic()

            val lookupStrings = myFixture.lookupElementStrings
            assertNotNull("Completion suggestions should be available", lookupStrings)
            println(lookupStrings)
            assertContainsElements(
                lookupStrings!!,
                "default",
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
     */

    fun testClosingKeywordCompletionAfterSlash() {
        // Test that closing keywords are suggested after {{ /
        myFixture.configureByText(VentoFileType, "{{ /<caret>")
        completeBasic()

        val lookupStrings = myFixture.lookupElementStrings
        assertNotNull("Completion suggestions should be available", lookupStrings)
        assertContainsElements(
            lookupStrings!!,
            "/default",
            "/if",
            "/for",
            "/function",
            "/export",
            "/layout",
            "/set",
            "/slot",
            "/fragment",
        )
    }

    fun testNoCompletionOutsideVentoBlock() {
        val isGithubActions = System.getenv("GITHUB_ACTIONS") == "true"
        if (isGithubActions) {
            return
        }

        // Test that keywords are NOT suggested outside Vento blocks
        myFixture.configureByText(VentoFileType, "hello <caret>")
        completeBasic()

        // Wait for async completion to finish
        NonBlockingReadActionImpl.waitForAsyncTaskCompletion()

        val lookupStrings = myFixture.lookupElementStrings ?: emptyList()
        // Vento keywords should not appear outside blocks
        assertDoesntContain(
            lookupStrings,
            "default",
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
        completeBasic()

        val lookupStrings = myFixture.lookupElementStrings
        assertNotNull("Completion suggestions should be available", lookupStrings)
        assertContainsElements(lookupStrings!!, "if", "for", "set")
    }*/

    /*
        fun testIfKeywordCompletion() {
            // Test specific 'if' keyword completion and insertion
            myFixture.configureByText(VentoFileType, "{{ i<caret>")
            completeBasic()

            val lookupStrings = myFixture.lookupElementStrings
            assertNotNull(lookupStrings)
            assertContains(lookupStrings!!, "if")
            assertContains(lookupStrings, "import")
            assertContains(lookupStrings, "include")
        }
     */

    fun testForKeywordCompletion() {
        // Test specific 'for' keyword completion
        myFixture.configureByText(VentoFileType, "{{ f<caret>")
        completeBasic()

        val lookupStrings = myFixture.lookupElementStrings
        assertNotNull(lookupStrings)
        assertContains(lookupStrings!!, "for")
        assertContains(lookupStrings, "function")
    }

/*    fun testSetKeywordCompletion() {
        // Test specific 'set' keyword completion
        myFixture.configureByText(VentoFileType, "{{ s<caret>")
        completeBasic()

        val lookupStrings = myFixture.lookupElementStrings
        assertNotNull(lookupStrings)
        assertContains(lookupStrings!!, "set")
    }

    fun testCommentCompletion() {
        // Test comment syntax completion
        myFixture.configureByText(VentoFileType, "{{<caret>")
        completeBasic()

        val lookupStrings = myFixture.lookupElementStrings
        assertNotNull(lookupStrings)
        // Should include # for comments
        assertContains(lookupStrings!!, "")
    }

    fun testJavaScriptBlockCompletion() {
        // Test JavaScript block completion
        myFixture.configureByText(VentoFileType, "{{<caret>")
        completeBasic()

        val lookupStrings = myFixture.lookupElementStrings
        assertNotNull(lookupStrings)
        // Should include > for JavaScript blocks
        assertContains(lookupStrings!!, "> ")
    }*/

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
        completeBasic()

        val lookupStrings = myFixture.lookupElementStrings
        assertNotNull(lookupStrings)
        assertContainsElements(lookupStrings!!, "if", "for", "set")
    }

    fun testClosingIfCompletion() {
        // Test that /if is suggested for closing. "/i" uniquely matches only "/if" among the
        // closing keywords, so completion may auto-insert it directly instead of leaving a
        // lookup open - either way the result should offer/contain "/if".
        myFixture.configureByText(
            VentoFileType,
            """
            <caret>{{ if condition }}
                content
            {{ /i
            """.trimIndent(),
        )
        val hostDocument = myFixture.editor.document
        myFixture.editor.caretModel.moveToOffset(hostDocument.textLength)
        completeBasic()

        val lookupStrings = myFixture.lookupElementStrings
        if (lookupStrings != null) {
            assertContains(lookupStrings, "/if")
        } else {
            assertContains(hostDocument.text, "/if")
        }
    }

    fun testSlotKeywordCompletion() {
        // Test specific 'slot' keyword completion
        myFixture.configureByText(VentoFileType, "{{ sl<caret> }}")
        completeBasic()

        val lookupStrings = myFixture.lookupElementStrings
        assertNotNull(lookupStrings)
        assertContains(lookupStrings!!, "slot")
    }

    fun testExportFunctionKeywordCompletion() {
        // Test specific 'export function' keyword completion
        myFixture.configureByText(VentoFileType, "{{ export f<caret>")
        completeBasic()

        val lookupStrings = myFixture.lookupElementStrings
        assertNotNull(lookupStrings)
        assertContains(lookupStrings!!, "export function")
    }

    fun testBreakKeywordCompletion() {
        // "br" uniquely matches "break", so completion auto-inserts it directly instead of
        // showing a lookup popup - assert on the resulting text instead of
        // lookupElementStrings, which is null in that case.
        myFixture.configureByText(VentoFileType, "{{ br<caret> }}")
        completeBasic()
        assertContains(myFixture.editor.document.text, "break")
    }

    // No editor-level test for "continue" completion: it reliably crashes test setup with
    // "Cannot restore JSVariable ... from injected" (TestLoggerFactory$TestLoggerAssertionError)
    // regardless of prefix length or a fresh daemon, while the otherwise-identical "break" case
    // (testBreakKeywordCompletion above) is fine - this looks like the bundled JavaScript
    // plugin's own "continue" keyword completion hitting a loop-context smart-pointer lookup
    // against our synthetic injected content, not something in this plugin's control. The
    // grammar/parser side is covered by BreakContinueTestCase.testSimpleContinue instead.

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
        completeBasic()

        val lookupStrings = myFixture.lookupElementStrings
        assertNotNull(lookupStrings)
        assertContains(lookupStrings!!, "/for")
        assertContains(lookupStrings, "/function")
    }

    fun testIfKeywordCompletionDoesNotDuplicateExistingCloser() {
        // Retriggering completion on an already-closed if-block's opening keyword (e.g. the
        // user deletes and retypes "if" via completion instead of by hand) must not tack on a
        // second "{{ /if }}" - one already closes the block a couple of lines down.
        myFixture.configureByText(
            VentoFileType,
            """
            <caret>{{ i }}
            content
            {{ /if }}
            """.trimIndent(),
        )
        val hostDocument = myFixture.editor.document
        // Move past "{{ " into the JS-injected expression region before completing, matching
        // how a real "if<caret>" completion is actually resolved (see InjectedJsCompletionProvider).
        myFixture.editor.caretModel.moveToOffset(4)
        completeBasic()
        val ifItem = myFixture.lookupElements?.firstOrNull { it.lookupString == "if" }
        assertNotNull(ifItem)
        myFixture.lookup.currentItem = ifItem
        myFixture.finishLookup('\n')

        val closerCount = Regex("\\{\\{\\s*/if\\s*}}").findAll(hostDocument.text).count()
        assertEquals(1, closerCount)
    }

    fun testIfKeywordCompletionStillAddsCloserWhenMissing() {
        // The happy path (no existing closer anywhere) must still get one auto-inserted.
        myFixture.configureByText(VentoFileType, "<caret>{{ i }}")
        val hostDocument = myFixture.editor.document
        myFixture.editor.caretModel.moveToOffset(4)
        completeBasic()
        val ifItem = myFixture.lookupElements?.firstOrNull { it.lookupString == "if" }
        assertNotNull(ifItem)
        myFixture.lookup.currentItem = ifItem
        myFixture.finishLookup('\n')

        assertContains(hostDocument.text, "{{ /if }}")
    }

    fun testForKeywordCompletionExpandsFullTemplateFromEmptyBlock() {
        // Completing "for" with nothing typed yet (an empty block, no value/collection filled
        // in) exercises the same PSI-based closer check with genuinely malformed/partial PSI
        // (parseFor can't build a normal FOR_ELEMENT without an expression) - the check must
        // fall back gracefully instead of throwing and aborting the rest of the template,
        // which previously left only the bare "for" behind with none of its placeholders.
        myFixture.configureByText(VentoFileType, "<caret>{{  }}")
        val hostDocument = myFixture.editor.document
        myFixture.editor.caretModel.moveToOffset(3)
        completeBasic()
        val forItem = myFixture.lookupElements?.firstOrNull { it.lookupString == "for" }
        assertNotNull(forItem)
        myFixture.lookup.currentItem = forItem
        myFixture.finishLookup('\n')

        assertContains(hostDocument.text, "value of collection")
        assertContains(hostDocument.text, "{{ /for }}")
    }

    fun testClosingFunctionCompletionDoesNotDuplicateSlash() {
        // Accepting "/function" from a partially-typed "/functi" must replace the leading '/'
        // too, not just the letters after it, or it leaves the original '/' behind and
        // produces "//function".
        myFixture.configureByText(VentoFileType, "<caret>{{ /functi }}")
        val hostDocument = myFixture.editor.document
        myFixture.editor.caretModel.moveToOffset("{{ /functi".length)
        completeBasic()
        val lookup = myFixture.lookup
        if (lookup != null) {
            val item = myFixture.lookupElements?.firstOrNull { it.lookupString == "/function" }
            assertNotNull(item)
            lookup.currentItem = item
            myFixture.finishLookup('\n')
        }

        assertContains(hostDocument.text, "{{ /function }}")
        assertFalse(hostDocument.text.contains("//function"))
    }

    override fun getTestDataPath(): String = "src/test/resources/testdata"

    /**
     * `myFixture.complete()`, guarded against a known IntelliJ Platform bug rather than a bug
     * in this plugin: `MutableLookupStorage.shouldComputeFeatures()` always runs the bundled ML
     * completion ranking pipeline in unit-test mode, and that pipeline's
     * `LocationFeaturesUtil.linesDiff()` (plugins/completion-ml-ranking in intellij-community)
     * occasionally computes a stale/out-of-range document offset and reports it via
     * `LOG.error()`, which the test framework promotes to a hard failure. None of the frames
     * in that crash touch Vento code. Only that one message is swallowed, so a real regression
     * here still fails the test.
     */
    private fun completeBasic() {
        LoggedErrorProcessor.executeWith<Throwable>(IgnoreLinesDiffMlBug()) {
            myFixture.complete(CompletionType.BASIC)
        }
    }

    private class IgnoreLinesDiffMlBug : LoggedErrorProcessor() {
        override fun processError(
            category: String,
            message: String,
            details: Array<String>,
            t: Throwable?,
        ): Set<Action> =
            if (message == "Error while calculating lines diff") {
                setOf(Action.LOG, Action.STDERR)
            } else {
                super.processError(category, message, details, t)
            }
    }
}
