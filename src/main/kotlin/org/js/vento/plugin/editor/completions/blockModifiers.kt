/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.editor.completions

import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.lang.injection.InjectedLanguageManager
import com.intellij.psi.PsiFile
import com.intellij.psi.util.elementType
import org.js.vento.plugin.Vento
import org.js.vento.plugin.lexer.LexerTokens

fun blockModifiers(result: CompletionResultSet) {
    result.addElement(
        LookupElementBuilder
            .create("")
            .withPresentableText("#")
            .withIcon(Vento.ICON)
            .withTypeText("Vento")
            .bold()
            .withInsertHandler { context, elem ->
                val injectedFile = context.file
                val hostFile = InjectedLanguageManager.getInstance(context.project).getTopLevelFile(injectedFile)

                // Translate offset from injected -> host
                val injectedOffset = context.startOffset
                val hostOffset =
                    InjectedLanguageManager.getInstance(context.project).injectedToHost(injectedFile, injectedOffset)

                val vBlockCloseOffset = nextVBlockCloseOffset(hostOffset, hostFile)

                // Insert in host document, not injected
                val hostDocument = hostFile.viewProvider.document ?: return@withInsertHandler

                hostDocument.insertString(hostOffset, "# ")
                if (vBlockCloseOffset != null) {
                    hostDocument.insertString(vBlockCloseOffset + 3, "#")
                }

                // Move caret in host
                context.editor.caretModel.moveToOffset(hostOffset + 2)
            },
    )

    result.addElement(
        LookupElementBuilder
            .create("> ")
            .withIcon(Vento.ICON)
            .withTailText(" javascript statements }}")
            .withTypeText("Vento", true)
            .bold(),
    )

    result.addElement(
        LookupElementBuilder
            .create("- ")
            .withIcon(Vento.ICON)
            .withTypeText("Vento", true)
            .bold(),
    )
}

private fun nextVBlockCloseOffset(offset: Int, file: PsiFile): Int? {
    var element = file.findElementAt(offset)
    while (element != null) {
        if (element.elementType == LexerTokens.VBLOCK_CLOSE) {
            return element.textRange.startOffset
        }
        element = element.nextSibling ?: element.parent?.nextSibling
    }
    return null
}
