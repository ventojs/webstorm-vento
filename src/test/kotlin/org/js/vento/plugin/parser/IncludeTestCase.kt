/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.testFramework.ParsingTestCase
import org.js.vento.plugin.VentoParserDefinition

class IncludeTestCase : ParsingTestCase("", "vto", VentoParserDefinition()) {
    fun testIncludeFilenameString() = doCodeTest("""{{ include "filename.vto" }}""".trimIndent())

    fun testIncludeFilenameVariable() = doCodeTest("""{{ include filename }}""".trimIndent())

    fun testIncludeLookup() = doCodeTest("""{{ include resolve( pathname, true) }}""".trimIndent())

    fun testIncludeLookupWithObject() = doCodeTest("""{{ include resolve({ path: "./section.vto" }) }}""".trimIndent())

    fun testIncludeWithData() = doCodeTest("""{{ include "myfile.vto" {salutation: "Good bye"} }}""".trimIndent())

    fun testIncludeWithDataRef() = doCodeTest("""{{ include "myfile.vto" data }}""".trimIndent())

    fun testIncludeWithDataAndPipe() =
        doCodeTest("""{{ include "myfile.vto" {salutation: "Good bye"} |> toUpperCase }}""".trimIndent())

    fun testBrockenInclude() =
        doCodeTest("""{{ include "myfile.vto" |> toUpperCase {salutation: "Good bye"} }}""".trimIndent())

    /**
     * @return path to test data file directory relative to root of this module.
     */
    override fun getTestDataPath(): String = "src/test/resources/testdata/include"

    override fun includeRanges(): Boolean = true
}
