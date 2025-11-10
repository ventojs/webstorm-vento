/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.lexer

/**
 * Tests for variable expression lexing.
 *
 * The tests are grouped and ordered from simple to complex to make intent clear and maintenance easier.
 * Each test includes a short description of the scenario and the expected tokenization.
 */
@Suppress("ktlint:standard:blank-line-before-declaration")
class VariableTestCase(name: String) : BaseLexerTestCase(name) {
    // ---------------------------
    // Basics: literals & variables
    // ---------------------------

    /**
     * Verifies that the undefined literal is recognized as a single identifier within delimiters.
     * Input: {{ undefined }}
     * Expected: {{, undefined, }}
     */
    fun `test undefined`() = lexAndTest("{{ undefined }}", arrayOf("{{", "undefined", "}}"))

    /**
     * Verifies that the null literal is recognized within delimiters.
     * Input: {{ null }}
     * Expected: {{, null, }}
     */
    fun `test null`() = lexAndTest("{{ null }}", arrayOf("{{", "null", "}}"))

    /**
     * Verifies a simple variable name inside delimiters.
     * Input: {{ variable }}
     * Expected: {{, variable, }}
     */
    fun `test lexing variables`() = lexAndTest("{{ variable }}", arrayOf("{{", "variable", "}}"))

    /**
     * Verifies a simple string literal inside delimiters.
     * Input: {{ "Hello World" }}
     * Expected: {{, ", Hello World, ", }}
     */
    fun `test string variable`() = lexAndTest("{{ \"Hello World\" }}", arrayOf("{{", "\"", "Hello World", "\"", "}}"))

    /**
     * Verifies trimming markers - and -}} around a string expression.
     * Input: {{- "Hello World" -}}
     * Expected: {{-, ", Hello World, ", -}}
     */
    fun `test with trimming`() = lexAndTest("{{- \"Hello World\" -}}", arrayOf("{{-", "\"", "Hello World", "\"", "-}}"))

    // ---------------------------
    // Operators
    // ---------------------------

    /**
     * Verifies logical-or with a default string value.
     * Input: {{ variable || "default" }}
     * Expected: {{, variable, ||, ", default, ", }}
     */
    fun `test with or`() =
        lexAndTest(
            "{{ variable || \"default\" }}",
            arrayOf("{{", "variable", "||", "\"", "default", "\"", "}}"),
        )

    /**
     * Verifies ternary conditional operator tokenization.
     * Input: {{ message ? "yes" : "no" }}
     * Expected: {{, message, ?, ", yes, ", :, ", no, ", }}
     */
    fun `test with ternary operator`() =
        lexAndTest(
            "{{ message ? \"yes\" : \"no\" }}",
            arrayOf("{{", "message", "?", "\"", "yes", "\"", ":", "\"", "no", "\"", "}}"),
        )

    fun `test ternary operator`() =
        lexAndTest(
            "{{ foo ? bar : baz }}",
            arrayOf("{{", "foo", "?", "bar", ":", "baz", "}}"),
        )

    fun `test await call`() =
        lexAndTest(
            "{{ await  Promise.resolve(\"Hello, world!\") }}",
            arrayOf(
                "{{",
                "await",
                "Promise",
                ".",
                "resolve",
                "(",
                "\"",
                "Hello, world!",
                "\"",
                ")",
                "}}",
            ),
        )

    // ---------------------------
    // Strings, arrays, regex, literals
    // ---------------------------

    /**
     * Verifies that a closing delimiter sequence inside a quoted string is handled as text, not as a delimiter.
     * Input: {{ "}}" }}
     * Expected: {{, ", }}, ", }}
     */
    fun `test moustache string`() = lexAndTest("{{ \"}}\" }}", arrayOf("{{", "\"", "}}", "\"", "}}"))

    /**
     * Verifies complex quoting/escaping within a string.
     * Input: {{ "'\"'" }}
     * Expected: {{, ", ', \\", ', ", }}
     */
    fun `test complex string`() =
        lexAndTest(
            """{{ "'\"'" }}""",
            arrayOf("{{", "\"", "'\\\"'", "\"", "}}"),
        )

