/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.lexer

import com.intellij.lexer.FlexLexer
import com.intellij.psi.tree.IElementType
import com.intellij.util.SlowOperations
import com.jetbrains.rd.util.printlnError
import junit.framework.TestCase
import kotlin.math.abs

abstract class BaseLexerTestCase(name: String, val debug: Boolean = false) : TestCase(name) {
    protected fun lexAndTest(template: String, tokens: Array<String>, noErrors: Boolean = true) {
        var passed = false
        try {
            val lexer = initLexer(template, debug)
            tokens.forEach { expected ->
                val token: Pair<IElementType?, String> = getNext(lexer, template)
                assertEquals("token mismatched", expected, token.second)
                if (noErrors) assertNotSame("UNKNOWN token found.", LexerTokens.UNKNOWN, token.first)
            }

            val token: IElementType? = lexer.advance()
            assertTrue(
                "($token) at (${lexer.tokenStart}, ${lexer.tokenEnd}) is not null: " +
                    template.substring(
                        lexer.tokenStart,
                        lexer.tokenEnd,
                    ),
                token == null || token.toString() == "UNKNOWN_TOKEN",
            )
            passed = true
        } finally {
            if (!passed) lexAndPrint(template)
        }
    }

    protected fun countVentoBlocks(template: String, count: Int = 1) {
        var passed = false
        var openCount = 0
        var closeCount = 0
        try {
            val lexer = initLexer(template, debug)

            while (lexer.tokenEnd < template.length) {
                val type = lexer.advance()
                if (type == LexerTokens.VBLOCK_OPEN) openCount++
                if (type == LexerTokens.VBLOCK_CLOSE) closeCount++
            }

//            assertEquals("number of {{ does not match number of }}: ", closeCount, openCount)
            assertEquals(
                "did not meet expected count (open:$openCount, close:$closeCount): ",
                count,
                ((openCount + closeCount) - abs(openCount - closeCount)) / 2,
            )

            passed = true
        } finally {
            if (!passed) {
                printlnError("[{{] found: $openCount expected: $count ")
                printlnError("[}}] found: $closeCount expected: $count ")
                lexAndPrint(template)
            }
        }
    }

    protected fun lexAndPrint(template: String) {
        val lexer = initLexer(template, false)
        val output = StringBuilder()

        output.appendLine("-".repeat(30))
        output.appendLine("Template:")
        output.appendLine("-".repeat(30))
        output.appendLine(template)
        output.appendLine("-".repeat(30))
        output.appendLine("Tokens:")
        output.appendLine("-".repeat(30))

        do {
            val token: Pair<IElementType?, String> = getNext(lexer, template)
            output.appendLine(
                "token: " + "${token.first}(${token.first?.index})".padEnd(40, ' ') + " = [${token.second}]",
            )
        } while (lexer.tokenEnd < template.length)

        printlnError(output.toString())
        lexer.debugModeRestore()
    }

    private fun initLexer(string: String, debug: Boolean = false): VentoLexer {
        val lexer =
            SlowOperations.knownIssue("IDEA-000000").use {
                LexerAdapter.createTestLexer(debug)
            }
        assertNotNull("Lexer should not be null", lexer)

        lexer.apply {
            reset(string, 0, string.length, 0)
            return lexer as VentoLexer
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
