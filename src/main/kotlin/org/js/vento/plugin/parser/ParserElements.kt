/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import org.js.vento.plugin.*

/**
 * Defines token and element types for the Vento language.
 * This includes both basic token types (like COMMENT, KEYWORD, etc.)
 * and any custom element types for the PSI structure.
 */
object ParserElements {
    // Basic Vento elements
    @JvmField
    val COMMENT_BLOCK = ParserElement("COMMENT_BLOCK")

    @JvmField
    val HTML_ELEMENT = ParserElement("HTML_ELEMENT")

    @JvmField
    val DEFAULT_ELEMENT = ParserElement("ELEMENT")

    // JavaScript and variable elements
    @JvmField
    val JAVACRIPT_VARIABLE_ELEMENT = ParserElement("VARIABLE_ELEMENT")

    @JvmField
    val JAVASCRIPT_ELEMENT = ParserElement("JAVASCRIPT_ELEMENT")

    // Control flow elements
    @JvmField
    val FOR_ELEMENT: IElementType = ParserElement("FOR_ELEMENT")

    @JvmField
    val FOR_CLOSE_ELEMENT: IElementType = ParserElement("FOR_CLOSE_ELEMENT")

    // Import elements
    @JvmField
    val IMPORT_ELEMENT: IElementType = ParserElement("IMPORT_ELEMENT")

    // Export elements
    @JvmField
    val EXPORT_CLOSE_ELEMENT: IElementType = ParserElement("EXPORT_CLOSE_ELEMENT")

    @JvmField
    val EXPORT_ELEMENT: IElementType = ParserElement("EXPORT_ELEMENT")

    @JvmField
    val EXPORT_FUNCTION_ELEMENT: IElementType = ParserElement("EXPORT_FUNCTION_ELEMENT")

    @JvmField
    val EXPORT_OPEN_ELEMENT: IElementType = ParserElement("EXPORT_OPEN_ELEMENT")

    // Set elements
    @JvmField
    val SET_CLOSE_ELEMENT: IElementType = ParserElement("SET_CLOSE_ELEMENT")

    @JvmField
    val SET_ELEMENT: IElementType = ParserElement("SET_ELEMENT")

    @JvmField
    val LAYOUT_ELEMENT: IElementType = ParserElement("LAYOUT_ELEMENT")

    @JvmField
    val LAYOUT_CLOSE_ELEMENT: IElementType = ParserElement("LAYOUT_CLOSE_ELEMENT")

    @JvmField
    val LAYOUT_SLOT_ELEMENT: IElementType = ParserElement("LAYOUT_SLOT_ELEMENT")

    @JvmField
    val LAYOUT_SLOT_CLOSE_ELEMENT: IElementType = ParserElement("LAYOUT_SLOT_CLOSE_ELEMENT")

    @JvmField
    val OBJECT_ELEMENT: IElementType = ParserElement("OBJECT_ELEMENT")

    @JvmField
    val INCLUDE_ELEMENT: IElementType = ParserElement("INCLUDE_ELEMENT")

    @JvmField
    val VENTO_ELEMENT: IElementType = ParserElement("VENTO_ELEMENT")

    @JvmField
    val STRING_ELEMENT: IElementType = ParserElement("STRING_ELEMENT")

    @JvmField
    val ARRAY_ELEMENT: IElementType = ParserElement("ARRAY_ELEMENT")

    @JvmField
    val EXPRESSION_ELEMENT: IElementType = ParserElement("EXPRESSION_ELEMENT")

    @JvmField
    val UNKNOWN_ELEMENT: IElementType = ParserElement("UNKNOWN_ELEMENT")

    /**
     * A factory to create PSI nodes from AST nodes, typically referenced
     * by your parser definition in createElement(node: ASTNode).
     */
    object Factory {
        fun createElement(node: ASTNode): PsiElement =
            when (node.elementType) {
                VENTO_ELEMENT -> VentoElement(node)
                DEFAULT_ELEMENT -> HtmlElement(node)
                JAVASCRIPT_ELEMENT -> JavaScriptElement(node)
                JAVACRIPT_VARIABLE_ELEMENT -> VariableElement(node)
                FOR_ELEMENT -> ForBlockElement(node)
                FOR_CLOSE_ELEMENT -> ForBlockElement(node)
                IMPORT_ELEMENT -> ImportElement(node)
                EXPORT_ELEMENT -> ExportElement(node)
                EXPORT_OPEN_ELEMENT -> ExportOpenElement(node)
                EXPORT_CLOSE_ELEMENT -> ExportCloseElement(node)
                EXPORT_FUNCTION_ELEMENT -> ExportFunctionElement(node)
                EXPRESSION_ELEMENT -> ExpressionElement(node)
                SET_ELEMENT -> SetElement(node)
                SET_CLOSE_ELEMENT -> SetCloseElement(node)
                LAYOUT_ELEMENT -> LayoutElement(node)
                LAYOUT_CLOSE_ELEMENT -> LayoutCloseElement(node)
                OBJECT_ELEMENT -> ObjectElement(node)
                INCLUDE_ELEMENT -> IncludeElement(node)
                STRING_ELEMENT -> StringElement(node)
                else -> DefaultElement(node)
            }
    }
}
