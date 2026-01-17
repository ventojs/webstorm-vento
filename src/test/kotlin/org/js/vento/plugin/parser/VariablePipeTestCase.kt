/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

class VariablePipeTestCase : ParsingTestCase() {
    fun testStringAndPipeVariable() {
        val code = "{{ \"Hello, world!\" |> toUpperCase  }}"
        doCodeTest(code)
    }

    fun testObjectAndPipeVariable() {
        val code = "{{ {a:\"hello\",b:\"world\"} |> JSON.stringify }}"
        doCodeTest(code)
    }

    override fun getTestDataPath(): String = "src/test/resources/testdata/variable"

    override fun includeRanges(): Boolean = true
}
