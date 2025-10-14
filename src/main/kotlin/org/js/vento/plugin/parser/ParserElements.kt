/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.psi.tree.IElementType

/**
 * Defines token and element types for the Vento language.
 * This includes both basic token types (like COMMENT, KEYWORD, etc.)
 * and any custom element types for the PSI structure.
 */
object ParserElements {
    // Basic elements
    @JvmField
    val EXPRESSION = ParserElement("VENTO_EXPRESSION")

    @JvmField
    val COMMENT_BLOCK = ParserElement("VENTO_COMMENT_BLOCK")

    @JvmField
    val HTML_ELEMENT = ParserElement("VENTO_HTML_ELEMENT")

    // JavaScript elements
    @JvmField
    val JAVASCRIPT_ELEMENT = ParserElement("VENTO_JAVASCRIPT_ELEMENT")

    @JvmField
    val JAVACRIPT_VARIABLE_ELEMENT = ParserElement("VENTO_VARIABLE_ELEMENT")

    // Control flow elements
    @JvmField
    val FOR_ELEMENT: IElementType = ParserElement("VENTO_FOR_ELEMENT")

    // Import/Export elements
    @JvmField
    val IMPORT_ELEMENT: IElementType = ParserElement("VENTO_IMPORT_ELEMENT")

    @JvmField
    val EXPORT_ELEMENT: IElementType = ParserElement("VENTO_EXPORT_ELEMENT")

    @JvmField
    val EXPORT_OPEN_ELEMENT: IElementType = ParserElement("VENTO_EXPORT_OPEN_ELEMENT")

    @JvmField
    val EXPORT_CLOSE_ELEMENT: IElementType = ParserElement("VENTO_EXPORT_CLOSE_ELEMENT")

    @JvmField
    val EXPORT_FUNCTION_ELEMENT: IElementType = ParserElement("VENTO_EXPORT_FUNCTION_ELEMENT")

    // Set elements
    @JvmField
    val SET_ELEMENT: IElementType = ParserElement("VENTO_SET_ELEMENT")

    @JvmField
    val SET_CLOSE_ELEMENT: IElementType = ParserElement("VENTO_SET_CLOSE_ELEMENT")

    // Fallback element
    @JvmField
    val UNKNOWN_ELEMENT = ParserElement("UNKNOWN_ELEMENT")
}
