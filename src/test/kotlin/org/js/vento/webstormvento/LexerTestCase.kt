/*
 * Copyright (c) 2023 Óscar Otero
 * All rights reserved.
 */

package org.js.vento.webstormvento

import com.intellij.util.SlowOperations
import java_cup.runtime.Scanner
import junit.framework.TestCase
import java.io.StringReader

class LexerTestCase(name: String) : TestCase(name) {

    fun testLexer() {
        // Use the public test method instead
        val lexer = SlowOperations.knownIssue("IDEA-000000").use {
            VentoLexer(StringReader(""" {{ content }} """)) as Scanner
        }
        // Add actual test assertions here
        assertNotNull("Lexer should not be null", lexer)



        println("foo:${lexer.next_token()}")
    }

}