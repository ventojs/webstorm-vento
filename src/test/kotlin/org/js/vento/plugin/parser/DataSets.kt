/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

var expressionSet: Set<Pair<String, String>> =
    setOfNotNull(
        Pair(
            "string",
            """"Hello World of \"vento\"!"""",
        ),
        Pair(
            "string-single",
            """'Hello to the "World\'s People"!'""",
        ),
        Pair(
            "string-literal",
            $$"""`Hello ${user}!`""",
        ),
        Pair(
            "regex",
            """/[Hh].*\/.*[}]/""",
        ),
        Pair(
            "regex-and-match",
            """"[Hello]/{World!}".match(/[Hh].*\/.*[}]/) """,
        ),
        Pair(
            "identifier",
            """myTextVariable""",
        ),
        Pair(
            "identifier-with-child-call",
            """JSON.stringify(data)""",
        ),
        Pair(
            "object",
            """{a: 1, b: true, c:{d:"hello"}}""",
        ),
        Pair(
            "object-json",
            """{"a": 1, "b": true, "c":{"d":"hello"}}""",
        ),
        Pair(
            "array",
            """[1, 2, 3, 4]""",
        ),
        Pair(
            "array-of-objects",
            """[{a:1,b:2},{c:3,d:4}]""",
        ),
        Pair(
            "array-of-strings",
            """["a","b","c","d"]""",
        ),
        Pair(
            "function",
            """function() { return 'Hello World!' }""",
        ),
        Pair(
            "iife",
            """(function() { return 'Hello World!' })()""",
        ),
        Pair(
            "lambda-with-body",
            """() => { return 'Hello World!' }""",
        ),
        Pair(
            "lambda",
            """() => 'Hello World!' """,
        ),
    )

var destructuringSet: Set<Pair<String, String>> =
    setOfNotNull(
        Pair(
            "array-simple",
            "[a, b] = array",
        ),
        Pair(
            "array-skip",
            "[a, , b] = array",
        ),
        Pair(
            "array-default",
            "[a = aDefault, b] = array",
        ),
        Pair(
            "array-expand",
            "[a, b, ...rest] = array",
        ),
        Pair(
            "array-skip-expand",
            "[a, , b, ...rest] = array",
        ),
        Pair(
            "array-expand-object",
            "[a, b, ...{ pop, push }] = array",
        ),
        Pair(
            "array-expand-array",
            "[a, b, ...[c, d]] = array",
        ),
        Pair(
            "object-simple",
            "{ a, b } = obj",
        ),
        Pair(
            "object-assignment",
            "{ a: a1, b: b1 } = obj",
        ),
        Pair(
            "object-assignment-default",
            """{ a: a1 = "hi", b = bDefault } = obj""",
        ),
        Pair(
            "object-assignment-default2",
            "{ a: a1 = find(10).toText(), b = 100 } = obj",
        ),
        Pair(
            "object-assignment-default3",
            "{ a: a1 = true, b = new Date() } = obj",
        ),
        Pair(
            "object-expand",
            "{ a, b, ...rest } = obj",
        ),
        Pair(
            "object-assignment-expand",
            "{ a: a1, b: b1, ...rest } = obj",
        ),
        Pair(
            "object-key",
            "{ [key]: a } = obj",
        ),
    )
