/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.LiteralTextEscaper
import com.intellij.psi.PsiLanguageInjectionHost

/**
 * Base class for all PSI elements in the Vento language.
 * Extends ASTWrapperPsiElement to provide default implementations.
 *
 * @param node The AST node corresponding to this PSI element.
 */
sealed class BaseElementImpl(node: ASTNode) : ASTWrapperPsiElement(node) {
    override fun toString(): String = "Element: ${node.elementType}"
}

class DefaultElement(node: ASTNode) : BaseElementImpl(node)

class ImportElement(node: ASTNode) : BaseElementImpl(node)

class ExportElement(node: ASTNode) : BaseElementImpl(node)

class ExportOpenElement(node: ASTNode) : BaseElementImpl(node)

class ExportCloseElement(node: ASTNode) : BaseElementImpl(node)

class ExportFunctionElement(node: ASTNode) : BaseElementImpl(node)

class ForBlockElement(node: ASTNode) : BaseElementImpl(node)

class SetElement(node: ASTNode) : BaseElementImpl(node)

class SetCloseElement(node: ASTNode) : BaseElementImpl(node)

/**
 * Represents a PSI element for JavaScript blocks in Vento templates.
 * This class is a specific implementation of `VentoPsiElementImpl` and implements
 * `PsiLanguageInjectionHost` to enable language injection for JavaScript code.
 *
 * @constructor Creates a new instance with the given AST node.
 * @param node The AST node associated with this PSI element.
 */
class JavaScriptElement(node: ASTNode) :
    BaseElementImpl(node),
    PsiLanguageInjectionHost {
    override fun isValidHost(): Boolean = true

    override fun updateText(text: String): PsiLanguageInjectionHost = this

    override fun createLiteralTextEscaper(): LiteralTextEscaper<out PsiLanguageInjectionHost> {
        return object : LiteralTextEscaper<JavaScriptElement>(this) {
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

/**
 * PSI element representing a Vento variable block that contains a JavaScript expression.
 * This element enables JavaScript language injection for variable expressions.
 */
class VariableElement(node: ASTNode) :
    BaseElementImpl(node),
    PsiLanguageInjectionHost {
    override fun isValidHost(): Boolean = true

    override fun updateText(text: String): PsiLanguageInjectionHost = this

    override fun createLiteralTextEscaper(): LiteralTextEscaper<out PsiLanguageInjectionHost> {
        return object : LiteralTextEscaper<VariableElement>(this) {
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

class HtmlElement(node: ASTNode) :
    BaseElementImpl(node),
    PsiLanguageInjectionHost {
    override fun toString(): String = "HtmlElement: ${node.elementType}"

    // Enable language injection (e.g., HTML) into default content blocks
    override fun isValidHost(): Boolean = true

    override fun updateText(text: String): PsiLanguageInjectionHost = this

    override fun createLiteralTextEscaper(): LiteralTextEscaper<out PsiLanguageInjectionHost> {
        return object : LiteralTextEscaper<HtmlElement>(this) {
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
