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
import org.js.vento.plugin.JavaScriptDataObjectElement
import org.js.vento.plugin.JavaScriptElement
import org.js.vento.plugin.JavaScriptExpressionElement

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

            // Add common Vento context at the beginning using the first element. Both parts
            // must be passed as the PREFIX (not the suffix) of this zero-width addPlace: content
            // registered as a suffix here is fragile under completion - IntelliJ's completion
            // machinery inserts a temporary "dummy identifier" at the caret and reparses to
            // compute candidates, then reverts; if any declared name here happens to match what
            // the user is typing, a JS smart pointer created against the temporary reparsed
            // state fails to resolve once the real state is restored ("Cannot restore JSVariable
            // ... from injected"), crashing completion. The same content placed in the prefix
            // does not exhibit this - verified empirically (see #240), not just theorized.
            val firstElement = allJsElements.first()
            if (firstElement is JavaScriptElement ||
                firstElement is JavaScriptExpressionElement ||
                firstElement is JavaScriptDataObjectElement
            ) {
                val emptyRange = TextRange(0, 0)
                registrar.addPlace(
                    getVentoContextPrefix() + getVariableDeclarations(allJsElements, file),
                    "",
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

                    is JavaScriptExpressionElement -> {
                        val contentRange = element.getContentRange()
                        if (contentRange.length > 0) {
//                            println("\n// Variable $index evaluation\noutput_$index = "+element.text)
                            registrar.addPlace(
                                "\n// Variable $index evaluation\noutput_$index = ",
                                ";\n",
                                element,
                                contentRange,
                            )
                        }
                    }

                    is JavaScriptDataObjectElement -> {
                        val contentRange = element.getContentRange()
                        if (contentRange.length > 0) {
                            registrar.addPlace(
                                "\n// Variable $index evaluation\noutput_$index = ",
                                ";\n",
                                element,
                                contentRange,
                            )
                        }
                    }
                }
            }

            registrar.doneInjecting()
        }
    }

    override fun elementsToInjectIn(): List<Class<out PsiElement>> =
        listOf(JavaScriptElement::class.java, JavaScriptExpressionElement::class.java, JavaScriptDataObjectElement::class.java)

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
            .findChildrenOfType(file, JavaScriptExpressionElement::class.java)
            .forEach { jsElements.add(it) }

        PsiTreeUtil
            .findChildrenOfType(file, JavaScriptDataObjectElement::class.java)
            .forEach { jsElements.add(it) }

        return jsElements.sortedBy { it.textOffset }
    }

    private fun getVariableDeclarations(allJsElements: List<PsiElement>, file: PsiFile): String {
        val declarations = StringBuilder()

        // Pre-declare variables that might be used across blocks
        allJsElements.forEachIndexed { index, element ->
            when (element) {
                is JavaScriptExpressionElement -> {
                    declarations.append("\nlet output_$index;")
                }
            }
        }

        // Declare the real variables bound by for/set/default/import blocks elsewhere in the
        // file, so an expression block can resolve them - e.g. `item` in `{{ for item of items
        // }}`. `var` (not `let`/`const`) is deliberate: the same name can legitimately repeat
        // across independent sequential blocks (two separate `for item of x` loops), and `var`
        // tolerates redeclaration in this flat synthetic scope where `let`/`const` would throw.
        val names = VentoVariableExtractor.collectAllVariableNames(file)
        if (names.isNotEmpty()) {
            declarations.append("\n// Template variables declared by for/set/default/import blocks\n")
            declarations.append("var ${names.joinToString(", ")};")
        }

        return declarations.toString()
    }
}
