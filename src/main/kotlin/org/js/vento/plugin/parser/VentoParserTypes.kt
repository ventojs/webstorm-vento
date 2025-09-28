/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import org.js.vento.plugin.VentoPsiElementImpl
import org.js.vento.plugin.lexer.VentoElementImpl

/**
 * Defines token and element types for the Vento language.
 * This includes both basic token types (like COMMENT, KEYWORD, etc.)
 * and any custom element types for the PSI structure.
 */
object VentoParserTypes {
    @JvmField
    val COMMENT_BLOCK: IElementType = VentoParserElementType("VENTO_COMMENT_BLOCK")

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

    /**
     * A factory to create PSI nodes from AST nodes, typically referenced
     * by your parser definition in createElement(node: ASTNode).
     */
    object Factory {
        fun createElement(node: ASTNode): PsiElement =
            when (node.elementType) {
                VENTO_ELEMENT -> VentoElementImpl(node)
                HTML_ELEMENT -> VentoElementImpl(node)
                JAVASCRIPT_ELEMENT -> VentoJavaScriptPsiElement(node)
                JAVACRIPT_VARIABLE_ELEMENT -> VentoVariablePsiElement(node)
                VENTO_FOR_ELEMENT -> VentoPsiElementImpl(node)
                else -> VentoPsiElementImpl(node)
            }
    }
}
