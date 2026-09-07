/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser.injectors

import com.intellij.lang.injection.InjectedLanguageManager
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.js.vento.plugin.JavaScriptDataObjectElement
import org.js.vento.plugin.JavaScriptElement
import org.js.vento.plugin.JavaScriptExpressionElement
import org.js.vento.plugin.file.VentoFileType
import kotlin.test.assertContains

/**
 * Tests that the shared JS context injected into `{{ }}` expressions declares the real
 * variable names bound by for/set/default/import blocks elsewhere in the file, instead of the
 * old hardcoded generic placeholder list.
 */
class ContextualJavaScriptInjectorTest : BasePlatformTestCase() {
    /**
     * Regression test: the injector used to build one shared document but only ever register it
     * from the callback for the *first* JS element in the file, so querying injection directly
     * at any other element returned nothing - e.g. completion inside the second of two `for`
     * loops, or in any expression after the first one in a file, silently saw no injected
     * context at all. Every JS-injectable element now gets its own working injection.
     */
    fun testSecondElementAlsoResolvesInjection() {
        val code =
            """
            {{ for post of posts }}
            {{ post }}
            {{ /for }}
            """.trimIndent()
        myFixture.configureByText(VentoFileType, code)
        val hostFile = myFixture.file
        val secondElementOffset = code.indexOf("post }}") + 1
        val injected =
            InjectedLanguageManager.getInstance(project).findInjectedElementAt(hostFile, secondElementOffset)
        assertNotNull("Expected the second JS element to resolve its own injection", injected)
        assertContains(injected!!.containingFile.text, "var post;")
    }

    fun testForLoopVariableIsDeclared() {
        val code =
            """
            {{ for item of items }}
            {{ item.name }}
            {{ /for }}
            """.trimIndent()
        assertContains(injectedText(code), "var item;")
    }

    fun testImportedAliasIsDeclared() {
        val code =
            """
            {{ import { a as helper } from "helpers.js" }}
            {{ helper() }}
            """.trimIndent()
        val text = injectedText(code)
        assertContains(text, "var helper;")
        assertFalse("aliased import source name should not be declared", text.contains(" a;") || text.contains(" a,"))
    }

    fun testRepeatedForLoopVariableDoesNotDuplicateOrConflict() {
        val code =
            """
            {{ for item of items }}
            {{ item }}
            {{ /for }}
            {{ for item of otherItems }}
            {{ item }}
            {{ /for }}
            """.trimIndent()

        // Exactly one `var item;` declaration - not one per loop, and using `var` (not
        // `let`/`const`) so the shared synthetic scope tolerates the name being bound by two
        // independent, sequential for-loops without a redeclaration conflict.
        val occurrences = Regex("\\bvar item;").findAll(injectedText(code)).count()
        assertEquals(1, occurrences)
    }

    /** Finds the first JS-injectable element (matching the injector's own document-order pick
     * of which host to anchor the shared injected file to) and returns that file's full text. */
    private fun injectedText(code: String): String {
        myFixture.configureByText(VentoFileType, code)
        val hostFile = myFixture.file
        val firstJsElement =
            listOfNotNull(
                PsiTreeUtil.findChildOfType(hostFile, JavaScriptElement::class.java),
                PsiTreeUtil.findChildOfType(hostFile, JavaScriptExpressionElement::class.java),
                PsiTreeUtil.findChildOfType(hostFile, JavaScriptDataObjectElement::class.java),
            ).minByOrNull { it.textOffset }
        assertNotNull("Expected at least one JS-injectable element in:\n$code", firstJsElement)

        val injectedElement =
            InjectedLanguageManager.getInstance(project).findInjectedElementAt(hostFile, firstJsElement!!.textOffset + 1)
        assertNotNull("Expected an injected JS element inside $firstJsElement", injectedElement)
        return injectedElement!!.containingFile.text
    }
}
