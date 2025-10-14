/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import org.js.vento.plugin.DefaultElement
import org.js.vento.plugin.ExportCloseElement
import org.js.vento.plugin.ExportElement
import org.js.vento.plugin.ExportFunctionElement
import org.js.vento.plugin.ExportOpenElement
import org.js.vento.plugin.ForBlockElement
import org.js.vento.plugin.ImportElement
import org.js.vento.plugin.JavaScripElement
import org.js.vento.plugin.SetCloseElement
import org.js.vento.plugin.SetElement
import org.js.vento.plugin.VariableElement
import org.js.vento.plugin.VentoElement

/**
 * A factory to create PSI nodes from AST nodes, typically referenced
 * by your parser definition in createElement(node: ASTNode).
 */
object PareserElementFactory {
    fun createElement(node: ASTNode): PsiElement =
        when (node.elementType) {
            ParserElements.UNKNOWN_ELEMENT -> VentoElement(node)
            ParserElements.HTML_ELEMENT -> VentoElement(node)
            ParserElements.JAVASCRIPT_ELEMENT -> JavaScripElement(node)
            ParserElements.JAVACRIPT_VARIABLE_ELEMENT -> VariableElement(node)
            ParserElements.FOR_ELEMENT -> ForBlockElement(node)

            ParserElements.IMPORT_ELEMENT -> ImportElement(node)

            ParserElements.EXPORT_ELEMENT -> ExportElement(node)
            ParserElements.EXPORT_OPEN_ELEMENT -> ExportOpenElement(node)
            ParserElements.EXPORT_CLOSE_ELEMENT -> ExportCloseElement(node)
            ParserElements.EXPORT_FUNCTION_ELEMENT -> ExportFunctionElement(node)

            ParserElements.SET_ELEMENT -> SetElement(node)
            ParserElements.SET_CLOSE_ELEMENT -> SetCloseElement(node)

            else -> DefaultElement(node)
        }
}
