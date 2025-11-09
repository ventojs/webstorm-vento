/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.lexer

class ObjectVariableTestCase(name: String) : BaseLexerTestCase(name) {
    fun `test empty block`() = lexAndTest("{{}}", arrayOf("{{", "}}"))

    fun `test empty object`() = lexAndTest("{{{}}}", arrayOf("{{", "{", "}", "}}"))

    fun `test empty object with spaces`() = lexAndTest("{{ {} }}", arrayOf("{{", "{", "}", "}}"))

    fun `test object with value shorthand property`() = lexAndTest("{{ {a} }}", arrayOf("{{", "{", "a", "}", "}}"))

    fun `test empty object of empty object`() = lexAndTest("{{{aa:{}}}}", arrayOf("{{", "{", "aa", ":", "{", "}", "}", "}}"))

    fun `test object with key and value`() = lexAndTest("{{ {a:1} }}", arrayOf("{{", "{", "a", ":", "1", "}", "}}"))

    fun `test object with key and array`() = lexAndTest("{{ {a:[]} }}", arrayOf("{{", "{", "a", ":", "[", "]", "}", "}}"))

    fun `test object with key and array of numbers`() =
        lexAndTest("{{ {a:[1,2,3]} }}", arrayOf("{{", "{", "a", ":", "[", "1", ",", "2", ",", "3", "]", "}", "}}"))

    fun `test object with key and array of strings`() =
        lexAndTest(
            "{{ {a:['a','b','c']} }}",
            arrayOf("{{", "{", "a", ":", "[", "'", "a", "'", ",", "'", "b", "'", ",", "'", "c", "'", "]", "}", "}}"),
        )

    fun `test object with string keys and string values`() =
        lexAndTest(
            "{{ {\"a\":\"aaa\", \"bb\":\"bb\", \"ccc\":\"c\"} }}",
            arrayOf(
                "{{",
                "{",
                "\"",
                "a",
                "\"",
                ":",
                "\"",
                "aaa",
                "\"",
                ",",
                "\"",
                "bb",
                "\"",
                ":",
                "\"",
                "bb",
                "\"",
                ",",
                "\"",
                "ccc",
                "\"",
                ":",
                "\"",
                "c",
                "\"",
                "}",
                "}}",
            ),
        )

    fun `test object with key and array of objects`() =
        lexAndTest(
            "{{ {aaa:[{bbb:'1'},{bbb:'2'}]} }}",
            arrayOf(
                "{{",
                "{",
                "aaa",
                ":",
                "[",
                "{",
                "bbb",
                ":",
                "'",
                "1",
                "'",
                "}",
                ",",
                "{",
                "bbb",
                ":",
                "'",
                "2",
                "'",
                "}",
                "]",
                "}",
                "}}",
            ),
        )

    fun `test object of objects`() =
        lexAndTest(
            "{{ {a:{bb:12,ccc:\"abc\"}} }}",
            arrayOf("{{", "{", "a", ":", "{", "bb", ":", "12", ",", "ccc", ":", "\"", "abc", "\"", "}", "}", "}}"),
        )

    fun `test object with key and array of objects with error`() =
        lexAndTest(
            "{{ {aaa:[{bbb:'1',{bbb:'2'}]} }}",
            arrayOf(
                "{{",
                "{",
                "aaa",
                ":",
                "[",
                "{",
                "bbb",
                ":",
                "'",
                "1",
                "'",
                ",",
                "{",
                "bbb",
                ":",
                "'",
                "2",
                "'",
                "}",
                "]",
                "}",
                "}",
                "}",
            ),
            false,
        )

    fun `test object with invalid javascript syntax`() =
        lexAndTest(
            "{{ {let x = 1;} }}",
            arrayOf("{{", "{", "let", "x", "=", "1", ";", "}", "}}"),
            false,
        )

    fun `test object with missing closing brace`() =
        lexAndTest(
            "{{ {key: 'value' }}",
            arrayOf("{{", "{", "key", ":", "'", "value", "'", "}", "}"),
            false,
        )

    fun `test object with invalid key format`() =
        lexAndTest(
            "{{ {123key: 'value'} }}",
            arrayOf("{{", "{", "123key", ":", "'", "value", "'", "}", "}}"),
            false,
        )

    fun `test object with missing colon`() =
        lexAndTest(
            "{{ {key 'value'} }}",
            arrayOf("{{", "{", "key", "'", "value", "'", "}", "}}"),
            false,
        )
}
