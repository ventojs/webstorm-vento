/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.lexer

/**
 * Test suite for Vento "for" loop lexer functionality.
 *
 * This test suite validates the lexical analysis of Vento "for" loop syntax,
 * ensuring correct tokenization across various scenarios including:
 * - Basic iteration over arrays and objects
 * - Complex data structures (nested objects, object arrays)
 * - Expression-based iteration (method chaining, pipes)
 * - Complete for blocks with opening and closing tags
 * - Error handling for malformed syntax
 */
@Suppress("ktlint:standard:blank-line-before-declaration")
class ForTestCase(name: String) : BaseLexerTestCase(name) {
    // =============================================================================
    // Basic Iteration - Arrays and Objects
    // =============================================================================

    /**
     * Tests basic iteration over an array literal.
     * Syntax: {{ for variable of [elements] }}
     */
    fun `test for array`() =
        lexAndTest(
            "{{ for number of [1, 2, 3] }}",
            arrayOf(
                "{{",
                "for",
                "number",
                "of",
                "[",
                "1",
                ",",
                "2",
                ",",
                "3",
                "]",
                "}}",
            ),
        )

    /**
     * Tests iteration over an object literal with key-value destructuring.
     * Syntax: {{ for key,value of {key:value} }}
     */
    fun `test for object`() =
        lexAndTest(
            "{{ for key,value of {a:1,b:2} }}",
            arrayOf(
                "{{",
                "for",
                "key",
                ",",
                "value",
                "of",
                "{",
                "a",
                ":",
                "1",
                ",",
                "b",
                ":",
                "2",
                "}",
                "}}",
            ),
        )

    // =============================================================================
    // Complex Data Structures
    // =============================================================================

    /**
     * Tests iteration over an array containing object literals.
     * Syntax: {{ for variable of [{...}] }}
     */
    fun `test for object array`() =
        lexAndTest(
            "{{ for value of [{a:1},{a:2}] }}",
            arrayOf(
                "{{",
                "for",
                "value",
                "of",
                "[",
                "{",
                "a",
                ":",
                "1",
                "}",
                ",",
                "{",
                "a",
                ":",
                "2",
                "}",
                "]",
                "}}",
            ),
        )

    /**
     * Tests iteration over an array of objects with index and value destructuring.
     * Syntax: {{ for index, value of [...] }}
     */
    fun `test for object array with index`() =
        lexAndTest(
            "{{ for index, value of [{a:1},{a:2}] }}",
            arrayOf(
                "{{",
                "for",
                "index",
                ",",
                "value",
                "of",
                "[",
                "{",
                "a",
                ":",
                "1",
                "}",
                ",",
                "{",
                "a",
                ":",
                "2",
                "}",
                "]",
                "}}",
            ),
        )

    fun `test for string array`() =
        lexAndTest(
            """{{ for value of ["a"+"b"+"c","hello"] }}""",
            arrayOf(
                "{{",
                "for",
                "value",
                "of",
                "[",
                "\"",
                "a",
                "\"",
                "+",
                "\"",
                "b",
                "\"",
                "+",
                "\"",
                "c",
                "\"",
                ",",
                "\"",
                "hello",
                "\"",
                "]",
                "}}",
            ),
        )

    /**
     * Tests iteration with complex destructuring pattern using index and object pattern.
     * Syntax: {{ for index, {name, value} of items }}
     * Validates correct tokenization of combined index and object destructuring.
     */
    fun `test for with many values`() =
        lexAndTest(
            "{{ for index, {name, value} of items }}",
            arrayOf(
                "{{",
                "for",
                "index",
                ",",
                "{",
                "name",
                ",",
                "value",
                "}",
                "of",
                "items",
                "}}",
            ),
        )

    /**
     * Tests iteration using array destructuring pattern.
     * Syntax: {{ for [name, value] of items }}
     * Validates correct tokenization of array destructuring syntax.
     */
    fun `test for with array values`() =
        lexAndTest(
            "{{ for [name, value] of items }}",
            arrayOf(
                "{{",
                "for",
                "[",
                "name",
                ",",
                "value",
                "]",
                "of",
                "items",
                "}}",
            ),
        )

    /**
     * Tests iteration with nested array destructuring pattern.
     * Syntax: {{ for [[n]] of [[[1]], [[2]]] }}
     * Validates correct tokenization of deeply nested array structures.
     */
    fun `test for with x`() =
        lexAndTest(
            "{{ for [[n]] of [[[1]], [[2]]] }}",
            arrayOf(
                "{{",
                "for",
                "[",
                "[",
                "n",
                "]",
                "]",
                "of",
                "[",
                "[",
                "[",
                "1",
                "]",
                "]",
                ",",
                "[",
                "[",
                "2",
                "]",
                "]",
                "]",
                "}}",
            ),
        )

