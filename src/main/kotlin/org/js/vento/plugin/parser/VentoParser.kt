/*
 * Copyright (c) 2023 Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.psi.tree.IElementType
import org.js.vento.plugin.VentoTypes

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
            else -> {
                val marker = builder.mark()
                builder.advanceLexer()
                marker.done(VentoTypes.VENTO_ELEMENT)
            }
        }
    }

    private fun parseJavaScriptElement(builder: PsiBuilder) {
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

}