/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.testFramework.ParsingTestCase
import org.js.vento.plugin.VentoParserDefinition

class FragmentTestCase : ParsingTestCase("", "vto", VentoParserDefinition()) {
    fun testFragmentWithContent() {
        val code =
            """
            <html>
                {{ fragment list }}
                    {{ for user in users }}
                        <li>{{ user }}</li>
                    {{ /for }}
                {{ /fragment }}
            </html>
            """.trimIndent()
        doCodeTest(code)
    }

    override fun getTestDataPath(): String = "src/test/resources/testdata/fragment"

    override fun includeRanges(): Boolean = true
}
