/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.lang.injection.MultiHostInjector
import com.intellij.lang.injection.MultiHostRegistrar
import com.intellij.lang.javascript.JavascriptLanguage
import com.intellij.psi.PsiElement

/**
 * Implements a `MultiHostInjector` for handling JavaScript code injection
 * within Vento template files.
 *
 * This class processes PSI elements representing JavaScript blocks in Vento templates
 * and enables language injection for these blocks, utilizing the `JavascriptLanguage`.
 *
 * It works with `VentoJavaScriptPsiElement`, extracting the relevant code content
 * via its `getContentRange` method for precise injection.
 *
 * Methods:
 * - `getLanguagesToInject`: Configures the language injection for JavaScript blocks found in the specified PSI context.
 * - `elementsToInjectIn`: Returns the list of PSI element types that this injector targets for injection.
 *
 * See also:
 * - `VentoJavaScriptPsiElement` which represents the JavaScript blocks in the PSI tree.
 */
class VentoJavaScriptInjector : MultiHostInjector {
    override fun getLanguagesToInject(
        registrar: MultiHostRegistrar,
        context: PsiElement,
    ) {
        if (context is VentoJavaScriptPsiElement) {
            val contentRange = context.getContentRange()
            if (contentRange.length > 0) {
                registrar
                    .startInjecting(JavascriptLanguage)
                    .addPlace(null, null, context, contentRange)
                    .doneInjecting()
            }
        }
    }

    override fun elementsToInjectIn(): List<Class<out PsiElement>> = listOf(VentoJavaScriptPsiElement::class.java)
}
