/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser.injectors

import com.intellij.lang.injection.MultiHostInjector
import com.intellij.lang.injection.MultiHostRegistrar
import com.intellij.lang.javascript.JavascriptLanguage
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiLanguageInjectionHost
import com.intellij.psi.util.PsiTreeUtil
import org.js.vento.plugin.parser.VentoJavaScriptPsiElement
import org.js.vento.plugin.parser.VentoVariablePsiElement

/**
 * Contextual JavaScript injector that creates a shared scope with common
 * Vento context variables and functions available to all blocks.
 */
class VentoContextualJavaScriptInjector : MultiHostInjector {
    override fun getLanguagesToInject(registrar: MultiHostRegistrar, context: PsiElement) {
        if (!isFirstJavaScriptElementInFile(context)) return

        val file = context.containingFile ?: return
        val allJsElements = findAllJavaScriptElements(file)

        if (allJsElements.isNotEmpty()) {
            registrar.startInjecting(JavascriptLanguage)

            // Add common Vento context at the beginning using the first element
            val firstElement = allJsElements.first()
            if (firstElement is VentoJavaScriptPsiElement || firstElement is VentoVariablePsiElement) {
                // Create an empty range at the start for context injection
                val emptyRange = TextRange(0, 0)
                registrar.addPlace(getVentoContextPrefix(), "\n", firstElement as PsiElement as PsiLanguageInjectionHost, emptyRange)
            }

            allJsElements.forEachIndexed { index, element ->
                when (element) {
                    is VentoJavaScriptPsiElement -> {
                        val contentRange = element.getContentRange()
                        if (contentRange.length > 0) {
                            registrar.addPlace("\n// Block $index\n", "", element, contentRange)
                        }
                    }
                    is VentoVariablePsiElement -> {
                        val contentRange = element.getContentRange()
                        if (contentRange.length > 0) {
                            registrar.addPlace("\n// Variable $index\nlet output_$index = ", ";", element, contentRange)
                        }
                    }
                }
            }

            registrar.doneInjecting()
        }
    }

    override fun elementsToInjectIn(): List<Class<out PsiElement>> =
        listOf(VentoJavaScriptPsiElement::class.java, VentoVariablePsiElement::class.java)

    private fun getVentoContextPrefix(): String =
        """
        // Vento Template Context
        const data = {}; // Template data
        const filters = {}; // Available filters
        const helpers = {}; // Helper functions
        const it = {}; // Current item
        let output = ''; // Output accumulator
        const global = {};

        // Common template functions
        function include(template) { return ''; }
        function layout(name) { return ''; }
        function partial(name) { return ''; }
        function slugify(text) { return ''; }
        """.trimIndent()

    private fun isFirstJavaScriptElementInFile(context: PsiElement): Boolean {
        val file = context.containingFile ?: return false
        val allJsElements = findAllJavaScriptElements(file)
        return allJsElements.firstOrNull() == context
    }

    private fun findAllJavaScriptElements(file: PsiFile): List<PsiElement> {
        val jsElements = mutableListOf<PsiElement>()

        PsiTreeUtil.findChildrenOfType(file, VentoJavaScriptPsiElement::class.java)
            .forEach { jsElements.add(it) }

        PsiTreeUtil.findChildrenOfType(file, VentoVariablePsiElement::class.java)
            .forEach { jsElements.add(it) }

        return jsElements.sortedBy { it.textOffset }
    }
}
