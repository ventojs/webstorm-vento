/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import org.js.vento.plugin.ExportCloseElement
import org.js.vento.plugin.ExportElement
import org.js.vento.plugin.ExportFunctionElement
import org.js.vento.plugin.ExportOpenElement
import org.js.vento.plugin.ForBlockElement
import org.js.vento.plugin.HtmlElement
import org.js.vento.plugin.ImportElement
import org.js.vento.plugin.JavaScriptElement
import org.js.vento.plugin.SetCloseElement
import org.js.vento.plugin.SetElement
import org.js.vento.plugin.VariableElement

/**
 * Defines token and element types for the Vento language.
 * This includes both basic token types (like COMMENT, KEYWORD, etc.)
 * and any custom element types for the PSI structure.
 */
object ParserElements {
    // Basic Vento elements
    @JvmField
    val COMMENT_BLOCK = VentoParserElementType("VENTO_COMMENT_BLOCK")

    @JvmField
    val EXPRESSION = VentoParserElementType("VENTO_EXPRESSION")

    @JvmField
    val HTML_ELEMENT = VentoParserElementType("VENTO_HTML_ELEMENT")

    @JvmField
    val DEFAULT_ELEMENT = VentoParserElementType("VENTO_ELEMENT")

    // JavaScript and variable elements
    @JvmField
    val JAVACRIPT_VARIABLE_ELEMENT = VentoParserElementType("VENTO_VARIABLE_ELEMENT")

    @JvmField
    val JAVASCRIPT_ELEMENT = VentoParserElementType("VENTO_JAVASCRIPT_ELEMENT")

    // Control flow elements
    @JvmField
    val VENTO_FOR_ELEMENT: IElementType = VentoParserElementType("VENTO_FOR_ELEMENT")

    // Import elements
    @JvmField
    val IMPORT_ELEMENT: IElementType = VentoParserElementType("VENTO_IMPORT_ELEMENT")

    // Export elements
    @JvmField
    val EXPORT_CLOSE_ELEMENT: IElementType = VentoParserElementType("VENTO_EXPORT_CLOSE_ELEMENT")

    @JvmField
    val EXPORT_ELEMENT: IElementType = VentoParserElementType("VENTO_EXPORT_ELEMENT")

    @JvmField
    val EXPORT_FUNCTION_ELEMENT: IElementType = VentoParserElementType("VENTO_EXPORT_FUNCTION_ELEMENT")

    @JvmField
    val EXPORT_OPEN_ELEMENT: IElementType = VentoParserElementType("VENTO_EXPORT_OPEN_ELEMENT")

    // Set elements
    @JvmField
    val SET_CLOSE_ELEMENT: IElementType = VentoParserElementType("VENTO_SET_CLOSE_ELEMENT")

    @JvmField
    val SET_ELEMENT: IElementType = VentoParserElementType("VENTO_SET_ELEMENT")

    /**
     * A factory to create PSI nodes from AST nodes, typically referenced
     * by your parser definition in createElement(node: ASTNode).
     */
    object Factory {
        fun createElement(node: ASTNode): PsiElement =
            when (node.elementType) {
                DEFAULT_ELEMENT -> HtmlElement(node)
                HTML_ELEMENT -> HtmlElement(node)
                JAVASCRIPT_ELEMENT -> JavaScriptElement(node)
                JAVACRIPT_VARIABLE_ELEMENT -> VariableElement(node)
                VENTO_FOR_ELEMENT -> ForBlockElement(node)
                IMPORT_ELEMENT -> ImportElement(node)
                EXPORT_ELEMENT -> ExportElement(node)
                EXPORT_OPEN_ELEMENT -> ExportOpenElement(node)
                EXPORT_CLOSE_ELEMENT -> ExportCloseElement(node)
                EXPORT_FUNCTION_ELEMENT -> ExportFunctionElement(node)
                SET_ELEMENT -> SetElement(node)
                SET_CLOSE_ELEMENT -> SetCloseElement(node)
                else -> HtmlElement(node)
            }
    }
}
