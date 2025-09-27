/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.highlighting

import com.intellij.lang.javascript.JavaScriptFileType
import com.intellij.lang.javascript.psi.JSExpression
import com.intellij.lang.javascript.psi.JSFile
import com.intellij.lang.javascript.psi.JSStatement
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileFactory

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
class VentoJavaScriptExpressionValidator {
    fun isValidExpression(content: String, project: Project): ValidationResult {
        if (content.trim().isEmpty()) {
            return ValidationResult(false, "Empty expression")
        }

        try {
            // If expression parsing failed, check if it's a statement (which is invalid for variables)
            val statementFile = createJSFile(content.trim(), project)
            val statementsList = statementFile.statements

            if (statementsList.isNotEmpty()) {
                if (statementsList.size > 1) return ValidationResult(false, "Variable blocks must contain only one expression")
                val statement = statementsList[0]
                if (statement is JSStatement && statement !is JSExpression) {
                    return ValidationResult(true, "Variable blocks must contain expressions, not statements")
                }
            }

            val expressionFile = createJSFile("(${content.trim()})", project)
            val statements = expressionFile.statements

            if (statements.isNotEmpty() && statements.size > 0) {
                return ValidationResult(false, "Variable blocks must contain a statements")
            }

            return ValidationResult(false, "Invalid JavaScript expression")
        } catch (e: Exception) {
            return ValidationResult(false, "Syntax error: ${e.message}")
        }
    }

    private fun createJSFile(content: String, project: Project): JSFile =
        PsiFileFactory
            .getInstance(project)
            .createFileFromText("temp.js", JavaScriptFileType, content) as JSFile

    data class ValidationResult(val isValid: Boolean, val errorMessage: String?)
}
