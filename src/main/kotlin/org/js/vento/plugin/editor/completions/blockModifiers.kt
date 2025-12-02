/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.editor.completions

import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.lang.injection.InjectedLanguageManager
import com.intellij.psi.PsiFile
import com.intellij.psi.util.elementType
import org.js.vento.plugin.Vento

const val priority = 100.0

fun blockModifiers(result: CompletionResultSet) {
    result.addElement(
        PrioritizedLookupElement.withPriority(
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
                        InjectedLanguageManager
                            .getInstance(context.project)
                            .injectedToHost(injectedFile, injectedOffset)

                    val vBlockCloseOffset =
                        nextVBlockCloseOffset(hostOffset, hostFile)

                    // Insert in host document, not injected
                    val hostDocument = hostFile.viewProvider.document ?: return@withInsertHandler

                    hostDocument.insertString(hostOffset, "#")
                    if (vBlockCloseOffset != null) hostDocument.insertString(vBlockCloseOffset + 1, "#")

                    // Move caret in host
                    context.editor.caretModel.moveToOffset(hostOffset + 1)
                },
            priority,
        ),
    )

    result.addElement(
        PrioritizedLookupElement.withPriority(
            LookupElementBuilder
                .create("> ")
                .withIcon(Vento.ICON)
                .withTailText(" javascript statements }}")
                .withTypeText("Vento", true)
                .bold(),
            priority,
        ),
    )

    result.addElement(
        PrioritizedLookupElement.withPriority(
            LookupElementBuilder
                .create("- ")
                .withIcon(Vento.ICON)
                .withTailText(" left-trim")
                .withTypeText("Vento", true)
                .bold(),
            priority,
        ),
    )

    result.addElement(
        PrioritizedLookupElement.withPriority(
            LookupElementBuilder
                .create("")
                .withPresentableText("- -")
                .withIcon(Vento.ICON)
                .withTailText(" trim")
                .withTypeText("Vento")
                .bold()
                .withInsertHandler { context, elem ->
                    val injectedFile = context.file
                    val hostFile = InjectedLanguageManager.getInstance(context.project).getTopLevelFile(injectedFile)

                    // Translate offset from injected -> host
                    val injectedOffset = context.startOffset
                    val hostOffset =
                        InjectedLanguageManager
                            .getInstance(context.project)
                            .injectedToHost(injectedFile, injectedOffset)

                    val vBlockCloseOffset = nextVBlockCloseOffset(hostOffset, hostFile)

                    // Insert in host document, not injected
                    val hostDocument = hostFile.viewProvider.document ?: return@withInsertHandler

                    hostDocument.insertString(hostOffset, "-")
                    if (vBlockCloseOffset != null) {
                        hostDocument.insertString(vBlockCloseOffset + 1, "-")
                    }

                    // Move caret in host
                    context.editor.caretModel.moveToOffset(hostOffset + 1)
                },
            priority,
        ),
    )
}

private fun countCharactersBetween(
    startOffset: Int,
    file: PsiFile,
    closeTokenType: com.intellij.psi.tree.IElementType,
): Int? {
    var element = file.findElementAt(startOffset)
    while (element != null) {
        if (element.elementType == closeTokenType) {
            val closeOffset = element.textRange.startOffset
            return closeOffset - startOffset
        }
        element = element.nextSibling ?: element.parent?.nextSibling
    }
    return null
}

private fun nextVBlockCloseOffset(offset: Int, file: PsiFile): Int? {
    val text = file.text
    val closeTag = "}}"
    val idx = text.indexOf(closeTag, offset)
    return if (idx >= 0) idx else null
}
