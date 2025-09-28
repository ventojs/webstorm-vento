/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.LiteralTextEscaper
import com.intellij.psi.PsiLanguageInjectionHost
import org.js.vento.plugin.VentoPsiElementImpl

/**
 * PSI element representing a Vento variable block that contains a JavaScript expression.
 * This element enables JavaScript language injection for variable expressions.
 */
class VentoVariablePsiElement(node: ASTNode) :
    VentoPsiElementImpl(node),
    PsiLanguageInjectionHost {
    override fun isValidHost(): Boolean = true

    override fun updateText(text: String): PsiLanguageInjectionHost = this

    override fun createLiteralTextEscaper(): LiteralTextEscaper<out PsiLanguageInjectionHost> {
        return object : LiteralTextEscaper<VentoVariablePsiElement>(this) {
            override fun decode(rangeInsideHost: TextRange, outChars: StringBuilder): Boolean {
                val content = rangeInsideHost.substring(myHost.text)
                outChars.append(content)
                return true
            }

            override fun getOffsetInHost(offsetInDecoded: Int, rangeInsideHost: TextRange): Int =
                rangeInsideHost.startOffset + offsetInDecoded

            override fun getRelevantTextRange(): TextRange = getContentRange()

            override fun isOneLine(): Boolean = false
        }
    }

    /**
     * Returns the content range of the JavaScript expression within the variable block,
     * excluding the opening and closing delimiters.
     */
    fun getContentRange(): TextRange {
        val text = this.text
        val start =
            when {
                text.indexOf("{{-") > -1 -> text.indexOf("{{-") + 3
                text.indexOf("{{") > -1 -> text.indexOf("{{") + 2
                else -> 0
            }
        val end =
            when {
                text.endsWith("-}}") -> text.length - 3
                text.endsWith("}}") -> text.length - 2
                else -> text.length
            }

        return TextRange(start, end)
    }
}
