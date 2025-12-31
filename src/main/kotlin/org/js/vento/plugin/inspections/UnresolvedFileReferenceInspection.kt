/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.inspections

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElementVisitor
import org.js.vento.plugin.ImportElement
import org.js.vento.plugin.IncludeElement
import org.js.vento.plugin.LayoutElement
import org.js.vento.plugin.lexer.LexerTokens

/**
 * Inspection that checks if file references in layout, include, and import elements can be resolved.
 * Shows a warning when a referenced file cannot be found.
 */
class UnresolvedFileReferenceInspection : LocalInspectionTool() {
    override fun getShortName(): String = "UnresolvedFileReferenceInspection"

    override fun getStaticDescription(): String {
        return "Reports unresolved file references in Vento layout, include, and import elements"
    }

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PsiElementVisitor() {
            override fun visitElement(element: com.intellij.psi.PsiElement) {
                super.visitElement(element)

                when (element) {
                    is LayoutElement, is IncludeElement, is ImportElement -> {
                        checkFileReference(element, holder)
                    }
                }
            }
        }
    }

    private fun checkFileReference(element: com.intellij.psi.PsiElement, holder: ProblemsHolder) {
        // Find the FILE token which contains the file path
        val fileToken = element.node.findChildByType(LexerTokens.FILE) ?: return

        val fileText = fileToken.text
        val path = fileText.trim().removeSurrounding("\"").removeSurrounding("'")

        if (path.isEmpty()) return

        // Get all references for this element
        val references = element.references
        if (references.isEmpty()) return

        // Check if the LAST reference can be resolved (the actual file, not intermediate directories)
        // FileReferenceSet breaks paths like './comment.vto' into segments: ['.', 'comment.vto']
        // We only care if the final segment (the actual file) resolves
        val lastReference = references.lastOrNull() ?: return

        var canResolve = false
        try {
            val resolved = lastReference.resolve()
            if (resolved != null) {
                canResolve = true
            }
        } catch (e: Exception) {
            // Ignore resolution errors
        }

        if (!canResolve) {
            val elementType = when (element) {
                is LayoutElement -> "layout"
                is IncludeElement -> "include"
                is ImportElement -> "import"
                else -> "file"
            }

            // Calculate the offset of the FILE token relative to the element
            val fileTokenOffset = fileToken.startOffset - element.node.startOffset

            // Calculate the quote offset
            val quoteStartOffset = fileText.indexOfFirst { it == '"' || it == '\'' }
            if (quoteStartOffset == -1) return

            // Create text range for the path (excluding quotes) relative to the element
            val textRange = TextRange(
                fileTokenOffset + quoteStartOffset + 1,
                fileTokenOffset + quoteStartOffset + 1 + path.length
            )

            holder.registerProblem(
                element,
                "Cannot resolve $elementType file '$path'",
                ProblemHighlightType.WARNING,
                textRange
            )
        }
    }
}
