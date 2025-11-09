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
 * Defines element types for the Vento language.
 * This includes both basic elements types (like COMMENT, KEYWORD, etc.)
 * and any custom element types for the PSI structure.
 */
object ParserElements {
    // Basic Vento elements
    @JvmField
    val COMMENT_BLOCK = ParserElement("COMMENT_BLOCK")

    @JvmField
    val DEFAULT_ELEMENT = ParserElement("ELEMENT")

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

    @JvmField
    val FUNCTION_SIGNATURE_ELEMENT: IElementType = ParserElement("FUNCTION_SIGNATURE_ELEMENT")

    @JvmField
    val FUNCTION_ARGUMENTS_ELEMENT: IElementType = ParserElement("FUNCTION_ARGUMENTS_ELEMENT")

    @JvmField
    val FUNCTION_ARG_ELEMENT: IElementType = ParserElement("FUNCTION_ARG_ELEMENT")

    @JvmField
    val FUNCTION_BODY_ELEMENT: IElementType = ParserElement("FUNCTION_BODY_ELEMENT")

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
    val REGEX_ELEMENT: IElementType = ParserElement("REGEX_ELEMENT")

    @JvmField
    val ARRAY_ELEMENT: IElementType = ParserElement("ARRAY_ELEMENT")

    @JvmField
    val EXPRESSION_ELEMENT: IElementType = ParserElement("EXPRESSION_ELEMENT")

    @JvmField
    val UNKNOWN_ELEMENT: IElementType = ParserElement("UNKNOWN_ELEMENT")

    @JvmField
    val FUNCTION_ELEMENT: IElementType = ParserElement("FUNCTION_ELEMENT")

    @JvmField
    val ECHO_ELEMENT: IElementType = ParserElement("ECHO_ELEMENT")

    @JvmField
    val ECHO_CLOSE_ELEMENT: IElementType = ParserElement("ECHO_CLOSE_ELEMENT")

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
                FOR_ELEMENT -> ForElement(node)
                FOR_CLOSE_ELEMENT -> ForElement(node)
                IMPORT_ELEMENT -> ImportElement(node)
                EXPORT_ELEMENT -> ExportElement(node)
                EXPORT_OPEN_ELEMENT -> ExportOpenElement(node)
                EXPORT_CLOSE_ELEMENT -> ExportCloseElement(node)
                EXPORT_FUNCTION_ELEMENT -> ExportFunctionElement(node)
                EXPRESSION_ELEMENT -> ExpressionElement(node)
                FUNCTION_ELEMENT -> FunctionElement(node)
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
