/*
 * Copyright (c) 2023 Óscar Otero
 * All rights reserved.
 */

package org.js.vento.webstormvento

import com.intellij.util.SlowOperations
import junit.framework.TestCase

class LexerTestCase(name: String) : TestCase(name) {

    fun testLexer() {
        // Use the VentoLexerAdapter instead of direct VentoLexer instantiation
        val lexer = SlowOperations.knownIssue("IDEA-000000").use {
            VentoLexerAdapter.createTestLexer()
        }

        // Add actual test assertions here
        assertNotNull("Lexer should not be null", lexer)

        val vto = " {{# console.log('Hello World') #}} "
        lexer.reset(vto, 0, vto.length, 0)

        var tokenType = lexer.advance()

        val tokenStart = lexer.tokenStart
        val tokenEnd = lexer.tokenEnd
        val tokenText = vto.substring(tokenStart, tokenEnd)

        assertEquals("capture commented code", " console.log('Hello World') ", tokenText)

        tokenType = lexer.advance()


    }
}