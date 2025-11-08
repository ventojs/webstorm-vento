/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.testFramework.ParsingTestCase
import org.js.vento.plugin.VentoParserDefinition

class ForTestCase : ParsingTestCase("", "vto", VentoParserDefinition(true)) {
    fun testSimpleFor() {
        val code = "{{ for item of items }}"
        doCodeTest(code)
    }

    fun testSimpleForClose() {
        val code = "{{ /for }}"
        doCodeTest(code)
    }

    fun testCompleteFor() {
        val code =
            """
            |{{ for item of items }}
            |{{ /for }}
            """.trimMargin()
        doCodeTest(code)
    }

    fun testForWithIndex() {
        val code =
            """
            {{ for index, value of [{a:1},{a:2}] }}
            """.trimMargin()
        doCodeTest(code)
    }

    fun testForObjectWithStrings() {
        val code = """{{ for value of ["a"+"b"+"c","hello"] }}"""
        doCodeTest(code)
    }

    fun testCompleteForObject() {
        val code =
            """
            |{{ for item of {a:1,b:{c:2}} }}
            |{{ /for }}
            """.trimMargin()
        doCodeTest(code)
    }

    fun testCompleteForBrokenObject() {
        val code =
            """
            |{{ for item of {a:1,b:{c:2} }}
            |{{ /for }}
            """.trimMargin()
        doCodeTest(code)
    }

    fun testComplexFor() {
        val code = "{{ for odd_number of [1, 2, 3].filter((n) => n%2) }}"
        doCodeTest(code)
    }

    fun testComplexForWithPipe() {
        val code = "{{ for even_number of [1, 2, 3] |> filter((n) => n % 2 === 0) }}"
        doCodeTest(code)
    }

    fun testForNoValue() {
        val code = "{{ for of values }}"
        doCodeTest(code)
    }

    fun testForNoOf() {
        val code = "{{ for value values }}"
        doCodeTest(code)
    }

    fun testBrokenForCloseFollowedByBlock() {
        val code =
            """
            {{ /fo }}
            {{ "hello"}}
            """.trimIndent()
        doCodeTest(code)
    }

    fun testRecover() {
        val code =
            """
            {{ for number of [1,2,3] }}
            {{ "hello " + number }}
            {{  / }}
            {{ "hi!" }}
            """.trimIndent()
        doCodeTest(code)
    }

    fun testComplexArray() {
        val code = """{{ for [[n]] of [[[1]], [[2]]] }}""".trimIndent()
        doCodeTest(code)
    }

    override fun getTestDataPath(): String = "src/test/resources/testdata/for"

    override fun includeRanges(): Boolean = true
}
