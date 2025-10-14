/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.highlighting

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import org.js.vento.plugin.ForBlockElement
import org.js.vento.plugin.VariableElement
import org.js.vento.plugin.highlighting.validator.ForBlockValidator
import org.js.vento.plugin.highlighting.validator.JsExpressionValidator

/**
 * Annotator for Vento template language that validates JavaScript expressions within templates.
 * It checks the syntax and semantics of variable expressions and provides error annotations
 * when invalid expressions are detected.
 */
class Annotator : Annotator {
    /** Validator used to check JavaScript expression syntax and semantics */
    private val expressionValidator = JsExpressionValidator()
    private val forBlockValidator = ForBlockValidator()

    /**
     * Processes PSI elements to find and validate Vento variable expressions.
     *
     * @param element The PSI element to analyze
     * @param holder The holder to store annotations
     */
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element is VariableElement) {
            validateVariableExpression(element, holder)
        }

        if (element is ForBlockElement) {
            val result = forBlockValidator.isValidExpression(element)

            if (!result.isValid) {
                holder
                    .newAnnotation(HighlightSeverity.ERROR, result.errorMessage ?: "Invalid for block")
                    .range(element.textRange)
                    .create()
            }
        }
    }

    /**
     * Validates a Vento variable expression and creates error annotations if invalid.
     *
     * @param element The variable PSI element to validate
     * @param holder The holder to store potential error annotations
     */
    private fun validateVariableExpression(element: VariableElement, holder: AnnotationHolder) {
        val contentRange = element.getContentRange()
        if (contentRange.length == 0) return

        val content = contentRange.substring(element.text)
        val project = element.project

        val result = expressionValidator.isValidExpression(content, project)

        if (!result.isValid) {
            val annotationRange = contentRange.shiftRight(element.textRange.startOffset)
            holder
                .newAnnotation(HighlightSeverity.ERROR, result.errorMessage ?: "Invalid expression")
                .range(annotationRange)
                .create()
        }
    }
}
