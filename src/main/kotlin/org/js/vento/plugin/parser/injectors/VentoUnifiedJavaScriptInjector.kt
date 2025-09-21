
/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser.injectors

import com.intellij.lang.injection.MultiHostInjector
import com.intellij.lang.injection.MultiHostRegistrar
import com.intellij.lang.javascript.JavascriptLanguage
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import org.js.vento.plugin.parser.VentoJavaScriptPsiElement
import org.js.vento.plugin.parser.VentoVariablePsiElement

/**
 * Unified JavaScript injector that creates a single JavaScript scope for all
 * JavaScript blocks and variables in a Vento file. This allows variables
 * defined in one block to be accessible in other blocks without syntax errors.
 */
class VentoUnifiedJavaScriptInjector : MultiHostInjector {
    override fun getLanguagesToInject(registrar: MultiHostRegistrar, context: PsiElement) {
        // Only inject on the first JavaScript element we encounter in the file
        if (!isFirstJavaScriptElementInFile(context)) return

        val file = context.containingFile ?: return
        val allJsElements = findAllJavaScriptElements(file)

        if (allJsElements.isNotEmpty()) {
            registrar.startInjecting(JavascriptLanguage)

            allJsElements.forEachIndexed { index, element ->
                when (element) {
                    is VentoJavaScriptPsiElement -> {
                        val contentRange = element.getContentRange()
                        if (contentRange.length > 0) {
                            // Add a newline separator between blocks for better readability
                            val prefix = if (index > 0) "\n" else ""
                            registrar.addPlace(prefix, "", element, contentRange)
                        }
                    }
                    is VentoVariablePsiElement -> {
                        val contentRange = element.getContentRange()
                        if (contentRange.length > 0) {
                            // Wrap variables in output assignments
                            val prefix = if (index > 0) "\n" else ""
                            registrar.addPlace("${prefix}let output_$index = ", ";", element, contentRange)
                        }
                    }
                }
            }

            registrar.doneInjecting()
        }
    }

    override fun elementsToInjectIn(): List<Class<out PsiElement>> =
        listOf(VentoJavaScriptPsiElement::class.java, VentoVariablePsiElement::class.java)

    private fun isFirstJavaScriptElementInFile(context: PsiElement): Boolean {
        val file = context.containingFile ?: return false
        val allJsElements = findAllJavaScriptElements(file)
        return allJsElements.firstOrNull() == context
    }

    private fun findAllJavaScriptElements(file: PsiFile): List<PsiElement> {
        val jsElements = mutableListOf<PsiElement>()

        // Find all JavaScript blocks
        PsiTreeUtil.findChildrenOfType(file, VentoJavaScriptPsiElement::class.java)
            .forEach { jsElements.add(it) }

        // Find all variable blocks
        PsiTreeUtil.findChildrenOfType(file, VentoVariablePsiElement::class.java)
            .forEach { jsElements.add(it) }

        // Sort by text offset to maintain order in the file
        return jsElements.sortedBy { it.textOffset }
    }
}
