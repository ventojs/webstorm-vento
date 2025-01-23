/*
 * Copyright (c) 2023 Óscar Otero
 * All rights reserved.
 */

package org.js.vento.webstormvento

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement


/**
 * Defines token and element types for the Vento language.
 * This includes both basic token types (like COMMENT, KEYWORD, etc.)
 * and any custom element types for the PSI structure.
 */
object VentoTypes {
    @JvmField
    val COMMENT = VentoTokenType("VENTO_COMMENT")

    @JvmField
    val STRING = VentoTokenType("VENTO_STRING")

    @JvmField
    val VENTO_ELEMENT = VentoElementType("VENTO_ELEMENT")

    /**
     * A factory to create PSI nodes from AST nodes, typically referenced
     * by your parser definition in createElement(node: ASTNode).
     */
    object Factory {
        fun createElement(node: ASTNode): PsiElement = when (node.elementType) {
            VENTO_ELEMENT -> VentoElementImpl(node)
            else -> VentoPsiElementImpl(node)
        }
    }
}
