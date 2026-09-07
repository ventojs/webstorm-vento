/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.editor

import com.intellij.codeInsight.completion.CompletionType
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.js.vento.plugin.file.VentoFileType

/**
 * Regression coverage for a crash in [org.js.vento.plugin.parser.injectors.ContextualJavaScriptInjector]:
 * completion anywhere inside a `{{ }}` block used to fail with
 * `TestLoggerFactory$TestLoggerAssertionError: Cannot restore JSVariable ... from injected` -
 * IntelliJ's completion machinery inserts a temporary "dummy identifier" at the caret and
 * reparses to compute candidates, then reverts; the injector's shared JS context previously
 * registered its `const`/`var` declarations as the *suffix* of a zero-width `addPlace` call,
 * which is unstable across that reparse-and-revert cycle - a JS smart pointer created against
 * the temporary state fails to resolve once the real state is restored. Moving that content
 * into the *prefix* parameter instead resolves it. This was fully pre-existing (present with the
 * original hardcoded variable list, unrelated to any dynamic content), reproducible with `main`
 * before this fix, and not specific to any one completion position.
 */
class JsInjectionCompletionStabilityTest : BasePlatformTestCase() {
    fun testCompletionRightAfterOpenBrace() {
        myFixture.configureByText(VentoFileType, "{{<caret>")
        myFixture.complete(CompletionType.BASIC)
        assertNotNull(myFixture.lookupElementStrings)
    }

    fun testCompletionAfterOpenBraceAndSpace() {
        myFixture.configureByText(VentoFileType, "{{ <caret>")
        myFixture.complete(CompletionType.BASIC)
        assertNotNull(myFixture.lookupElementStrings)
    }

    /**
     * "i" is one of the names in the shared context's declared variable list - completing a
     * prefix that exactly matches (or is a prefix of) an already-declared name is exactly the
     * condition that used to trigger the crash.
     */
    fun testCompletionWithPrefixMatchingDeclaredVariable() {
        myFixture.configureByText(VentoFileType, "{{ i<caret>")
        myFixture.complete(CompletionType.BASIC)
        val lookupStrings = myFixture.lookupElementStrings
        assertNotNull(lookupStrings)
        assertContains(lookupStrings!!, "i")
        assertContains(lookupStrings, "if")
        assertContains(lookupStrings, "item")
    }

    fun testReportedInputParsesWithoutUnexpectedToken() {
        myFixture.configureByText(VentoFileType, "{{ if '0'===true }}")
        val infos = myFixture.doHighlighting()
        // Sanity check this parses as expected (only the pre-existing, unrelated "missing
        // closing tag" error - not a crash).
        assertTrue(infos.none { it.description?.contains("Unexpected token") == true })
    }

    fun testCompletionRightBeforeIfCondition() {
        myFixture.configureByText(VentoFileType, "{{ if <caret>'0'===true }}")
        myFixture.complete(CompletionType.BASIC)
        assertNotNull(myFixture.lookupElementStrings)
    }

    private fun assertContains(collection: Collection<String>, element: String) {
        assertTrue("Expected $collection to contain '$element'", collection.contains(element))
    }
}
