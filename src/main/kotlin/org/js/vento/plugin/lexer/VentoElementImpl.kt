/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.lexer

import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.LiteralTextEscaper
import com.intellij.psi.PsiLanguageInjectionHost
import org.js.vento.plugin.VentoPsiElementImpl

class VentoElementImpl(
    node: ASTNode,
) : VentoPsiElementImpl(node), PsiLanguageInjectionHost {
    override fun toString(): String = "VentoElement: ${node.elementType}"

    // Enable language injection (e.g., HTML) into default content blocks
    override fun isValidHost(): Boolean = true

    override fun updateText(text: String): PsiLanguageInjectionHost = this

    override fun createLiteralTextEscaper(): LiteralTextEscaper<out PsiLanguageInjectionHost> {
        return object : LiteralTextEscaper<VentoElementImpl>(this) {
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

    fun getContentRange(): TextRange = TextRange(0, text.length)
}
