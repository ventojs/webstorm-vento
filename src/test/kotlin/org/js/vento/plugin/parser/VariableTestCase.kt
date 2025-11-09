/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.testFramework.ParsingTestCase
import org.js.vento.plugin.VentoParserDefinition

class VariableTestCase : ParsingTestCase("", "vto", VentoParserDefinition()) {
    fun testSimpleVariable() {
        val code = "{{ content }}"
        doCodeTest(code)
    }

    fun testStringVariable() {
        val code = "{{ \"content\" }}"
        doCodeTest(code)
    }

    fun testTrimmedVariable() {
        val code = "{{- content -}}"
        doCodeTest(code)
    }

    fun testComplexVariable() {
        val code = "{{ url |> await fetch |> await json |> JSON.stringify }}"
        doCodeTest(code)
    }

    fun testTrimAndComplexVariable() {
        val code = "{{---foo-}}"
        doCodeTest(code)
    }

    fun testTrimAndComplexVariable2() {
        val code = "{{-foo---}}"
        doCodeTest(code)
    }

    fun testTernaryExpression() {
        val code = "{{ foo ? bar : baz }}"
        doCodeTest(code)
    }

    fun testNewObject() {
        val code = "{{ new Date().getFullYear() }}"
        doCodeTest(code)
    }

    override fun getTestDataPath(): String = "src/test/resources/testdata/variable"

    override fun includeRanges(): Boolean = true
}
