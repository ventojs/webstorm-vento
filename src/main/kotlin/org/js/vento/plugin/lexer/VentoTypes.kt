/*
 * Copyright (c) 2023 Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.lexer

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType

/**
 * Defines token and element types for the Vento language.
 * This includes both basic token types (like COMMENT, KEYWORD, etc.)
 * and any custom element types for the PSI structure.
 */
object VentoTypes {
    @JvmField
    var FRONT_MATTER_END: IElementType = VentoTokenType("VENTO_FRONT_MATTER_END")

    @JvmField
    var PURE_JS_END: IElementType = VentoTokenType("VENTO_PURE_JS_END")

    @JvmField
    var TEMPLATE_TAG_START: IElementType = VentoTokenType("VENTO_TEMPLATE_TAG_START")

    @JvmField
    var TEMPLATE_TAG_END: IElementType = VentoTokenType("VENTO_TEMPLATE_TAG_END")

    @JvmField
    var KEYWORD: IElementType = VentoTokenType("VENTO_KEYWORD")

    @JvmField
    var PURE_JS_START: IElementType = VentoTokenType("VENTO_PURE_JS_START")

    @JvmField
    var COMMENTED_CODE_START: IElementType = VentoTokenType("VENTO_COMMENTED_CODE_START")

    @JvmField
    var TRIMMED_COMMENTED_CODE_START: IElementType = VentoTokenType("VENTO_TRIMMED_COMMENTED_CODE_START")

    @JvmField
    var COMMENTED_CODE_CONTENT: IElementType = VentoTokenType("VENTO_COMMENTED_CODE_CONTENT")

    @JvmField
    var COMMENTED_CODE_END: IElementType = VentoTokenType("VENTO_COMMENTED_CODE_END")

    @JvmField
    var TRIMMED_COMMENTED_CODE_END: IElementType = VentoTokenType("VENTO_TRIMMED_COMMENTED_CODE_END")

    @JvmField
    var RBRACE: IElementType = VentoTokenType("VENTO_RBRACE")

    @JvmField
    var LBRACE: IElementType = VentoTokenType("VENTO_LBRACE")

    @JvmField
    var IDENTIFIER: IElementType = VentoTokenType("VENTO_IDENTIFIER")

    @JvmField
    var EQUALS: IElementType = VentoTokenType("VENTO_EQUALS")

    @JvmField
    var SEMICOLON: IElementType = VentoTokenType("VENTO_SEMICOLON")

    @JvmField
    var NUMBER: IElementType = VentoTokenType("VENTO_NUMBER")

    @JvmField
    var DIVIDE: IElementType = VentoTokenType("VENTO_DIVIDE")

    @JvmField
    var MINUS: IElementType = VentoTokenType("VENTO_MINUS")

    @JvmField
    var PLUS: IElementType = VentoTokenType("VENTO_PLUS")

    @JvmField
    var MULTIPLY: IElementType = VentoTokenType("VENTO_MULTIPLY")

    @JvmField
    var ERROR = VentoTokenType("VENTO_ERROR")

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