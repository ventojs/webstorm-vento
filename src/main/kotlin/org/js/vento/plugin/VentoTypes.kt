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
    var TRIMMED_COMMENTED_START: IElementType = VentoTokenType("VENTO_TRIMMED_COMMENTED_START")

    @JvmField
    var COMMENTED_CONTENT: IElementType = VentoTokenType("VENTO_COMMENTED_CONTENT")

    @JvmField
    var COMMENTED_END: IElementType = VentoTokenType("VENTO_COMMENTED_END")

    @JvmField
    var TRIMMED_COMMENTED_END: IElementType = VentoTokenType("VENTO_TRIMMED_COMMENTED_END")

    @JvmField
    var JAVASCRIPT_START: IElementType = VentoTokenType("VENTO_JAVASCRIPT_START")

    @JvmField
    var JAVASCRIPT_END: IElementType = VentoTokenType("VENTO_JAVASCRIPT_END")


    /*    @JvmField
        var FRONT_MATTER_END: IElementType = VentoTokenType("VENTO_FRONT_MATTER_END")


        @JvmField
        var TEMPLATE_TAG_START: IElementType = VentoTokenType("VENTO_TEMPLATE_TAG_START")

        @JvmField
        var TEMPLATE_TAG_END: IElementType = VentoTokenType("VENTO_TEMPLATE_TAG_END")

        @JvmField
        var KEYWORD: IElementType = VentoTokenType("VENTO_KEYWORD")

        @JvmField
        var PURE_JS_START: IElementType = VentoTokenType("VENTO_PURE_JS_START")


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
        var MULTIPLY: IElementType = VentoTokenType("VENTO_MULTIPLY")*/

    @JvmField
    var ERROR = VentoTokenType("VENTO_ERROR")

    @JvmField
    val COMMENT = VentoTokenType("VENTO_COMMENT")

    @JvmField
    val STRING = VentoTokenType("VENTO_STRING")

    @JvmField
    val VENTO_ELEMENT = VentoElementType("VENTO_ELEMENT")

    @JvmField
    val JAVASCRIPT_ELEMENT = VentoElementType("VENTO_JAVASCRIPT_ELEMENT")


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