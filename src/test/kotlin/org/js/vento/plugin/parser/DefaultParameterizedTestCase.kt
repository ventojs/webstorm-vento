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
class DefaultParameterizedTestCase : ParameterizedBaseTestCase() {
    companion object {
        @Suppress("unused")
        val expressions = expressionSet
    }

    @ParameterizedTest
    @FieldSource("expressions")
    fun testWithExpressionDefault(exp: Pair<String, String>) {
        assertSet(exp, "exp-default", "{{ default myVar = %s }}")
    }

    @ParameterizedTest
    @FieldSource("expressions")
    fun testWithExpressionDefaultWithPipe(exp: Pair<String, String>) {
        assertSet(exp, "exp-default-with-pipe", "{{ default myVar = %s |> JSON.stringify }}")
    }

    override fun getTestDataPath(): String = "src/test/resources/testdata/default"
}
