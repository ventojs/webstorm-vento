/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.webstormvento

import com.intellij.lexer.FlexLexer
import com.intellij.psi.tree.IElementType
import com.intellij.util.SlowOperations
import junit.framework.TestCase
import org.js.vento.plugin.lexer.VentoLexerAdapter

abstract class BaseLexerTestCase(name: String) : TestCase(name) {
    private lateinit var lexer: FlexLexer

    override fun setUp() {
        super.setUp()
        val lexer =
            SlowOperations.knownIssue("IDEA-000000").use {
                VentoLexerAdapter.createTestLexer()
            }
        assertNotNull("Lexer should not be null", lexer)
        this.lexer = lexer
    }

    protected fun lexAndTest(template: String, tokens: Array<String>) {
        var passed = false
        try {
            initLexer(template)
            tokens.forEach { expected ->
                val token = getNext(lexer, template)
                assertEquals(expected, token.second)
            }
            passed = true
        } finally {
            if (!passed) lexAndPrint(template)
        }
    }

    protected fun lexAndPrint(template: String) {
        initLexer(template)
        println("-".repeat(30))
        println("Template:")
        println("-".repeat(30))
        println(template)
        println("-".repeat(30))
        println("Tokens:")
        println("-".repeat(30))
        do {
            val token: Pair<IElementType?, String> = getNext(lexer, template)
            println(
                "token: " + "${token.first}(${token.first?.index})".padEnd(40, ' ') + " = [${token.second}]",
            )
        } while (lexer.tokenEnd < template.length)
    }

    private fun initLexer(string: String) {
        lexer.apply {
            reset(string, 0, string.length, 0)
        }
    }

    private fun getNext(lexer: FlexLexer, vto: String): Pair<IElementType?, String> {
        val tokenType = lexer.advance()
        val tokenStart = lexer.tokenStart
        val tokenEnd = lexer.tokenEnd
        val tokenText = vto.substring(tokenStart, tokenEnd)
        return Pair(tokenType, tokenText)
    }
}