    /**
     * Verifies array literal tokenization when used as an expression.
     * Input: {{ [1,2,3] }}
     * Expected: {{, [1,2,3], }}
     */
    fun `test array`() =
        lexAndTest(
            """{{ [1,2,3] }}""",
            arrayOf(
                "{{",
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
     * Verifies regex literal and method call tokenization including brackets and quotes.
     * Input: {{ !/[/\"}]/.test('foo/bar') }}
     * Expected: {{, !, /, [, /\"}, ], /, .test(, ', foo/bar, ', ), }}
     */
    fun `test regex`() =
        lexAndTest(
            "{{ !/[/\"}]/.test('foo/bar') }}",
            arrayOf(
                "{{",
                "!",
                "/",
                "[",
                "/\"}",
                "]",
                "/",
                ".",
                "test",
                "(",
                "'",
                "foo/bar",
                "'",
                ")",
                "}}",
            ),
        )

    /**
     * Verifies template/string literal tokenization with nested escaped substitutions.
     * Input: {{ html`foo \${bar} \${baz}` }}
     * Expected: {{, html, `, foo \${bar \${baz}, `, }}
     */
    fun `test string literal`() =
        lexAndTest(
            "{{ html`foo \${bar} \${baz}` }}",
            arrayOf(
                "{{",
                "html",
                "`",
                "foo ",
                "\$",
                "{bar} ",
                "\$",
                "{baz}",
                "`",
                "}}",
            ),
        )

    // ---------------------------
    // Objects and JSON
    // ---------------------------

    /**
     * Verifies a simple inline object literal.
     * Input: {{{a:1,b:2}}}
     * Expected: {{, {, a:1,b:2, }, }}
     */
    fun `test object`() =
        lexAndTest(
            "{{{a:1,b:2}}}",
            arrayOf(
                "{{",
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

    /**
     * Verifies nested object literals.
     * Input: {{{a:1,b:{c:2}}}}
     * Expected: {{, {, a:1,b:, {, c:2, }, }, }}
     */
    fun `test deep object`() =
        lexAndTest(
            "{{{a:1,b:{c:2}}}}",
            arrayOf(
                "{{",
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
            ),
        )

    /**
     * Verifies an object with a string property value.
     * Input: {{{a:1,b:"abc"}}}
     * Expected: {{, {, a:1,b:, ", abc, ", }, }}
     */
    fun `test object with string`() =
        lexAndTest(
            "{{{a:1,b:\"abc\"}}}",
            arrayOf(
                "{{",
                "{",
                "a",
                ":",
                "1",
                ",",
                "b",
                ":",
                "\"",
                "abc",
                "\"",
                "}",
                "}}",
            ),
        )

    /**
     * Verifies an object containing a value with dash characters that could affect delimiter parsing.
     * Input: {{{a:1,b:foo--}}}
     * Expected: {{, {, a:1,b:foo--, }, }}
     */
    fun `test object with dashes`() =
        lexAndTest(
            "{{{a:1,b:foo--}}}",
            arrayOf(
                "{{",
                "{",
                "a",
                ":",
                "1",
                ",",
                "b",
                ":",
                "foo",
                "-",
                "-",
                "}",
                "}}",
            ),
        )

    /**
     * Verifies JSON-like structure tokenization where keys are quoted.
     * Input: {{{"a":1,"b":"abc"}}}
     * Expected: {{, {, ", a, ", :1,, ", b, ", :, ", abc, ", }, }}
     */
    fun `test JSON`() =
        lexAndTest(
            "{{{\"a\":1,\"b\":\"abc\"}}}",
            arrayOf(
                "{{",
                "{",
                "\"",
                "a",
                "\"",
                ":",
                "1",
                ",",
                "\"",
                "b",
                "\"",
                ":",
                "\"",
                "abc",
                "\"",
                "}",
                "}}",
            ),
        )

    // ---------------------------
    // Edge cases with dashes and trimming
    // ---------------------------

    /**
     * Verifies tokenization when an identifier is followed by a dash and a closing trimming delimiter.
     * Input: {{ foo--}}
     * Expected: {{, foo, -, -}}
     */
    fun `test dashes`() = lexAndTest("{{ foo---}}", arrayOf("{{", "foo", "-", "-", "-}}"))

    /**
     * Verifies tokenization when opening trimming and multiple trailing dashes are present.
     * Input: {{-foo---}}
     * Expected: {{-, foo, -, -, -}}
     */
    fun `test many dashes`() =
        lexAndTest(
            "{{-foo---}}",
            arrayOf(
                "{{-",
                "foo",
                "-",
                "-",
                "-}}",
            ),
        )

    // ---------------------------
    // Complex scenario
    // ---------------------------

    /**
     * Verifies a multi-line scenario combining a directive, arithmetic text, and a trimmed negative variable.
     * Input:
     *   {{> let foo = -2 }}
     *   1 + 1 = {{--foo}}
     * Expected: {{>,  let foo = -2 , }, \n1 + 1 = , {{-, -, foo, }}
     */
    fun `test complex`() {
        lexAndTest(
            "{{> let foo = -2 }}\n1 + 1 = {{--foo}}",
            arrayOf(
                "{{>",
                " let foo = -2 ",
                "}}",
                "\n1 + 1 = ",
                "{{-",
                "-",
                "foo",
                "}}",
            ),
        )
    }
}
