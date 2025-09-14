/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode

/**
 * Base class for all PSI elements in the Vento language.
 * Extends ASTWrapperPsiElement to provide default implementations.
 *
 * @param node The AST node corresponding to this PSI element.
 */
open class VentoPsiElementImpl(
    node: ASTNode,
) : ASTWrapperPsiElement(node) {
    override fun toString(): String = "VentoPsiElement: ${node.elementType}"
}
