/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.highlighting

import com.intellij.lang.javascript.JavaScriptFileType
import com.intellij.lang.javascript.psi.JSBlockStatement
import com.intellij.lang.javascript.psi.JSExpressionStatement
import com.intellij.lang.javascript.psi.JSFile
import com.intellij.lang.javascript.psi.JSLabeledStatement
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiWhiteSpace

private const val RULE = "Variable blocks must contain one expression only"
private const val PIPE_RULE = "$RULE followed by a Vento pipe expression"

private const val PIPE = "|>"

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
        val trimmed = content.trim()
        val rule = if (trimmed.contains(PIPE)) PIPE_RULE else RULE
        val segment = if (trimmed.contains(PIPE)) trimmed.take(trimmed.indexOf(PIPE)) else content

        if (segment.isEmpty()) return ValidationResult(false, "Empty expression")

        try {
            val file = createJSFile(segment, project)
            val elements = file.statements

            return when {
                elements.size > 1 -> ValidationResult(false, rule)
                elements[0] is JSExpressionStatement -> ValidationResult(true, rule)
                elements[0] is JSBlockStatement &&
                    elements[0].children.filter {
                        it !is PsiWhiteSpace
                    }[1] is JSLabeledStatement -> ValidationResult(true, rule) // object
                elements[0] is JSBlockStatement &&
                    elements[0].children.filter { it !is PsiWhiteSpace }[1] is JSExpressionStatement &&
                    elements[0].children.filter { it !is PsiWhiteSpace }[3] is JSExpressionStatement -> ValidationResult(true, rule) // json
                else -> ValidationResult(false, rule)
            }
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
