/*
 * Copyright (c) 2023 Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.lexer

import com.intellij.lang.ASTNode
import org.js.vento.plugin.VentoPsiElementImpl

/**
 * Concrete implementation of a PSI element for the Vento language.
 *
 * This class can be extended to represent specific language constructs.
 *
 * @param node The AST node corresponding to this PSI element.
 */
class VentoElementImpl(node: ASTNode) : VentoPsiElementImpl(node) {
    override fun toString(): String = "VentoElement: ${node.elementType}"
}