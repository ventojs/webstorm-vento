/*
 * Copyright (c) 2023 Óscar Otero
 * All rights reserved.
 */

package org.js.vento.webstormvento

import com.intellij.lexer.FlexLexer
import com.intellij.psi.tree.IElementType
import com.intellij.util.SlowOperations
import junit.framework.TestCase
import org.js.vento.plugin.lexer.VentoLexerAdapter


class LexerTestCase(name: String) : TestCase(name) {

    private lateinit var lexer: FlexLexer

    override fun setUp() {
        super.setUp()
        val lexer = SlowOperations.knownIssue("IDEA-000000").use {
            VentoLexerAdapter.createTestLexer()
        }
        assertNotNull("Lexer should not be null", lexer)
        this.lexer = lexer
    }

    fun `test lexing comment`() {

        lexAndTest(
            " {{# console.log('Hello World') #}} ",
            arrayOf("{{#", " console.log('Hello World') ", "#}}")
        )

    }

    fun `test lexing trimmed comment `() {

        lexAndTest(
            " {{#- This is a comment -#}} ",
            arrayOf("{{#-", " This is a comment ", "-#}}")
        )

    }

    fun `test lexing javascript`() {

        lexAndTest(
            " {{> if(true){console.log('Hello World')} }} ",
            arrayOf("{{>", " if(true){console.log('Hello World')} ", "}}")
        )

    }

    fun `test lexing multiline javascript`() {

        lexAndTest(
            """ 
            {{> if(true){
                   console.log('Hello World')
                } }} 
            """,
            arrayOf(
                "{{>",
                """ if(true){
                   console.log('Hello World')
                } """,
                "}}"
            )
        )
    }

    fun `test lexing variables`() {
        lexAndTest("{{ variable }}", arrayOf("{{", " variable ", "}}"))
    }

    fun `test lexing variables with pipes`() {
        lexAndTest("{{ variable || \"default\" }}", arrayOf("{{", " variable ", "||", " \"default\" ", "}}"))
    }

    private fun lexAndTest(template: String, tokens: Array<String>) {
        initLexer(template)
        tokens.forEach { expected ->
            val token = getNext(lexer, template)
            assertEquals(expected, token.second)
        }
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