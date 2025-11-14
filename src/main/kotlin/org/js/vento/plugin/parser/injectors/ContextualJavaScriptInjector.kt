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
import org.js.vento.plugin.JavaScriptElement
import org.js.vento.plugin.JavascriptExpressionElement

/**
 * Contextual JavaScript injector that creates a shared scope with common
 * Vento context variables and functions available to all blocks.
 */
class ContextualJavaScriptInjector : MultiHostInjector {
    override fun getLanguagesToInject(registrar: MultiHostRegistrar, context: PsiElement) {
        if (!isFirstJavaScriptElementInFile(context)) return

        val file = context.containingFile ?: return
        val allJsElements = findAllJavaScriptElements(file)

        if (allJsElements.isNotEmpty()) {
            registrar.startInjecting(JavascriptLanguage)

            // Add common Vento context at the beginning using the first element
            val firstElement = allJsElements.first()
            if (firstElement is JavaScriptElement || firstElement is JavascriptExpressionElement) {
                val emptyRange = TextRange(0, 0)
                registrar.addPlace(
                    getVentoContextPrefix(),
                    getVariableDeclarations(allJsElements),
                    firstElement as PsiElement as PsiLanguageInjectionHost,
                    emptyRange,
                )
            }

            allJsElements.forEachIndexed { index, element ->
                when (element) {
                    is JavaScriptElement -> {
                        val contentRange = element.getContentRange()
                        if (contentRange.length > 0) {
                            registrar.addPlace("\n// Variable $index evaluation\n", "\n", element, contentRange)
                        }
                    }

                    is JavascriptExpressionElement -> {
                        val contentRange = element.getContentRange()
                        if (contentRange.length > 0) {
//                            println("\n// Variable $index evaluation\noutput_$index = "+element.text)
                            registrar.addPlace("\n// Variable $index evaluation\noutput_$index = ", ";\n", element, contentRange)
                        }
                    }
                }
            }

            registrar.doneInjecting()
        }
    }

    override fun elementsToInjectIn(): List<Class<out PsiElement>> =
        listOf(JavaScriptElement::class.java, JavascriptExpressionElement::class.java)

    private fun getVentoContextPrefix(): String =
        """
        /* eslint-disable */
        // @ts-nocheck
        // noinspection JSUnusedLocalSymbols,JSUnusedGlobalSymbols,JSUnreachableCode,JSUnresolvedVariable
        // Vento Template Context
        const data = {}; // Template data
        const filters = {}; // Available filters
        const helpers = {}; // Helper functions
        const it = {}; // Current item
        let output = ''; // Output accumulator
        const global = {};
        const content = {};

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

        PsiTreeUtil
            .findChildrenOfType(file, JavaScriptElement::class.java)
            .forEach { jsElements.add(it) }

        PsiTreeUtil
            .findChildrenOfType(file, JavascriptExpressionElement::class.java)
            .forEach { jsElements.add(it) }

        return jsElements.sortedBy { it.textOffset }
    }

    private fun getVariableDeclarations(allJsElements: List<PsiElement>): String {
        val declarations = StringBuilder()

        // Pre-declare variables that might be used across blocks
        allJsElements.forEachIndexed { index, element ->
            when (element) {
                is JavascriptExpressionElement -> {
                    declarations.append("\nlet output_$index;")
                }
            }
        }

        // Add common variables that might be declared in JS blocks
        declarations.append(
            """
            // Common template variables (will be hoisted if declared in blocks)
            var result, temp, value, item, items, i, j, key, content;
            """.trimIndent(),
        )

        return declarations.toString()
    }
}
