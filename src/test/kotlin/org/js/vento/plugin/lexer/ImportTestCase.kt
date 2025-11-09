/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.lexer

@Suppress("ktlint:standard:blank-line-before-declaration")
class ImportTestCase(name: String) : BaseLexerTestCase(name) {
    fun `test simple import`() =
        lexAndTest(
            "{{ import { foo } from \"./afile.vto\" }}",
            arrayOf("{{", "import", "{ foo }", "from", "\"./afile.vto\"", "}}"),
        )

    fun `test multiple imports`() =
        lexAndTest(
            "{{ import { foo as f, bar,    _,\t$ } from \"./afile.vto\" }}",
            arrayOf("{{", "import", "{ foo as f, bar,    _,\t$ }", "from", "\"./afile.vto\"", "}}"),
        )

    fun `test simple import error`() =
        lexAndTest(
            "{{ import { foo  from \"./afile.vto\" }}",
            arrayOf("{{", "import", "{ foo", "from", "\"./afile.vto\"", "}}"),
            false,
        )

    fun `test bare import`() =
        lexAndTest(
            "{{ import foo from \"./afile.vto\" }}",
            arrayOf("{{", "import", "foo", "from", "\"./afile.vto\"", "}}"),
        )

    fun `test no value import`() =
        lexAndTest(
            "{{ import from \"./afile.vto\" }}",
            arrayOf("{{", "import", "from", "\"./afile.vto\"", "}}"),
        )

    fun `test no file import`() =
        lexAndTest(
            "{{ import foo from  }}",
            arrayOf("{{", "import", "foo", "from", "}}"),
        )

    fun `test no from`() =
        lexAndTest(
            "{{ import foo \"./afile.vto\" }}",
            arrayOf("{{", "import", "foo", "\"./afile.vto\"", "}}"),
        )

    fun `test renamed import`() =
        lexAndTest(
            "{{ import { foo as bar } from \"baz\" }}",
            arrayOf("{{", "import", "{ foo as bar }", "from", "\"baz\"", "}}"),
        )
}
