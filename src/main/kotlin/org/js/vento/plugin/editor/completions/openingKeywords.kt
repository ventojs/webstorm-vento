/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.editor.completions

import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import org.js.vento.plugin.Vento

fun openingKeywords(result: CompletionResultSet) {
    result.addElement(
        LookupElementBuilder
            .create("if ")
            .withTailText(" condition }}")
            .withTypeText("Vento", true)
            .withIcon(Vento.ICON)
            .bold(),
    )

    result.addElement(
        LookupElementBuilder
            .create("else if ")
            .withTailText(" condition }}")
            .withTypeText("Vento", true)
            .withIcon(Vento.ICON)
            .bold(),
    )

    result.addElement(
        LookupElementBuilder
            .create("else ")
            .withIcon(Vento.ICON)
            .withTypeText("Vento")
            .bold(),
    )

    result.addElement(
        LookupElementBuilder
            .create("for ")
            .withTailText(" value of collection }}", true)
            .withTypeText("Vento")
            .withIcon(Vento.ICON)
            .bold(),
    )

    result.addElement(
        LookupElementBuilder
            .create("set ")
            .withIcon(Vento.ICON)
            .withTailText(" name }}", true)
            .withTypeText("Vento")
            .bold(),
    )

    result.addElement(
        LookupElementBuilder
            .create("set  =")
            .withIcon(Vento.ICON)
            .withTailText(" name = value }}", true)
            .withTypeText("Vento")
            .withInsertHandler { context, _ ->
                val offset = context.tailOffset
                context.editor.caretModel.moveToOffset(offset - 2)
            }.bold(),
    )

    result.addElement(
        LookupElementBuilder
            .create("function")
            .withIcon(Vento.ICON)
            .withTypeText("Vento")
            .bold(),
    )

    result.addElement(
        LookupElementBuilder
            .create("export")
            .withIcon(Vento.ICON)
            .withTypeText("Vento")
            .bold(),
    )

    result.addElement(
        LookupElementBuilder
            .create("layout")
            .withIcon(Vento.ICON)
            .withTypeText("Vento")
            .bold(),
    )

    result.addElement(
        LookupElementBuilder
            .create("include")
            .withIcon(Vento.ICON)
            .withTypeText("Vento")
            .bold(),
    )

    result.addElement(
        LookupElementBuilder
            .create("echo")
            .withIcon(Vento.ICON)
            .withTypeText("Vento")
            .bold(),
    )

    result.addElement(
        LookupElementBuilder
            .create("import")
            .withIcon(Vento.ICON)
            .withTypeText("Vento")
            .bold(),
    )
}