    /**
     * Tests iteration over a nested object structure within a complete for block.
     * Validates correct tokenization of nested braces.
     */
    fun `test for of object`() =
        lexAndTest(
            """
{{ for item of {a:1,b:{c:2}} }}
{{ /for }}
            """.trimMargin(),
            arrayOf(
                "{{",
                "for",
                "item",
                "of",
                "{",
                "a",
                ":",
                "1",
                ",",
                "b",
                ":",
                "{",
                "c",
                ":",
                "2",
                "}",
                "}",
                "}}",
                "\n",
                "{{",
                "/for",
                "}}",
            ),
        )

    /**
     * Tests lexer behavior with malformed nested object (missing closing brace).
     * Validates error recovery and tokenization of incomplete syntax.
     */
    fun `test for of broken object`() =
        lexAndTest(
            """
            |{{ for item of {a:1,b:{c:2} }}
            |{{ /for }}
            """.trimMargin(),
            arrayOf(
                "{{",
                "for",
                "item",
                "of",
                "{",
                "a",
                ":",
                "1",
                ",",
                "b",
                ":",
                "{",
                "c",
                ":",
                "2",
                "}",
                "}",
                "}",
                "{{",
                "/for",
                "}}",
            ),
            false,
        )

    // =============================================================================
    // Expression-Based Iteration
    // =============================================================================

    /**
     * Tests iteration over the result of a chained method call.
     * Syntax: {{ for variable of array.method() }}
     */
    fun `test for expression`() =
        lexAndTest(
            "{{ for odd_number of [1, 2, 3].filter((n) => n%2) }}",
            arrayOf(
                "{{",
                "for",
                "odd_number",
                "of",
                "[",
                "1",
                ",",
                "2",
                ",",
                "3",
                "]",
                ".",
                "filter",
                "(",
                "(",
                "n",
                ")",
                "=>",
                "n",
                "%",
                "2",
                ")",
                "}}",
            ),
        )

    /**
     * Tests iteration with pipe operator for functional transformations.
     * Syntax: {{ for variable of array |> function() }}
     */
    fun `test for with pipe`() =
        lexAndTest(
            "{{ for even_number of [1, 2, 3] |> filter((n) => n % 2 === 0) }}",
            arrayOf(
                "{{",
                "for",
                "even_number",
                "of",
                "[",
                "1",
                ",",
                "2",
                ",",
                "3",
                "]",
                "|>",
                "filter",
                "(",
                "(",
                "n",
                ")",
                "=>",
                "n",
                "%",
                "2",
                "===",
                "0",
                ")",
                "}}",
            ),
        )

    // =============================================================================
    // Complete For Blocks
    // =============================================================================

    /**
     * Tests a complete for block with opening and closing tags.
     * Validates proper tokenization of variable references and block structure.
     */
    fun `test complete for`() =
        lexAndTest(
            """
            |{{ for item of items }}
            |{{ /for }}
            """.trimMargin(),
            arrayOf("{{", "for", "item", "of", "items", "}}", "\n", "{{", "/for", "}}"),
        )

    /**
     * Tests closing for tag with standard spacing.
     * Syntax: {{ /for }}
     */
    fun `test close for`() = lexAndTest("{{ /for }}", arrayOf("{{", "/for", "}}"))

    // =============================================================================
    // Error Cases - Malformed Syntax
    // =============================================================================

    /**
     * Tests missing loop variable before "of" keyword.
     * Invalid syntax: {{ for of collection }}
     */
    fun `test for of with missing value `() = lexAndTest("{{ for of values }}", arrayOf("{{", "for", "of", "values", "}}"))

    /**
     * Tests missing "of" keyword between variable and collection.
     * Invalid syntax: {{ for variable collection }}
     */
    fun `test for of with missing of `() = lexAndTest("{{ for value values }}", arrayOf("{{", "for", "value", "values", "}}"))

    /**
     * Tests missing "of" keyword between variable and collection.
     * Invalid syntax: {{ for variable collection }}
     */
    fun `test for missing space`() =
        lexAndTest("{{ forletter of \"abcd\" }}", arrayOf("{{", "f", "orletter", "of", "\"", "abcd", "\"", "}}"), false)

    /**
     * Tests malformed closing tag with incorrect keyword.
     * Invalid syntax: {{ /fr }} instead of {{ /for }}
     */
    fun `test broken closing for`() = lexAndTest("{{ /fr }}", arrayOf("{{", "/", "fr }}"), false)

    /**
     * Tests malformed closing tag without spacing.
     * Invalid syntax: {{/fr}} instead of {{ /for }}
     */
    fun `test trimmed broken closing for`() = lexAndTest("{{/fr}}", arrayOf("{{", "/", "fr}}"), false)
}
