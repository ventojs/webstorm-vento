/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.lexer

class EchoTestCase(name: String) : BaseLexerTestCase(name) {
    fun `test echo`() {
        lexAndTest(
            "{{ echo }} Hello World {{ /echo }}",
            arrayOf("{{", "echo", "}}", " Hello World ", "{{", "/echo", "}}"),
        )
    }

    fun `test echo string`() {
        lexAndTest(
            "{{ echo \"Hello World\" }}",
            arrayOf("{{", "echo", "\"", "Hello World", "\"", "}}"),
        )
    }

    fun `test echo with pipe`() {
        lexAndTest(
            "{{ echo |> md }}\n" +
                "\n## Header\n" +
                "\n" +
                "- First item.\n" +
                "- Second item.\n" +
                "{{ /echo }}",
            arrayOf(
                "{{",
                "echo",
                "|>",
                "md",
                "}}",
                "\n\n## Header\n\n- First item.\n- Second item.\n",
                "{{",
                "/echo",
                "}}",
            ),
        )
    }
}
