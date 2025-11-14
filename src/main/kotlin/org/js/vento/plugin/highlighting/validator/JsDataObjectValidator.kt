/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.highlighting.validator

import com.intellij.lang.javascript.JavaScriptFileType
import com.intellij.lang.javascript.psi.*
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileFactory

class JsDataObjectValidator {
    fun isValidDataObject(content: String, project: Project): ValidationResult {
        val trimmed = content.trim()
        if (trimmed.isEmpty()) {
            return ValidationResult(false, "Empty data object")
        }

        return try {
            val file = createJSFile(trimmed, project)
            val statements = file.statements
            if (statements.size != 1) {
                return ValidationResult(false, "Data object must contain one value only")
            }

            val stmt = statements[0]
            val expression =
                when (stmt) {
                    is JSExpressionStatement -> stmt.expression
                    is JSReturnStatement -> stmt.expression
                    else -> null
                }

            if (expression == null) {
                return ValidationResult(false, "Data object must be a valid data structure")
            }

            if (isValidDataExpression(expression)) {
                ValidationResult(true, "Valid data object")
            } else {
                ValidationResult(false, "Data object cannot contain functions or executable code")
            }
        } catch (e: Exception) {
            ValidationResult(false, "Syntax error: ${e.message}")
        }
    }

    private fun isValidDataExpression(expr: JSExpression?): Boolean {
        if (expr == null) return false

        return when (expr) {
            // Primitives
            is JSLiteralExpression -> true

            // Object literals
            is JSObjectLiteralExpression -> {
                expr.properties.all { prop ->
                    val value = prop.value
                    isValidDataExpression(value)
                }
            }

            // Array literals
            is JSArrayLiteralExpression -> {
                expr.expressions.all { isValidDataExpression(it) }
            }

            // Unary operators (e.g., -1, +5)
            is JSPrefixExpression -> {
                val operand = expr.expression
                isValidDataExpression(operand)
            }

            // Parenthesized expressions
            is JSParenthesizedExpression -> {
                isValidDataExpression(expr.innerExpression)
            }

            // Reference expressions (null, undefined, true, false)
            is JSReferenceExpression -> {
                val referenceName = expr.referenceName
                referenceName == "null" ||
                    referenceName == "undefined" ||
                    referenceName == "true" ||
                    referenceName == "false"
            }

            // Reject everything else: function calls, arrow functions, etc.
            else -> false
        }
    }

    private fun createJSFile(content: String, project: Project): JSFile {
        val normalized = normalizeContent(content)
        return PsiFileFactory
            .getInstance(project)
            .createFileFromText("temp.js", JavaScriptFileType, normalized) as JSFile
    }

    private fun normalizeContent(raw: String): String {
        val trimmed = raw.trim()

        // Wrap object/array literals in parentheses so parser treats them as expressions
        val startsWithBlockLike = trimmed.startsWith("{") || trimmed.startsWith("[")

        return if (startsWithBlockLike) "($trimmed);" else "$trimmed;"
    }
}
