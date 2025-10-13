/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.testFramework.ParsingTestCase
import org.js.vento.plugin.VentoParserDefinition
import org.junit.jupiter.api.assertAll

class SetTestCase : ParsingTestCase("", "vto", VentoParserDefinition()) {
    fun testWithExpressionSet() {
        assertSet(expressionSet, "exp-set", "{{ set myVar = %s }}")
    }

    fun testWithExpressionSetWithPipe() {
        assertSet(expressionSet, "exp-set-with-pipe", "{{ set myVar = %s |> JSON.stringify }}")
    }

    private fun assertSet(set: Set<Pair<String, String>>, filenamePrefix: String, template: String) {
        var codes: List<String> = listOf()
        assertAll(
            set.map {
                {
                    this.name = "$filenamePrefix-${it.first}"
                    val code = template.format(it.second)
                    codes = codes.plus(code)
                    doCodeTest(code)
                }
            } +
                { println(codes.joinToString("\n")) },
        )
    }

    /**
     * @return path to test data file directory relative to root of this module.
     */
    override fun getTestDataPath(): String = "src/test/resources/testdata/set"

    override fun includeRanges(): Boolean = true
}
