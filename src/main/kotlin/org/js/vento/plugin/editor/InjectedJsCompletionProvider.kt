/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.editor

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.lang.injection.InjectedLanguageManager
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
open class InjectedJsCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet,
    ) {
        if (fileIgnored(parameters.originalFile)) return

        val injectedFile = parameters.position.containingFile
        val project = injectedFile.project
        val injectedLanguageManager = InjectedLanguageManager.getInstance(project)

        val hostFile = injectedLanguageManager.getTopLevelFile(injectedFile)
        val hostDocument = hostFile.viewProvider.document ?: return

        // Translate injected offset to host offset
        val injectedOffset = parameters.offset
        val hostOffset = injectedLanguageManager.injectedToHost(injectedFile, injectedOffset)

        val prefix = computePrefix(parameters)
        val resultSet = result.withPrefixMatcher(prefix)

        when {
            atBlockOpen(hostOffset, hostDocument) -> {
                blockModifiers(resultSet)
                openingKeywords(resultSet)
                closingKeywords(resultSet)
                result.stopHere()
            }

            insideBlock(hostOffset, hostDocument) -> {
                openingKeywords(resultSet)
                closingKeywords(resultSet)
            }

            else -> {
                openingKeywords(resultSet)
                closingKeywords(resultSet)
            }
        }
    }

    private fun computePrefix(parameters: CompletionParameters): String {
        val text = parameters.position.containingFile.text
        val offset = parameters.offset
        var start = offset
        while (start > 0 && text[start - 1].isJavaIdentifierPart()) {
            start--
        }
        return text.substring(start, offset)
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
