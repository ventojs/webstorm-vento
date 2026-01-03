/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

class FragmentTestCase : ParsingTestCase() {
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
