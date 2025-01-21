package com.github.illyrius666.webstormvento

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType

object VentoTypes {
    val COMMENT = IElementType("COMMENT", VentoLanguage.INSTANCE)
    val STRING = IElementType("STRING", VentoLanguage.INSTANCE)

    object Factory {
        fun createElement(node: ASTNode): PsiElement {
            throw AssertionError("Unknown element type: " + node.elementType)
        }
    }
}