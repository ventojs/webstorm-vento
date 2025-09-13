/*
 * Copyright (c) 2023 Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import org.js.vento.plugin.lexer.VentoElementImpl
import org.js.vento.plugin.lexer.VentoElementType
import org.js.vento.plugin.parser.VentoJavaScriptPsiElement

/**
 * Defines token and element types for the Vento language.
 * This includes both basic token types (like COMMENT, KEYWORD, etc.)
 * and any custom element types for the PSI structure.
 */
object VentoTypes {

    @JvmField
    var COMMENTED_START: IElementType = VentoTokenType("VENTO_COMMENTED_START")

    @JvmField
    var COMMENTED_END: IElementType = VentoTokenType("VENTO_COMMENTED_END")

    @JvmField
    var TRIMMED_COMMENTED_START: IElementType = VentoTokenType("VENTO_TRIMMED_COMMENTED_START")

    @JvmField
    var TRIMMED_COMMENTED_END: IElementType = VentoTokenType("VENTO_TRIMMED_COMMENTED_END")

    @JvmField
    var COMMENTED_CONTENT: IElementType = VentoTokenType("VENTO_COMMENTED_CONTENT")

    @JvmField
    var JAVASCRIPT_START: IElementType = VentoTokenType("VENTO_JAVASCRIPT_START")

    @JvmField
    val JAVASCRIPT_ELEMENT = VentoElementType("VENTO_JAVASCRIPT_ELEMENT")

    @JvmField
    var JAVASCRIPT_END: IElementType = VentoTokenType("VENTO_JAVASCRIPT_END")

    @JvmField
    var VARIABLE_START: IElementType = VentoTokenType("VENTO_VARIABLE_START")

    @JvmField
    val VARIABLE_ELEMENT = VentoTokenType("VENTO_VARIABLE_ELEMENT")

    @JvmField
    val VARIABLE_PIPES = VentoElementType("VENTO_VARIABLE_PIPES")

    @JvmField
    var VARIABLE_END: IElementType = VentoTokenType("VENTO_VARIABLE_END")

    @JvmField
    var ERROR = VentoTokenType("VENTO_ERROR")

    @JvmField
    val COMMENT = VentoTokenType("VENTO_COMMENT")

    @JvmField
    val STRING = VentoTokenType("VENTO_STRING")

    @JvmField
    val VENTO_ELEMENT = VentoElementType("VENTO_ELEMENT")

    @JvmField
    val HTML_TAG = VentoTokenType("VENTO_HTML_TAG")

    @JvmField
    val TEXT = VentoTokenType("VENTO_HTML_TAG")

    @JvmField
    val EMPTY_LINE = VentoTokenType("VENTO_EMPTY_LINE")


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