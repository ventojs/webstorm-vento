/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.lexer

class ArrayVariableTestCase(name: String) : BaseLexerTestCase(name, true) {
    fun `test empty object`() = lexAndTest("{{[]}}", arrayOf("{{", "[", "]", "}}"))

    fun `test empty object with spaces`() = lexAndTest("{{ [] }}", arrayOf("{{", "[", "]", "}}"))

    fun `test object with value shorthand property`() = lexAndTest("{{ [a] }}", arrayOf("{{", "[", "a", "]", "}}"))

    fun `test object with value shorthand property with spaces`() = lexAndTest("{{ [ a ] }}", arrayOf("{{", "[", "a", "]", "}}"))

    fun `test object with multiple values shorthand property with spaces`() =
        lexAndTest("{{ [ a, b, c ] }}", arrayOf("{{", "[", "a", ",", "b", ",", "c", "]", "}}"))

    fun `test object with multiple values shorthand property`() =
        lexAndTest("{{ [a,b,c] }}", arrayOf("{{", "[", "a", ",", "b", ",", "c", "]", "}}"))

    fun `test array with numeric values`() = lexAndTest("{{ [1, 2, 3] }}", arrayOf("{{", "[", "1", ",", "2", ",", "3", "]", "}}"))

    fun `test array with string values`() =
        lexAndTest("{{ ['a', 'b', 'c'] }}", arrayOf("{{", "[", "'", "a", "'", ",", "'", "b", "'", ",", "'", "c", "'", "]", "}}"))

    fun `test array with mixed types`() =
        lexAndTest("{{ [1, 'a', true] }}", arrayOf("{{", "[", "1", ",", "'", "a", "'", ",", "true", "]", "}}"))

    fun `test array with nested arrays`() =
        lexAndTest("{{ [1, [2, 3], 4] }}", arrayOf("{{", "[", "1", ",", "[", "2", ",", "3", "]", ",", "4", "]", "}}"))

    fun `test array with object literals`() =
        lexAndTest("{{ [{a: 1}, {b: 2}] }}", arrayOf("{{", "[", "{", "a", ":", "1", "}", ",", "{", "b", ":", "2", "}", "]", "}}"))

    fun `test array with function calls`() =
        lexAndTest("{{ [foo(), bar()] }}", arrayOf("{{", "[", "foo", "(", ")", ",", "bar", "(", ")", "]", "}}"))

    fun `test array with expressions`() =
        lexAndTest("{{ [1 + 2, x * y] }}", arrayOf("{{", "[", "1", "+", "2", ",", "x", "*", "y", "]", "}}"))

    fun `test array with array literals`() =
        lexAndTest("{{ [[1, 2], [3, 4]] }}", arrayOf("{{", "[", "[", "1", ",", "2", "]", ",", "[", "3", ",", "4", "]", "]", "}}"))

    fun `test array with array expressions`() =
        lexAndTest("{{ [[a + b], [c * d]] }}", arrayOf("{{", "[", "[", "a", "+", "b", "]", ",", "[", "c", "*", "d", "]", "]", "}}"))

    fun `test deeply nested arrays`() =
        lexAndTest("{{ [[[1]], [[2]]] }}", arrayOf("{{", "[", "[", "[", "1", "]", "]", ",", "[", "[", "2", "]", "]", "]", "}}"))

    fun `test array with mixed array types`() =
        lexAndTest(
            "{{ [[1, 'a'], [true, null]] }}",
            arrayOf("{{", "[", "[", "1", ",", "'", "a", "'", "]", ",", "[", "true", ",", "null", "]", "]", "}}"),
        )

    fun `test array with sparse elements`() =
        lexAndTest("{{ [,,[1,2],,] }}", arrayOf("{{", "[", ",", ",", "[", "1", ",", "2", "]", ",", ",", "]", "}}"))

    fun `test unclosed array`() = lexAndTest("{{ [ }}", arrayOf("{{", "[", "}}"), false)

    fun `test unclosed array with content`() = lexAndTest("{{ [1, 2, 3 }}", arrayOf("{{", "[", "1", ",", "2", ",", "3", "}}"), false)

    fun `test unclosed nested array`() = lexAndTest("{{ [1, [2, 3 }}", arrayOf("{{", "[", "1", ",", "[", "2", ",", "3", "}}"), false)
}
