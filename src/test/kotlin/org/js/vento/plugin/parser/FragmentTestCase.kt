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

    /**
     * A `fragment` block with no matching `/fragment` before EOF should be flagged.
     */
    fun testUnclosedFragment() {
        val code =
            """
            |{{ fragment list }}
            |<p>content</p>
            """.trimMargin()
        doCodeTest(code)
    }

    /**
     * A stray `/fragment` with no matching open `fragment` block should be flagged.
     */
    fun testOrphanFragmentClose() {
        val code = "{{ /fragment }}"
        doCodeTest(code)
    }

    /**
     * Stray tokens after `/fragment` should be resynced to the next `}}` as a single
     * grouped error, rather than leaking out as top-level HTML content once `}}` is
     * eventually reached.
     */
    fun testFragmentCloseWithTrailingGarbage() {
        val code = "{{ /fragment garbage }}"
        doCodeTest(code)
    }

    override fun getTestDataPath(): String = "src/test/resources/testdata/fragment"

    override fun includeRanges(): Boolean = true
}
