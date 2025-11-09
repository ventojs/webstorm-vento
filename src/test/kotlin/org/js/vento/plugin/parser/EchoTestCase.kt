/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.testFramework.ParsingTestCase
import org.js.vento.plugin.VentoParserDefinition

class EchoTestCase : ParsingTestCase("", "vto", VentoParserDefinition()) {
    fun `test echo`() = doCodeTest("{{ echo }} Hello World {{ /echo }}")

    fun `test echo string`() = doCodeTest("{{ echo \"Hello World\" }}")

    fun `test echo with pipe`() =
        doCodeTest(
            "{{ echo |> md }}\n" +
                "\n## Header\n" +
                "\n" +
                "- First item.\n" +
                "- Second item.\n" +
                "{{ /echo }}",
        )

    override fun getTestDataPath(): String = "src/test/resources/testdata/echo"

    override fun includeRanges(): Boolean = true
}
