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
    val JAVASCRIPT_ELEMENT = VentoParserElementType("VENTO_JAVASCRIPT_ELEMENT")

    @JvmField
    val VARIABLE_PIPES = VentoParserElementType("VENTO_VARIABLE_PIPES")

    @JvmField
    val VENTO_ELEMENT = VentoParserElementType("VENTO_ELEMENT")

    /**
     * A factory to create PSI nodes from AST nodes, typically referenced
     * by your parser definition in createElement(node: ASTNode).
     */
    object Factory {
        fun createElement(node: ASTNode): PsiElement {
//            println("ast node: ${node.elementType}")
            return when (node.elementType) {
                VENTO_ELEMENT -> VentoElementImpl(node)
                JAVASCRIPT_ELEMENT -> VentoJavaScriptPsiElement(node)
                else -> VentoPsiElementImpl(node)
            }
        }
    }
}
