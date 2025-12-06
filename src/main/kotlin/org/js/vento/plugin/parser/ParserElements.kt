/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.psi.tree.IElementType

/**
 * Defines element types for the Vento language.
 * This includes both basic elements types (like COMMENT, KEYWORD, etc.)
 * and any custom element types for the PSI structure.
 */
object ParserElements {
    @JvmField
    val FRONTMATTER_BLOCK = ParserElement("FRONTMATTER_BLOCK")

    @JvmField
    val FRONTMATTER_LINE = ParserElement("FRONTMATTER_LINE")

    // Basic Vento elements
    @JvmField
    val COMMENT_BLOCK = ParserElement("COMMENT_BLOCK")

    @JvmField
    val HTML_CONTENT = ParserElement("HTML_CONTENT")

    @JvmField
    val JAVASCRIPT_ELEMENT = ParserElement("JAVASCRIPT_ELEMENT")

    @JvmField
    val JAVASCRIPT_EXPRESSION_ELEMENT = ParserElement("JAVASCRIPT_EXPRESSION_ELEMENT")

    @JvmField
    val JAVASCRIPT_DATA_OBJECT_ELEMENT = ParserElement("JAVASCRIPT_DATA_OBJECT_ELEMENT")

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
    val VENTO_BLOCK: IElementType = ParserElement("VENTO_ELEMENT")

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
    val FUNCTION_CLOSE_ELEMENT: IElementType = ParserElement("FUNCTION_CLOSE_ELEMENT")

    @JvmField
    val ECHO_ELEMENT: IElementType = ParserElement("ECHO_ELEMENT")

    @JvmField
    val ECHO_CLOSE_ELEMENT: IElementType = ParserElement("ECHO_CLOSE_ELEMENT")

    @JvmField
    val IF_ELEMENT: IElementType = ParserElement("IF_ELEMENT")

    @JvmField
    val ELSE_ELEMENT: IElementType = ParserElement("ELSE_ELEMENT")

    @JvmField
    val ELSEIF_ELEMENT: IElementType = ParserElement("ELSEIF_ELEMENT")

    @JvmField
    val IF_CLOSE_ELEMENT: IElementType = ParserElement("IF_CLOSE_ELEMENT")
}
