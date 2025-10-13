/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin

import com.intellij.testFramework.ParsingTestCase
import org.junit.Ignore
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance

@Ignore("Run with JUnit 5")
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
abstract class ParameterizedBaseTestCase : ParsingTestCase("", "vto", VentoParserDefinition()) {
    companion object {
        var output = ""

        @AfterAll
        @JvmStatic
        fun atEndOfAll() {
            println(output)
        }
    }

    @BeforeEach
    fun setUpTest() {
        setUp()
    }

    @AfterEach
    fun tearDownTest() {
        tearDown()
    }

    @Suppress("unused")
    fun assertSet(set: Pair<String, String>, filenamePrefix: String, template: String) {
        this.name = "$filenamePrefix-${set.first}"
        val code = template.format(set.second)
        output += code + "\n"
        doCodeTest(code)
    }

    /**
     * @return path to test data file directory relative to root of this module.
     */
    override fun getTestDataPath(): String = "src/test/resources/testdata/set"

    override fun includeRanges(): Boolean = true
}
