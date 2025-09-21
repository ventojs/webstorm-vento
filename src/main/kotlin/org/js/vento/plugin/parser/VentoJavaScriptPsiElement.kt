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
 * Represents a PSI element for JavaScript blocks in Vento templates.
 * This class is a specific implementation of `VentoPsiElementImpl` and implements
 * `PsiLanguageInjectionHost` to enable language injection for JavaScript code.
 *
 * @constructor Creates a new instance with the given AST node.
 * @param node The AST node associated with this PSI element.
 */
class VentoJavaScriptPsiElement(
    node: ASTNode,
) : VentoPsiElementImpl(node),
    PsiLanguageInjectionHost {
    override fun isValidHost(): Boolean = true

    override fun updateText(text: String): PsiLanguageInjectionHost = this

    override fun createLiteralTextEscaper(): LiteralTextEscaper<out PsiLanguageInjectionHost> {
        return object : LiteralTextEscaper<VentoJavaScriptPsiElement>(this) {
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

    fun getContentRange(): TextRange {
        // Remove the {{ and }} delimiters from the range
        val text = this.text
        val start =
            when {
                text.indexOf("{{>") > -1 -> text.indexOf("{{>") + 3
                else -> 0
            }
        val end = if (text.endsWith("}}")) text.length - 2 else text.length
//        println("Getting content range for:${this.text} -> $start:$end")
        return TextRange(start, end)
    }
}
