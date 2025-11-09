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
