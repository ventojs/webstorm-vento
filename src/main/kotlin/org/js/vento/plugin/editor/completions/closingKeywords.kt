/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.editor.completions

import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import org.js.vento.plugin.Vento

fun closingKeywords(result: CompletionResultSet) {
    result.addElement(
        LookupElementBuilder
            .create("/if ")
            .withTailText("}}")
            .withTypeText("Vento")
            .withIcon(Vento.ICON)
            .bold(),
    )

    result.addElement(
        LookupElementBuilder
            .create("/for ")
            .withTailText("}}")
            .withTypeText("Vento")
            .withIcon(Vento.ICON)
            .bold(),
    )

    result.addElement(
        LookupElementBuilder
            .create("/set ")
            .withIcon(Vento.ICON)
            .withTailText("}}")
            .withTypeText("Vento")
            .bold(),
    )

    result.addElement(
        LookupElementBuilder
            .create("/function ")
            .withIcon(Vento.ICON)
            .withTailText("}}")
            .withTypeText("Vento")
            .bold(),
    )

    result.addElement(
        LookupElementBuilder
            .create("/export ")
            .withIcon(Vento.ICON)
            .withTailText("}}")
            .withTypeText("Vento")
            .bold(),
    )

    result.addElement(
        LookupElementBuilder
            .create("/layout ")
            .withIcon(Vento.ICON)
            .withTailText("}}")
            .withTypeText("Vento")
            .bold(),
    )

    result.addElement(
        LookupElementBuilder
            .create("/echo")
            .withIcon(Vento.ICON)
            .withTailText("}}")
            .withTypeText("Vento")
            .bold(),
    )
}
