/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.highlighting.validator

import com.intellij.lang.tree.util.children
import com.intellij.psi.impl.source.tree.PsiErrorElementImpl
import org.js.vento.plugin.ForBlockElement
import org.js.vento.plugin.lexer.LexerTokens

/**
 * Validates JavaScript expressions for use within Vento variable blocks.
 * This class ensures that the given JavaScript expressions follow the correct syntax and semantics required for variable expressions.
 *
 * The primary method in this class analyzes the provided JavaScript content
 * and checks if it complies with the rules for valid variable expressions in JavaScript.
 * It verifies that the content is not empty, consists of a single valid expression, and doesn't contain any invalid statements.
 *
 * Errors detected during validation are returned as part of a `ValidationResult`,
 * which includes an error message explaining the issue if the validation fails.
 */
class ForBlockValidator {
    fun isValidExpression(content: ForBlockElement): ValidationResult {
        if (content.text.isEmpty()) return ValidationResult(false, "Empty for-block found. $SYNTAX")
        if (content.text.matches(forClose)) {
            return ValidationResult(true, "Valide closing for-block found.")
        } else {
            if (content.node.children().any {
                    it.elementType.toString() == LexerTokens.UNKNOWN.toString() ||
                        it::class.java == PsiErrorElementImpl::class.java
                }
            ) {
                return ValidationResult(false, "Invalid for-block. $SYNTAX")
            }
        }

        return ValidationResult(true, RULE)
    }

    companion object {
        private val forClose = Regex("\\{\\{[ \t]*/for[ \t]*}}")
        private const val SYNTAX = "structure is: {{ for value of collection }} followed by {{ /for }}"
        private const val RULE = "Invalid for-block. $SYNTAX"
    }
}
