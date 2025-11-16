/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.testFramework.ParsingTestCase
import org.js.vento.plugin.VentoParserDefinition

class PipeTestCase : ParsingTestCase("", "vto", VentoParserDefinition()) {
    fun `test escape pipe with string`() {
        val code = "{{ \"<h1>Hello, world!</h1>\" |> escape }}"
        doCodeTest(code)
    }

    fun `test safe pipe with method call`() {
        val code = "{{ myTrustedSource.getHtml() |> safe }}"
        doCodeTest(code)
    }

    fun `test custom filter with arguments`() {
        val code = "{{ \"<h1>Hello, world!</h1>\" |> filter_name(arg1, arg2) }}"
        doCodeTest(code)
    }

    fun `test json stringify of object`() {
        val code = "{{ { name: \"Óscar\", surname: \"Otero\" } |> JSON.stringify }}"
        doCodeTest(code)
    }

    fun `test json stringify of object with spacing`() {
        val code = "{{ { name: \"Óscar\", surname: \"Otero\" } |> JSON.stringify(null, 2) }}"
        doCodeTest(code)
    }

    fun `test toUpperCase pipe`() {
        val code = "{{ \"Hello, world!\" |> toUpperCase }}"
        doCodeTest(code)
    }

    fun `test await fetch json stringify pipeline`() {
        val code = """
                {{
                   "https://example.com/data.json"
                      |> await fetch
                      |> await json
                      |> JSON.stringify
                }}
                """
        doCodeTest(code)
    }

    fun `test complex pipe expression`() {
        val code = """
               {{ for even_number of [1, 2, 3] |> filter((n) => n % 2 === 0) }}
                """
        doCodeTest(code)
    }

    /**
     * @return path to test data file directory relative to root of this module.
     */
    override fun getTestDataPath(): String = "src/test/resources/testdata/pipes"

    override fun includeRanges(): Boolean = true
}
