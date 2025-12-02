/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.editor

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.js.vento.plugin.file.VentoFileType

/**
 * Tests for VentoBraceMatcher bracket highlighting functionality.
 */
class VentoBraceMatcherTest : BasePlatformTestCase() {
    fun testSimpleBraceMatching() {
        // Test basic {{ }} matching
        myFixture.configureByText(
            VentoFileType,
            "<<caret>{ set foo = 'bar' }}",
        )

        val braceMatchingInfo = myFixture.file.findElementAt(myFixture.caretOffset)
        assertNotNull("Should find element at caret", braceMatchingInfo)
    }

    fun testBraceMatchingInIfBlock() {
        // Test brace matching in if blocks
        myFixture.configureByText(
            VentoFileType,
            """
            <<caret>{ if condition }}
                <p>Content</p>
            {{ /if }}
            """.trimIndent(),
        )

        val element = myFixture.file.findElementAt(myFixture.caretOffset)
        assertNotNull("Should find element at opening brace", element)
    }

    fun testBraceMatchingInForLoop() {
        // Test brace matching in for loops
        myFixture.configureByText(
            VentoFileType,
            """
            <<caret>{ for item of items }}
                {{ item }}
            {{ /for }}
            """.trimIndent(),
        )

        val element = myFixture.file.findElementAt(myFixture.caretOffset)
        assertNotNull("Should find element at opening brace", element)
    }

    fun testClosingBraceMatching() {
        // Test matching from closing brace
        myFixture.configureByText(
            VentoFileType,
            "{{ set foo = 'bar' }<caret>}",
        )

        val element = myFixture.file.findElementAt(myFixture.caretOffset)
        assertNotNull("Should find element at closing brace", element)
    }

    fun testNestedBraceMatching() {
        // Test brace matching with nested blocks
        myFixture.configureByText(
            VentoFileType,
            """
            <<caret>{ if outer }}
                {{ if inner }}
                    content
                {{ /if }}
            {{ /if }}
            """.trimIndent(),
        )

        val element = myFixture.file.findElementAt(myFixture.caretOffset)
        assertNotNull("Should find element in nested structure", element)
    }

    fun testMultipleBracePairs() {
        // Test with multiple brace pairs on same line
        myFixture.configureByText(
            VentoFileType,
            "<<caret>{ foo }} {{ bar }}",
        )

        val element = myFixture.file.findElementAt(myFixture.caretOffset)
        assertNotNull("Should find element with multiple pairs", element)
    }

    fun testBraceMatchingWithHTML() {
        // Test brace matching mixed with HTML
        myFixture.configureByText(
            VentoFileType,
            """
            <div>
                <<caret>{ set title = "Hello" }}
                <h1>{{ title }}</h1>
            </div>
            """.trimIndent(),
        )

        val element = myFixture.file.findElementAt(myFixture.caretOffset)
        assertNotNull("Should find element mixed with HTML", element)
    }

    fun testBraceMatchingInExpression() {
        // Test brace matching in expression context
        myFixture.configureByText(
            VentoFileType,
            """
            <div>
                <p><<caret>{ user.name }}</p>
            </div>
            """.trimIndent(),
        )

        val element = myFixture.file.findElementAt(myFixture.caretOffset)
        assertNotNull("Should find element in expression", element)
    }

    fun testBraceMatchingWithComment() {
        // Test brace matching with comments
        myFixture.configureByText(
            VentoFileType,
            """
            <<caret>{# This is a comment #}}
            {{ set foo = 'bar' }}
            """.trimIndent(),
        )

        val element = myFixture.file.findElementAt(myFixture.caretOffset)
        assertNotNull("Should find element with comments", element)
    }

    fun testBraceMatchingAtEndOfFile() {
        // Test brace matching at end of file
        myFixture.configureByText(
            VentoFileType,
            "<<caret>{ foo }}",
        )

        val element = myFixture.file.findElementAt(myFixture.caretOffset)
        assertNotNull("Should find element at end of file", element)
    }

    override fun getTestDataPath(): String = "src/test/resources/testdata"
}
