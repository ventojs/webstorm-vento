/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import org.js.vento.plugin.parser.ParserElements

/**
 * A factory to create PSI nodes from AST nodes, typically referenced
 * by your parser definition in createElement(node: ASTNode).
 */
object PsiElementFactory {
    fun createElement(node: ASTNode): PsiElement =
        when (node.elementType) {
            ParserElements.VENTO_BLOCK -> VentoElement(node)
            ParserElements.HTML_CONTENT -> HtmlElement(node)
            ParserElements.JAVASCRIPT_ELEMENT -> JavaScriptElement(node)
            ParserElements.JAVASCRIPT_EXPRESSION_ELEMENT -> JavaScriptExpressionElement(node)
            ParserElements.JAVASCRIPT_DATA_OBJECT_ELEMENT -> JavaScriptDataObjectElement(node)
            ParserElements.FOR_ELEMENT -> ForElement(node)
            ParserElements.FOR_CLOSE_ELEMENT -> ForElement(node)
            ParserElements.IMPORT_ELEMENT -> ImportElement(node)
            ParserElements.EXPORT_ELEMENT -> ExportElement(node)
            ParserElements.EXPORT_OPEN_ELEMENT -> ExportOpenElement(node)
            ParserElements.EXPORT_CLOSE_ELEMENT -> ExportCloseElement(node)
            ParserElements.EXPORT_FUNCTION_ELEMENT -> ExportFunctionElement(node)
            ParserElements.EXPRESSION_ELEMENT -> ExpressionElement(node)
            ParserElements.FUNCTION_ELEMENT -> FunctionElement(node)
            ParserElements.SET_ELEMENT -> SetElement(node)
            ParserElements.SET_CLOSE_ELEMENT -> SetCloseElement(node)
            ParserElements.LAYOUT_ELEMENT -> LayoutElement(node)
            ParserElements.LAYOUT_CLOSE_ELEMENT -> LayoutCloseElement(node)
            ParserElements.OBJECT_ELEMENT -> ObjectElement(node)
            ParserElements.INCLUDE_ELEMENT -> IncludeElement(node)
            ParserElements.STRING_ELEMENT -> StringElement(node)
            else -> DefaultElement(node)
        }
}
