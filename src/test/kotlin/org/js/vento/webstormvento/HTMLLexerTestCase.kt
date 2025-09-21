/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.webstormvento

class HTMLLexerTestCase(name: String) : BaseLexerTestCase(name) {
    fun `test lexing html `() {
        lexAndTest("hello <span>world</span>", arrayOf("hello <span>world</span>"))
    }

    fun `test lexing page `() {
        lexAndTest(
"""<html>
    <head>
        {{# comment #}}
    </head>
    <body>
        <h2>Hello {{ username || "unknown" }}! </h2>
    </body>
</html>

""",
            arrayOf(
"""<html>
    <head>
        """,
                "{{#",
                " comment ",
                "#}}",
"""
    </head>
    <body>
        <h2>Hello """,
                "{{",
                " username ",
                "||",
                " \"unknown\" ",
                "}}",
"""! </h2>
    </body>
</html>

""",
            ),
        )
    }
}
