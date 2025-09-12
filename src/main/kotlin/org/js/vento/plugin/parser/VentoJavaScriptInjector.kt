/*
 * Copyright (c) 2023 Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.lang.injection.MultiHostInjector
import com.intellij.lang.injection.MultiHostRegistrar
import com.intellij.lang.javascript.JavascriptLanguage
import com.intellij.psi.PsiElement

class VentoJavaScriptInjector : MultiHostInjector {

    override fun getLanguagesToInject(registrar: MultiHostRegistrar, context: PsiElement) {
        if (context is VentoJavaScriptPsiElement) {
            val contentRange = context.getContentRange()
            if (contentRange.length > 0) {
                registrar.startInjecting(JavascriptLanguage)
                    .addPlace(null, null, context, contentRange)
                    .doneInjecting()
            }
        }
    }

    override fun elementsToInjectIn(): List<Class<out PsiElement>> {
        return listOf(VentoJavaScriptPsiElement::class.java)
    }
}