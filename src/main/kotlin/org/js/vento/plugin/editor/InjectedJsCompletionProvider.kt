/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.editor

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.lang.javascript.JavaScriptFileType
import com.intellij.openapi.editor.Document
import com.intellij.psi.PsiFile
import com.intellij.util.ProcessingContext
import org.js.vento.plugin.editor.completions.blockModifiers
import org.js.vento.plugin.editor.completions.closingKeywords
import org.js.vento.plugin.editor.completions.openingKeywords
import org.js.vento.plugin.file.VentoFileType

/**
 * Provides completion suggestions for Vento keywords.
 */
class InjectedJsCompletionProvider : CompletionProvider<CompletionParameters>() {
    companion object {
        var counter = 0
    }

    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet,
    ) {
        println("injectedJS: " + counter++)

        if (fileIgnored(parameters.originalFile)) return

        val document = parameters.editor.document
        val offset = parameters.editor.caretModel.offset

        when {
            atBlockOpen(offset, document) -> blockModifiers(result)
            insideBlock(offset, document) -> {
                openingKeywords(result)
                closingKeywords(result)
            }
            else -> return
        }
    }

    private fun fileIgnored(file: PsiFile): Boolean =
        file.fileType != VentoFileType && file.fileType != JavaScriptFileType

    private fun atBlockOpen(offset: Int, document: Document): Boolean {
        if (offset >= 2) {
            val expected = "{{"
            val found = document.charsSequence.subSequence(offset - 2, offset).toString()
            if (found == expected) return true
        }
        return false
    }

    private fun insideBlock(offset: Int, document: Document): Boolean {
        if (offset >= 3) {
            val expected = "{{ "
            val found = document.charsSequence.subSequence(offset - 3, offset).toString()
            if (found == expected) return true
        }
        return false
    }
}
