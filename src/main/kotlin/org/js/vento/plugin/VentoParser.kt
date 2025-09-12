package org.js.vento.plugin

import com.intellij.lang.ASTNode
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.psi.tree.IElementType
import org.js.vento.plugin.lexer.VentoTypes

class VentoParser : PsiParser {

    override fun parse(root: IElementType, builder: PsiBuilder): ASTNode {
        val rootMarker = builder.mark()

        while (!builder.eof()) {
            parseElement(builder)
        }

        rootMarker.done(root)
        return builder.treeBuilt
    }

    private fun parseElement(builder: PsiBuilder) {
        val tokenType = builder.tokenType

        when (tokenType) {
            VentoTypes.JAVASCRIPT_START -> parseJavaScriptElement(builder)
            VentoTypes.COMMENTED_START, VentoTypes.TRIMMED_COMMENTED_START -> parseCommentElement(builder)
            else -> {
                // Handle regular content or other elements
                val marker = builder.mark()
                builder.advanceLexer()
                marker.done(VentoTypes.VENTO_ELEMENT)
            }
        }
    }

    private fun parseJavaScriptElement(builder: PsiBuilder) {
        println("parseJavaScriptElement")
        val marker = builder.mark()

        if (builder.tokenType == VentoTypes.JAVASCRIPT_START) {
            builder.advanceLexer()
        }

        if (builder.tokenType == VentoTypes.JAVASCRIPT_ELEMENT) {
            builder.advanceLexer()
        }

        if (builder.tokenType == VentoTypes.JAVASCRIPT_END) {
            builder.advanceLexer()
        }

        marker.done(VentoTypes.JAVASCRIPT_ELEMENT)
    }

    private fun parseCommentElement(builder: PsiBuilder) {
        val marker = builder.mark()

        // Consume comment start token
        builder.advanceLexer()

        // Consume comment content
        if (builder.tokenType == VentoTypes.COMMENTED_CONTENT) {
            builder.advanceLexer()
        }

        // Consume comment end token
        if (builder.tokenType == VentoTypes.COMMENTED_END ||
            builder.tokenType == VentoTypes.TRIMMED_COMMENTED_END
        ) {
            builder.advanceLexer()
        }

        marker.done(VentoTypes.VENTO_ELEMENT)
    }
}
