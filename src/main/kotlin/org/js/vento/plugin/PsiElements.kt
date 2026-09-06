/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.lang.Language
import com.intellij.lang.html.HTMLLanguage
import com.intellij.lang.javascript.JavascriptLanguage
import com.intellij.navigation.ItemPresentation
import com.intellij.navigation.ItemPresentationProviders
import com.intellij.openapi.util.TextRange
import com.intellij.psi.LiteralTextEscaper
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiLanguageInjectionHost
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry
import com.intellij.psi.util.PsiTreeUtil
import org.js.vento.plugin.file.VentoFileType

/**
 * Base class for all PSI elements in the Vento language.
 * Extends ASTWrapperPsiElement to provide default implementations.
 *
 * @param node The AST node corresponding to this PSI element.
 */
sealed class BaseElementImpl(node: ASTNode) : ASTWrapperPsiElement(node) {
    override fun toString(): String = "Element: ${node.elementType}"

    override fun getPresentation(): ItemPresentation? = ItemPresentationProviders.getItemPresentation(this)

    override fun getReferences(): Array<PsiReference?> = ReferenceProvidersRegistry.getReferencesFromProviders(this)

    override fun getLanguage(): Language = VentoLanguage
}

class FrontmatterElement(node: ASTNode) : BaseElementImpl(node)

class DefaultElement(node: ASTNode) : BaseElementImpl(node)

class ExportCloseElement(node: ASTNode) : BaseElementImpl(node)

class ExportElement(node: ASTNode) : BaseElementImpl(node)

class ExportFunctionElement(node: ASTNode) : BaseElementImpl(node)

class ExportOpenElement(node: ASTNode) : BaseElementImpl(node)

class ExpressionElement(node: ASTNode) : BaseElementImpl(node)

class ForElement(node: ASTNode) : BaseElementImpl(node)

class FunctionElement(node: ASTNode) : BaseElementImpl(node)

class ImportElement(node: ASTNode) : BaseElementImpl(node)

class IncludeElement(node: ASTNode) : BaseElementImpl(node)

class LayoutCloseElement(node: ASTNode) : BaseElementImpl(node)

class LayoutElement(node: ASTNode) : BaseElementImpl(node)

class ObjectElement(node: ASTNode) : BaseElementImpl(node)

class SetCloseElement(node: ASTNode) : BaseElementImpl(node)

class SetElement(node: ASTNode) : BaseElementImpl(node)

class StringElement(node: ASTNode) : BaseElementImpl(node)

class VentoElement(node: ASTNode) : BaseElementImpl(node)

abstract class BaseJsElement<T : PsiLanguageInjectionHost>(node: ASTNode) :
    BaseElementImpl(node),
    PsiLanguageInjectionHost {
    override fun isValidHost(): Boolean = true

    override fun updateText(text: String): PsiLanguageInjectionHost = this

    abstract fun getContentRange(): TextRange

    override fun getLanguage(): Language = JavascriptLanguage

    override fun createLiteralTextEscaper(): LiteralTextEscaper<T> {
        return object : LiteralTextEscaper<T>(this as (T)) {
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
}

class JavaScriptElement(node: ASTNode) : BaseJsElement<JavaScriptElement>(node) {
    override fun getContentRange(): TextRange {
        val text = this.text
        val start =
            when {
                text.indexOf("{{>") > -1 -> text.indexOf("{{>") + 3
                else -> 0
            }
        val end = if (text.endsWith("}}")) text.length - 2 else text.length
        return TextRange(start, end)
    }

    /**
     * Re-parses [text] as a standalone Vento file and replaces this element with the freshly
     * parsed one. [text] is already the complete new raw text for this element (the platform
     * reconstructs it from the injected/decoded edit using [getContentRange] and
     * [createLiteralTextEscaper] before calling this method) - it must NOT be re-wrapped with
     * `{{>`/`}}` here, or they end up duplicated. This is what lets injected-JS edits -
     * including inserted newlines, e.g. from the "Introduce Variable" refactoring - actually
     * reach the host `.vto` document instead of being silently dropped (see issue #170).
     */
    override fun updateText(text: String): PsiLanguageInjectionHost {
        val newFile = PsiFileFactory.getInstance(project).createFileFromText("dummy.vto", VentoFileType, text)
        val ventoPsi = newFile.viewProvider.getPsi(VentoLanguage) ?: return this
        val newElement = PsiTreeUtil.findChildOfType(ventoPsi, JavaScriptElement::class.java) ?: return this

        return this.replace(newElement) as PsiLanguageInjectionHost
    }
}

class JavaScriptExpressionElement(node: ASTNode) : BaseJsElement<JavaScriptExpressionElement>(node) {
    override fun getContentRange(): TextRange = TextRange(0, this.text.length)
}

class JavaScriptDataObjectElement(node: ASTNode) : BaseJsElement<JavaScriptDataObjectElement>(node) {
    override fun getContentRange(): TextRange = TextRange(0, this.text.length)
}

class HtmlElement(node: ASTNode) : BaseElementImpl(node), PsiLanguageInjectionHost {
    override fun toString(): String = "HtmlElement: ${node.elementType}"

    // Enable language injection (e.g., HTML) into default content blocks
    override fun isValidHost(): Boolean = true

    override fun updateText(text: String): PsiLanguageInjectionHost = this

    override fun getLanguage(): Language = HTMLLanguage.INSTANCE

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
