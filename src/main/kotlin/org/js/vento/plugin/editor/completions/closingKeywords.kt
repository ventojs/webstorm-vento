/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.editor.completions

import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import org.js.vento.plugin.Vento

fun closingKeywords(result: CompletionResultSet) {
    val priority = 50.0
    result.addElement(
        PrioritizedLookupElement.withPriority(
            LookupElementBuilder
                .create("/if")
                .withTailText("}}")
                .withTypeText("Vento")
                .withIcon(Vento.ICON)
                .bold(),
            priority,
        ),
    )

    result.addElement(
        PrioritizedLookupElement.withPriority(
            LookupElementBuilder
                .create("/for")
                .withTailText("}}")
                .withTypeText("Vento")
                .withIcon(Vento.ICON)
                .bold(),
            priority,
        ),
    )

    result.addElement(
        PrioritizedLookupElement.withPriority(
            LookupElementBuilder
                .create("/set")
                .withIcon(Vento.ICON)
                .withTailText("}}")
                .withTypeText("Vento")
                .bold(),
            priority,
        ),
    )

    result.addElement(
        PrioritizedLookupElement.withPriority(
            LookupElementBuilder
                .create("/function")
                .withIcon(Vento.ICON)
                .withTailText("}}")
                .withTypeText("Vento")
                .bold(),
            priority,
        ),
    )

    result.addElement(
        PrioritizedLookupElement.withPriority(
            LookupElementBuilder
                .create("/export")
                .withIcon(Vento.ICON)
                .withTailText("}}")
                .withTypeText("Vento")
                .bold(),
            priority,
        ),
    )

    result.addElement(
        PrioritizedLookupElement.withPriority(
            LookupElementBuilder
                .create("/layout")
                .withIcon(Vento.ICON)
                .withTailText("}}")
                .withTypeText("Vento")
                .bold(),
            priority,
        ),
    )

    result.addElement(
        PrioritizedLookupElement.withPriority(
            LookupElementBuilder
                .create("/echo")
                .withIcon(Vento.ICON)
                .withTailText("}}")
                .withTypeText("Vento")
                .bold(),
            priority,
        ),
    )
}
