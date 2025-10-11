/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import org.js.vento.plugin.DefaultBaseElement
import org.js.vento.plugin.ExportBaseElement
import org.js.vento.plugin.ExportCloseBaseElement
import org.js.vento.plugin.ExportFunctionBaseElement
import org.js.vento.plugin.ExportOpenBaseElement
import org.js.vento.plugin.ForBlockBaseElement
import org.js.vento.plugin.ImportBaseElement
import org.js.vento.plugin.JavaScriptBaseElement
import org.js.vento.plugin.VariablePsiBaseElement
import org.js.vento.plugin.VentoElement

/**
 * Defines token and element types for the Vento language.
 * This includes both basic token types (like COMMENT, KEYWORD, etc.)
 * and any custom element types for the PSI structure.
 */
object ParserTypes {
    @JvmField
    val EXPRESSION = VentoParserElementType("VENTO_EXPRESSION")

    @JvmField
    val COMMENT_BLOCK = VentoParserElementType("VENTO_COMMENT_BLOCK")

    @JvmField
    val HTML_ELEMENT = VentoParserElementType("VENTO_HTML_ELEMENT")

    @JvmField
    val JAVASCRIPT_ELEMENT = VentoParserElementType("VENTO_JAVASCRIPT_ELEMENT")

    @JvmField
    val JAVACRIPT_VARIABLE_ELEMENT = VentoParserElementType("VENTO_VARIABLE_ELEMENT")

    @JvmField
    val VENTO_ELEMENT = VentoParserElementType("VENTO_ELEMENT")

    @JvmField
    val VENTO_FOR_ELEMENT: IElementType = VentoParserElementType("VENTO_FOR_ELEMENT")

    @JvmField
    val IMPORT_ELEMENT: IElementType = VentoParserElementType("VENTO_IMPORT_ELEMENT")

    @JvmField
    val EXPORT_ELEMENT: IElementType = VentoParserElementType("VENTO_EXPORT_ELEMENT")

    @JvmField
    val EXPORT_OPEN_ELEMENT: IElementType = VentoParserElementType("VENTO_EXPORT_OPEN_ELEMENT")

    @JvmField
    val EXPORT_CLOSE_ELEMENT: IElementType = VentoParserElementType("VENTO_EXPORT_CLOSE_ELEMENT")

    @JvmField
    val EXPORT_FUNCTION_ELEMENT: IElementType = VentoParserElementType("VENTO_EXPORT_FUNCTION_ELEMENT")

    /**
     * A factory to create PSI nodes from AST nodes, typically referenced
     * by your parser definition in createElement(node: ASTNode).
     */
    object Factory {
        fun createElement(node: ASTNode): PsiElement =
            when (node.elementType) {
                VENTO_ELEMENT -> VentoElement(node)
                HTML_ELEMENT -> VentoElement(node)
                JAVASCRIPT_ELEMENT -> JavaScriptBaseElement(node)
                JAVACRIPT_VARIABLE_ELEMENT -> VariablePsiBaseElement(node)
                VENTO_FOR_ELEMENT -> ForBlockBaseElement(node)
                IMPORT_ELEMENT -> ImportBaseElement(node)
                EXPORT_ELEMENT -> ExportBaseElement(node)
                EXPORT_OPEN_ELEMENT -> ExportOpenBaseElement(node)
                EXPORT_CLOSE_ELEMENT -> ExportCloseBaseElement(node)
                EXPORT_FUNCTION_ELEMENT -> ExportFunctionBaseElement(node)
                else -> DefaultBaseElement(node)
            }
    }
}
