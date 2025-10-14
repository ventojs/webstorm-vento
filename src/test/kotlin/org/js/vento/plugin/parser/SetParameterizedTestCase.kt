/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import org.js.vento.plugin.ParameterizedBaseTestCase
import org.junit.Ignore
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.FieldSource

@Ignore("Run with JUnit 5")
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class SetParameterizedTestCase : ParameterizedBaseTestCase() {
    companion object {
        @Suppress("unused")
        val expressions = expressionSet
    }

    @ParameterizedTest
    @FieldSource("expressions")
    fun testWithExpressionSet(exp: Pair<String, String>) {
        assertSet(exp, "exp-set", "{{ set myVar = %s }}")
    }

    @ParameterizedTest
    @FieldSource("expressions")
    fun testWithExpressionSetWithPipe(exp: Pair<String, String>) {
        assertSet(exp, "exp-set-with-pipe", "{{ set myVar = %s |> JSON.stringify }}")
    }

    override fun getTestDataPath(): String = "src/test/resources/testdata/set"
}
