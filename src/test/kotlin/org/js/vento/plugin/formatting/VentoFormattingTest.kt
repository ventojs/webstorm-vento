/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.formatting

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.codeStyle.CodeStyleManager
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

        WriteCommandAction.runWriteCommandAction(project) {
            CodeStyleManager.getInstance(project).reformat(myFixture.file)
        }

        val actual =
            myFixture.file.text
                .replace("\r\n", "\n")
                .trim()
        val normalizedExpected = expected.replace("\r\n", "\n").trim()

        if (normalizedExpected != actual) {
            println("[DEBUG_LOG] Expected:\n$normalizedExpected")
            println("[DEBUG_LOG] Actual:\n$actual")
            assertEquals(normalizedExpected, actual)
        }
    }
}
