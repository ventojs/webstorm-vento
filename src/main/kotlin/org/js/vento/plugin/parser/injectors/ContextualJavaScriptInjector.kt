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
import org.js.vento.plugin.JavaScriptDataObjectElement
import org.js.vento.plugin.JavaScriptElement
import org.js.vento.plugin.JavaScriptExpressionElement

/**
 * Contextual JavaScript injector that gives every `{{ }}` JS expression/statement in a file its
 * own injected document, each prefixed with the same shared Vento context (common globals plus
 * the real variable names bound by for/set/default/import blocks elsewhere in the file) so
 * completion and resolution work consistently no matter which block the caret is in.
 *
 * Two non-obvious constraints shape this implementation - both verified empirically, not just
 * theorized:
 *
 * 1. This used to build ONE injected document meant to be shared across every JS element in the
 *    file, registered only from the callback for the first such element (via `addPlace` calls
 *    anchored to each of the *other*, unrelated sibling elements too). That doesn't reliably
 *    work: the injection framework only associates an injected document with whichever host
 *    triggered `getLanguagesToInject`, so querying injection directly at any element other than
 *    the first (`InjectedLanguageManager.findInjectedElementAt`/`getInjectedPsiFiles`) returned
 *    nothing - e.g. a variable declared by an earlier `for` loop was invisible to completion
 *    everywhere except inside that same first block. Giving each element its own self-contained
 *    document (still carrying the same shared context) fixes that: every element is now its own
 *    host, so every element gets a working injection (see #241).
 *
 * 2. The shared context must be passed as the *prefix* parameter of `addPlace`, not the suffix.
 *    Content registered as a suffix at a zero-width range is unstable across completion's normal
 *    "insert a temporary dummy identifier, reparse, compute candidates, revert" cycle: a JS smart
 *    pointer created against the temporarily-reparsed injected document fails to resolve once the
 *    real state is restored ("Cannot restore JSVariable ... from injected"), which aborts
 *    completion with no results. The same content as a prefix does not exhibit this (see #240).
 */
class ContextualJavaScriptInjector : MultiHostInjector {
    override fun getLanguagesToInject(registrar: MultiHostRegistrar, context: PsiElement) {
        if (context !is JavaScriptElement && context !is JavaScriptExpressionElement && context !is JavaScriptDataObjectElement) {
            return
        }
        val host = context as PsiLanguageInjectionHost
        val file = context.containingFile ?: return

        registrar.startInjecting(JavascriptLanguage)

        registrar.addPlace(
            getVentoContextPrefix() + getVariableDeclarations(file),
            "",
            host,
            TextRange(0, 0),
        )

        when (context) {
            is JavaScriptElement -> {
                val contentRange = context.getContentRange()
                if (contentRange.length > 0) {
                    registrar.addPlace("\n", "\n", context, contentRange)
                }
            }

            is JavaScriptExpressionElement -> {
                val contentRange = context.getContentRange()
                if (contentRange.length > 0) {
                    registrar.addPlace("\noutput = ", ";\n", context, contentRange)
                }
            }

            is JavaScriptDataObjectElement -> {
                val contentRange = context.getContentRange()
                if (contentRange.length > 0) {
                    registrar.addPlace("\noutput = ", ";\n", context, contentRange)
                }
            }
        }

        registrar.doneInjecting()
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

    private fun getVariableDeclarations(file: PsiFile): String {
        // Declare the real variables bound by for/set/default/import blocks elsewhere in the
        // file, so an expression block can resolve them - e.g. `item` in `{{ for item of items
        // }}`. `var` (not `let`/`const`) is deliberate: the same name can legitimately repeat
        // across independent sequential blocks (two separate `for item of x` loops), and `var`
        // tolerates redeclaration in this flat synthetic scope where `let`/`const` would throw.
        val names = VentoVariableExtractor.collectAllVariableNames(file)
        if (names.isEmpty()) return ""
        return "\n// Template variables declared by for/set/default/import blocks\nvar ${names.joinToString(", ")};"
    }
}
