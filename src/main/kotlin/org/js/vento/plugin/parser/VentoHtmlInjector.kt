/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.lang.html.HTMLLanguage
import com.intellij.lang.injection.MultiHostInjector
import com.intellij.lang.injection.MultiHostRegistrar
import com.intellij.psi.PsiElement
import org.js.vento.plugin.lexer.VentoElementImpl

/**
 * Injects HTML language into default content blocks of Vento templates.
 *
 * The default content (outside of Vento-specific constructs like comments, variables, and JS blocks)
 * is represented by VentoElementImpl PSI elements. This injector delegates their content to the
 * IntelliJ HTML parser for proper highlighting and features.
 */
class VentoHtmlInjector : MultiHostInjector {
    override fun getLanguagesToInject(registrar: MultiHostRegistrar, context: PsiElement) {
        if (context is VentoElementImpl) {
            val textLength = context.textLength
            if (textLength > 0) {
                val range = context.getContentRange()
                if (!range.isEmpty) {
                    registrar
                        .startInjecting(HTMLLanguage.INSTANCE)
                        .addPlace(null, null, context, range)
                        .doneInjecting()
                }
            }
        }
    }

    override fun elementsToInjectIn(): List<Class<out PsiElement>> = listOf(VentoElementImpl::class.java)
}
