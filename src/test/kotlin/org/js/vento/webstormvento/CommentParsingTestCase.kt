/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.webstormvento

import com.intellij.testFramework.ParsingTestCase
import org.js.vento.plugin.parser.VentoParserDefinition

class CommentParsingTestCase : ParsingTestCase("", "vto", VentoParserDefinition()) {
    fun testSimpleComment() {
        val code = "{{# This is a comment #}}"
        doCodeTest(code)
    }

    fun testTrimmedComment() {
        val code = "{{#- This is a trimmed comment -#}}"
        doCodeTest(code)
    }

    fun testMultilineComment() {
        val code =
            """{{#
This is
a multiline
comment #}}
            """.trimMargin()
        doCodeTest(code)
    }

    override fun getTestDataPath(): String = "src/test/resources/testdata/comment"

    override fun includeRanges(): Boolean = true
}
