/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

class CommentParsingTestCase : ParsingTestCase() {
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

    fun testMultilineBrokenComment() {
        val code =
            """{{# comment
{{ content }}
            """.trimMargin()
        doCodeTest(code)
    }

    fun testMultilineBrokenTrimmedComment() {
        val code =
            """{{#- comment
{{ content }}
            """.trimMargin()
        doCodeTest(code)
    }

    fun testEmptyComment() {
        doCodeTest("{{#--#}}")
    }

    fun testLeftTrimComment() {
        doCodeTest("{{#- left trim #}}")
    }

    fun testBrokenComment() {
        doCodeTest("<h1> {{# {{ title }} {{#}}")
    }

    override fun getTestDataPath(): String = "src/test/resources/testdata/comment"

    override fun includeRanges(): Boolean = true
}
