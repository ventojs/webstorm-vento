/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.highlighting.validator

import com.intellij.lang.javascript.JavaScriptFileType
import com.intellij.lang.javascript.psi.JSBlockStatement
import com.intellij.lang.javascript.psi.JSExpressionStatement
import com.intellij.lang.javascript.psi.JSFile
import com.intellij.lang.javascript.psi.JSReturnStatement
import com.intellij.lang.javascript.psi.JSThrowStatement
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileFactory

class JsExpressionValidator {
    fun isValidExpression(content: String, project: Project): ValidationResult {
        val trimmed = content.trim()
        val exprPart = normalizeJsContent(trimmed.substringBefore("|>").trim())
        if (exprPart.isEmpty()) {
            return ValidationResult(false, "Empty expression")
        }

        return try {
            val file = createJSFile(exprPart, project)
            val statements = file.statements
            if (statements.size != 1) {
                return ValidationResult(false, "Variable blocks must contain one expression only")
            }

            val isExpression =
                when (val stmt = statements[0]) {
                    is JSExpressionStatement -> true
                    is JSReturnStatement -> stmt.expression != null
                    is JSThrowStatement -> stmt.expression != null
                    // treat bare block `{ ... }` as invalid (statement), unless you REALLY want to support it
                    is JSBlockStatement -> false
                    else -> false
                }

            if (!isExpression) {
                ValidationResult(false, "Variable blocks must contain one expression only")
            } else {
                ValidationResult(true, "Variable blocks must contain one expression only")
            }
        } catch (e: Exception) {
            ValidationResult(false, "Syntax error: ${e.message}")
        }
    }

    private fun createJSFile(content: String, project: Project): JSFile =
        PsiFileFactory
            .getInstance(project)
            .createFileFromText("temp.js", JavaScriptFileType, content) as JSFile
}

private fun normalizeJsContent(raw: String): String {
    val trimmed = raw.trim()

    // Strip Vento pipe part if you do "expr |> pipe"
    val expr =
        if (trimmed.contains("|>")) {
            trimmed.substring(0, trimmed.indexOf("|>")).trim()
        } else {
            trimmed
        }

    // If the JS expression looks like an object/array pattern or literal at the top,
    // wrap it in parentheses so the JS parser always sees it as an expression.
    val startsWithBlockLike =
        expr.startsWith("{") ||
            expr.startsWith("[") ||
            expr.startsWith("({") ||
            expr.startsWith("([")

    return if (startsWithBlockLike) "($expr);" else "$expr;"
}
