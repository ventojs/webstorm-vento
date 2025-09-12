package org.js.vento.plugin.lexer

import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.LiteralTextEscaper

import com.intellij.psi.PsiLanguageInjectionHost

class VentoJavaScriptPsiElement(node: ASTNode) : VentoPsiElementImpl(node), PsiLanguageInjectionHost {

    override fun isValidHost(): Boolean = true

    override fun updateText(text: String): PsiLanguageInjectionHost {
        return this
    }

    override fun createLiteralTextEscaper(): LiteralTextEscaper<out PsiLanguageInjectionHost> {
        return object : LiteralTextEscaper<VentoJavaScriptPsiElement>(this) {
            override fun decode(rangeInsideHost: TextRange, outChars: StringBuilder): Boolean {
                val content = rangeInsideHost.substring(myHost.text)
                outChars.append(content)
                return true
            }

            override fun getOffsetInHost(offsetInDecoded: Int, rangeInsideHost: TextRange): Int {
                return rangeInsideHost.startOffset + offsetInDecoded
            }

            override fun getRelevantTextRange(): TextRange {
                return getContentRange()
            }

            override fun isOneLine(): Boolean = false
        }
    }

    fun getContentRange(): TextRange {
        // Remove the {{ and }} delimiters from the range
        val text = this.text
        val start = when {
            text.indexOf("{{") > 0 -> text.indexOf("{{") + 2
            text.startsWith("\n{{") -> 3
            else -> 0
        }
        val end = if (text.endsWith("}}")) text.length - 2 else text.length
        println("Getting content range for:${this.text} -> $start:$end")
        return TextRange(start, end)
    }
}