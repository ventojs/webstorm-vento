/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser.injectors

import org.js.vento.plugin.parser.ParsingTestCase

class VentoVariableExtractorTest : ParsingTestCase() {
    fun testForPlainValue() {
        val names = VentoVariableExtractor.collectAllVariableNames(parse("{{ for item of items }}"))
        assertEquals(listOf("item"), names)
    }

    fun testForKeyValue() {
        val names = VentoVariableExtractor.collectAllVariableNames(parse("{{ for key, value of items }}"))
        assertEquals(listOf("key", "value"), names)
    }

    fun testForArrayDestructured() {
        val names = VentoVariableExtractor.collectAllVariableNames(parse("{{ for [a, b] of items }}"))
        assertEquals(listOf("a", "b"), names)
    }

    fun testForNestedArrayDestructured() {
        val names = VentoVariableExtractor.collectAllVariableNames(parse("{{ for [[n]] of items }}"))
        assertEquals(listOf("n"), names)
    }

    fun testSetBlockForm() {
        val names = VentoVariableExtractor.collectAllVariableNames(parse("{{ set foo }}\n{{ /set }}"))
        assertEquals(listOf("foo"), names)
    }

    fun testSetInlineFormExcludesRhs() {
        val names = VentoVariableExtractor.collectAllVariableNames(parse("{{ set foo = bar }}"))
        assertEquals(listOf("foo"), names)
    }

    fun testDefaultBlockForm() {
        val names = VentoVariableExtractor.collectAllVariableNames(parse("{{ default foo }}\n{{ /default }}"))
        assertEquals(listOf("foo"), names)
    }

    fun testImportPlain() {
        val names = VentoVariableExtractor.collectAllVariableNames(parse("{{ import { a, b } from \"file.js\" }}"))
        assertEquals(listOf("a", "b"), names)
    }

    fun testImportAliased() {
        val names = VentoVariableExtractor.collectAllVariableNames(parse("{{ import { a as x, b } from \"file.js\" }}"))
        assertEquals(listOf("x", "b"), names)
    }

    fun testCombinedAndDeduplicated() {
        val names =
            VentoVariableExtractor.collectAllVariableNames(
                parse(
                    """
                    {{ import { helper } from "helpers.js" }}
                    {{ for item of items }}
                    {{ item }}
                    {{ /for }}
                    {{ for item of otherItems }}
                    {{ /for }}
                    """.trimIndent(),
                ),
            )
        assertEquals(setOf("helper", "item"), names.toSet())
        assertEquals(2, names.size)
    }

    private fun parse(code: String) = createPsiFile("a", code)

    override fun getTestDataPath(): String = "src/test/resources/testdata"
}
