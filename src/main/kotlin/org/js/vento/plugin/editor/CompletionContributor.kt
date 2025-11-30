/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.editor

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.lang.javascript.JavascriptLanguage
import com.intellij.patterns.PlatformPatterns
import org.js.vento.plugin.VentoLanguage

/**
 * Provides code completion for Vento keywords after {{ delimiters.
 */
class CompletionContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement().withLanguage(VentoLanguage),
            BlockCompletionProvider(),
        )

        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement().withLanguage(JavascriptLanguage),
            InjectedJsCompletionProvider(),
        )
    }
}
