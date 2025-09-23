
/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.highlighting

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import org.js.vento.plugin.parser.VentoVariablePsiElement

class VentoAnnotator : Annotator {
    private val expressionValidator = VentoJavaScriptExpressionValidator()

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element is VentoVariablePsiElement) {
            validateVariableExpression(element, holder)
        }
    }

    private fun validateVariableExpression(element: VentoVariablePsiElement, holder: AnnotationHolder) {
        val contentRange = element.getContentRange()
        if (contentRange.length == 0) return

        val content = contentRange.substring(element.text)
        val project = element.project

        val result = expressionValidator.isValidExpression(content, project)

        if (!result.isValid) {
            val annotationRange = contentRange.shiftRight(element.textRange.startOffset)
            holder.newAnnotation(HighlightSeverity.ERROR, result.errorMessage ?: "Invalid expression")
                .range(annotationRange)
                .create()
        }
    }
}
