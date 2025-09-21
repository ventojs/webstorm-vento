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
 * JavaScript injector specifically for Vento variable expressions.
 *
 * This injector wraps variable expressions in a JavaScript assignment context,
 * enabling proper parsing and validation of single JavaScript expressions
 * within variable blocks like {{ "hello world" }}.
 */
class VentoVariableJavaScriptInjector : MultiHostInjector {
    override fun getLanguagesToInject(registrar: MultiHostRegistrar, context: PsiElement) {
        if (context is VentoVariablePsiElement) {
            val contentRange = context.getContentRange()
            if (contentRange.length > 0) {
                registrar
                    .startInjecting(JavascriptLanguage)
                    // Wrap the expression in a variable assignment to ensure it's a valid JS statement
                    .addPlace("let output = ", ";", context, contentRange)
                    .doneInjecting()
            }
        }
    }

    override fun elementsToInjectIn(): List<Class<out PsiElement>> = listOf(VentoVariablePsiElement::class.java)
}
