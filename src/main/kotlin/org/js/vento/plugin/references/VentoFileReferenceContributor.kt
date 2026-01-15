/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.references

import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileSystemItem
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.PsiReferenceRegistrar
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceSet
import com.intellij.util.ProcessingContext
import org.js.vento.plugin.ImportElement
import org.js.vento.plugin.IncludeElement
import org.js.vento.plugin.LayoutElement
import org.js.vento.plugin.lexer.LexerTokens

/**
 * Provides file references for Vento layout, include, and import elements.
 * Enables navigation to files referenced in {{ layout "path/to/file.vto" }},
 * {{ include "path/to/file.vto" }}, and {{ import ... from "path/to/file.js" }} blocks.
 */
class VentoFileReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        // Register for layout elements
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(LayoutElement::class.java),
            VentoFileReferenceProvider(),
        )

        // Register for include elements
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(IncludeElement::class.java),
            VentoFileReferenceProvider(),
        )

        // Register for import elements
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(ImportElement::class.java),
            VentoFileReferenceProvider(),
        )
    }
}

/**
 * Reference provider that extracts file path from layout, include, and import elements.
 */
class VentoFileReferenceProvider : PsiReferenceProvider() {
    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
        // Find the FILE token which contains the file path
        val fileToken = element.node.findChildByType(LexerTokens.FILE) ?: return PsiReference.EMPTY_ARRAY

        val fileText = fileToken.text

        // Extract the path from quotes (handle both single and double quotes)
        val path = fileText.trim().removeSurrounding("\"").removeSurrounding("'")

        if (path.isEmpty()) {
            return PsiReference.EMPTY_ARRAY
        }

        // Calculate the offset of the FILE token relative to the element
        val fileTokenOffset = fileToken.startOffset - element.node.startOffset

        // Calculate the quote offset (start after opening quote)
        val quoteStartOffset = fileText.indexOfFirst { it == '"' || it == '\'' }
        if (quoteStartOffset == -1) {
            return PsiReference.EMPTY_ARRAY
        }

        // Create text range for the path (excluding quotes)
        val textRange =
            TextRange(
                fileTokenOffset + quoteStartOffset + 1,
                fileTokenOffset + quoteStartOffset + 1 + path.length,
            )

        // Create file reference set for path resolution
        val fileReferenceSet =
            object : FileReferenceSet(path, element, textRange.startOffset, null, true) {
                override fun isAbsolutePathReference(): Boolean = path.startsWith("/")

                override fun couldBeConvertedTo(relative: Boolean): Boolean = true

                override fun getDefaultContexts(): Collection<PsiFileSystemItem> {
                    val file = element.containingFile?.originalFile
                    val parent = file?.parent
                    return if (parent != null) listOf(parent) else emptyList()
                }
            }

        return fileReferenceSet.allReferences as Array<PsiReference>
    }
}
