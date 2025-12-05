/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.lexer

class FrontmatterTestCase(name: String) : BaseLexerTestCase(name) {
    fun `test simple frontmatter `() {
        lexAndTest(
            """---
title: Hello World!
---""",
            arrayOf(
                "---",
                "title",
                ":",
                " Hello World!",
                "---",
            ),
        )
    }

    fun `test frontmatter with flag `() {
        lexAndTest(
            """
            ---
            flags:
              - published
            ---
            """.trimIndent(),
            arrayOf(
                "---",
                "flags",
                ":",
                "  - published",
                "---",
            ),
        )
    }

    fun `test frontmatter complex `() {
        lexAndTest(
            """
            ---
            title: Hello World!
            author: John Doe
            flags:
              - published
              - final
            ---
            """.trimIndent(),
            arrayOf(
                "---",
                "title",
                ":",
                " Hello World!",
                "author",
                ":",
                " John Doe",
                "flags",
                ":",
                "  - published",
                "  - final",
                "---",
            ),
        )
    }

    fun `test frontmatter with html and vento block `() {
        lexAndTest(
            """
            ---
            title: Hello World!
            author: John Doe
            flags:
              - published
              - final
            ---
            <html>
            <body>
            <p>Hello {{ name }}!</p>
            </body>
            </html>
            """.trimIndent(),
            arrayOf(
                "---",
                "title",
                ":",
                " Hello World!",
                "author",
                ":",
                " John Doe",
                "flags",
                ":",
                "  - published",
                "  - final",
                "---",
                "\n<html>\n<body>\n<p>Hello ",
                "{{",
                "name",
                "}}",
                "!</p>\n</body>\n</html>",
            ),
        )
    }
}
