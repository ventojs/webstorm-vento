/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.formatting

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.psi.codeStyle.CodeStyleSettingsManager
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.js.vento.plugin.file.VentoFileType

class VentoFormattingTest : BasePlatformTestCase() {
    fun testIndentBlock() {
        val input =
            """
            <div>
            <div>
            {{set foo }}
            <div>
            <div>Hello</div>
            {{ if foo }}
            <div>{{ content }}</div>
            {{ /if }}
            <div>Hello</div>
            </div>
            {{ /set }}
            </div>
            </div>
            """.trimIndent()

        val expected =
            """
            <div>
                <div>
                    {{set foo }}
                        <div>
                            <div>Hello</div>
                            {{ if foo }}
                                <div>{{ content }}</div>
                            {{ /if }}
                            <div>Hello</div>
                        </div>
                    {{ /set }}
                </div>
            </div>
            """.trimIndent()

        myFixture.configureByText(VentoFileType, input)
        configure()
        myFixture.checkResult(expected)
    }

    fun testIndentHtmlInsideIf() {
        val input =
            """
            {{ if foo }}
            <p>hello</p>
            {{ /if }}
            """.trimIndent()

        val expected =
            """
            {{ if foo }}
                <p>hello</p>
            {{ /if }}
            """.trimIndent()

        myFixture.configureByText(VentoFileType, input)
        configure()
        myFixture.checkResult(expected)
    }

    fun testSetSingleLine() {
        val input =
            """
            {{ set foo = "hello" }}
                World
            """.trimIndent()

        val expected =
            """
            {{ set foo = "hello" }}
            World
            """.trimIndent()

        myFixture.configureByText(VentoFileType, input)
        configure()
        myFixture.checkResult(expected)
    }

    fun testSetUnpaired() {
        val input =
            """
            {{ set foo }}
            Hello
            {{ /set }]
            World
            """.trimIndent()

        val expected =
            """
            {{ set foo }}
                Hello
            {{ /set }]
            World
            """.trimIndent()

        myFixture.configureByText(VentoFileType, input)
        configure()
        myFixture.checkResult(expected)
    }

    fun testSimpleIf() {
        val input =
            """
            {{ if !it.user }}
            No user found!
            {{ /if }}
            """.trimIndent()

        val expected =
            """
            {{ if !it.user }}
                No user found!
            {{ /if }}
            """.trimIndent()

        myFixture.configureByText(VentoFileType, input)
        configure()
        myFixture.checkResult(expected)
    }

    fun testSimpleIfElse() {
        val input =
            """
            {{ if !it.user }}
            No user found!
            {{ else }}
            The user is {{ it.user.name }}.
            {{ /if }}
            """.trimIndent()

        val expected =
            """
            {{ if !it.user }}
                No user found!
            {{ else }}
                The user is {{ it.user.name }}.
            {{ /if }}
            """.trimIndent()

        myFixture.configureByText(VentoFileType, input)
        configure()
        myFixture.checkResult(expected)
    }

    fun testFullIfElse() {
        val input =
            """
            {{ if !it.user }}
            No user found!
            {{ else if !it.user.name }}
            The user doesn't have name.s
            {{ else }}
            The user is {{ it.user.name }}.
            {{ /if }}
            """.trimIndent()

        val expected =
            """
            {{ if !it.user }}
                No user found!
            {{ else if !it.user.name }}
                The user doesn't have name.s
            {{ else }}
                The user is {{ it.user.name }}.
            {{ /if }}
            """.trimIndent()

        myFixture.configureByText(VentoFileType, input)
        configure()
        myFixture.checkResult(expected)
    }

    private fun configure() {
        // Ensure consistent indent options regardless of environment
        val settings = CodeStyleSettingsManager.getSettings(project)
        val indentOptions = settings.getIndentOptions(VentoFileType)
        indentOptions.INDENT_SIZE = 4
        indentOptions.CONTINUATION_INDENT_SIZE = 4
        indentOptions.TAB_SIZE = 4
        indentOptions.USE_TAB_CHARACTER = false

        WriteCommandAction.runWriteCommandAction(project) {
            CodeStyleManager.getInstance(project).reformat(myFixture.file)
        }
    }
}
