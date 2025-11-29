/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.editor

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionInitializationContext
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
import com.intellij.util.ProcessingContext
import org.js.vento.plugin.VentoLanguage

/**
 * Provides code completion for Vento keywords after {{ delimiters.
 */
class VentoCompletionContributor : CompletionContributor() {
    init {
        // Extend basic completion with Vento keywords
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement().withLanguage(VentoLanguage),
            VentoKeywordCompletionProvider(),
        )
    }

    /**
     * Provides completion suggestions for Vento keywords.
     */
    private class VentoKeywordCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(
            parameters: CompletionParameters,
            context: ProcessingContext,
            result: CompletionResultSet,
        ) {
            val position = parameters.position
            val element = position.parent ?: position
            val text = position.containingFile.text
            val offset = parameters.offset

            // Check if we're inside a Vento block (after {{)
            if (!isInsideVentoBlock(text, offset)) {
                return
            }

            // Check if we're trying to close a block (after "{{ /")
            val isClosingBlock = isAfterSlash(text, offset)
            val afterOpeningBraces = isImmediatelyAfterOpeningBraces(text, offset)

            if (isClosingBlock) {
                // Suggest closing keywords
                addClosingKeywords(result)
            } else if (afterOpeningBraces) {
                addBlockModifiers(result)
            } else {
                // Suggest opening keywords
                addOpeningKeywords(result)
                addClosingKeywords(result)
            }
        }

        /**
         * Checks if the cursor is inside a Vento block (after {{).
         */
        private fun isInsideVentoBlock(text: String, offset: Int): Boolean {
            // Look backward for the nearest {{ or }}
            var i = offset - 1
            while (i >= 1) {
                if (text[i] == '}' && text[i - 1] == '}') {
                    // Found closing braces first, we're outside a block
                    return false
                }
                if (text[i] == '{' && text[i - 1] == '{') {
                    // Found opening braces first, we're inside a block
                    return true
                }
                i--
            }
            return false
        }

        /**
         * Checks if the cursor is positioned immediately after {{ with only whitespace or dummy identifier in between.
         */
        private fun isImmediatelyAfterOpeningBraces(text: String, offset: Int): Boolean {
            // Look backward skipping whitespace and dummy identifier
            var i = offset - 1
            while (i >= 0 &&
                (text[i] == CompletionInitializationContext.DUMMY_IDENTIFIER_TRIMMED.firstOrNull())
            ) {
                i--
            }

            // Check if we found {{ immediately before
            return i >= 1 && text[i] == '{' && text[i - 1] == '{'
        }

        /**
         * Checks if we're after a slash character (for closing tags).
         */
        private fun isAfterSlash(text: String, offset: Int): Boolean {
            // Look backward for a slash after {{
            var i = offset - 1
            while (i >= 0 &&
                (text[i].isWhitespace() || text[i] == CompletionInitializationContext.DUMMY_IDENTIFIER_TRIMMED.firstOrNull())
            ) {
                i--
            }

            if (i >= 0 && text[i] == '/') {
                // Found slash, check if there's {{ before it
                i--
                while (i >= 0 && text[i].isWhitespace()) {
                    i--
                }
                if (i >= 1 && text[i] == '{' && text[i - 1] == '{') {
                    return true
                }
            }
            return false
        }

        /**
         * Adds opening keyword suggestions.
         */
        private fun addBlockModifiers(result: CompletionResultSet) {
            result.addElement(
                LookupElementBuilder
                    .create("# ")
                    .withTailText("comment #}}")
                    .withTypeText("Comment", true),
            )

            result.addElement(
                LookupElementBuilder
                    .create("> ")
                    .withTailText("javascript }}")
                    .withTypeText("JavaScript block", true),
            )
        }

        /**
         * Adds opening keyword suggestions.
         */
        private fun addOpeningKeywords(result: CompletionResultSet) {
            result.addElement(
                LookupElementBuilder
                    .create("if")
                    .withTailText(" condition }}")
                    .withTypeText("Conditional block", true)
                    .bold(),
            )

            result.addElement(
                LookupElementBuilder
                    .create("for")
                    .withTailText(" item of collection }}")
                    .withTypeText("Loop block", true)
                    .bold(),
            )

            result.addElement(
                LookupElementBuilder
                    .create("set")
                    .withTailText(" variable = value }}")
                    .withTypeText("Variable assignment", true)
                    .bold(),
            )

            result.addElement(
                LookupElementBuilder
                    .create("function")
                    .withTailText(" name(args) }}")
                    .withTypeText("Function definition", true)
                    .bold(),
            )

            result.addElement(
                LookupElementBuilder
                    .create("export")
                    .withTailText(" name }}")
                    .withTypeText("Export block", true)
                    .bold(),
            )

            result.addElement(
                LookupElementBuilder
                    .create("layout")
                    .withTailText(" template }}")
                    .withTypeText("Layout block", true)
                    .bold(),
            )

            result.addElement(
                LookupElementBuilder
                    .create("include")
                    .withTailText(" template }}")
                    .withTypeText("Include template", true)
                    .bold(),
            )

            result.addElement(
                LookupElementBuilder
                    .create("echo")
                    .withTailText(" expression }}")
                    .withTypeText("Output expression", true)
                    .bold(),
            )

            result.addElement(
                LookupElementBuilder
                    .create("import")
                    .withTailText(" module from path }}")
                    .withTypeText("Import statement", true)
                    .bold(),
            )
        }

        /**
         * Adds closing keyword suggestions.
         */
        private fun addClosingKeywords(result: CompletionResultSet) {
            result.addElement(
                LookupElementBuilder
                    .create("/if")
                    .withTypeText("Close if block", true)
                    .bold(),
            )

            result.addElement(
                LookupElementBuilder
                    .create("/for")
                    .withTypeText("Close for loop", true)
                    .bold(),
            )

            result.addElement(
                LookupElementBuilder
                    .create("/function")
                    .withTypeText("Close function", true)
                    .bold(),
            )

            result.addElement(
                LookupElementBuilder
                    .create("/export")
                    .withTypeText("Close export", true)
                    .bold(),
            )

            result.addElement(
                LookupElementBuilder
                    .create("/layout")
                    .withTypeText("Close layout", true)
                    .bold(),
            )

            result.addElement(
                LookupElementBuilder
                    .create("/set")
                    .withTypeText("Close set block", true)
                    .bold(),
            )

            result.addElement(
                LookupElementBuilder
                    .create("/echo")
                    .withTypeText("Close echo block", true)
                    .bold(),
            )
        }
    }
}
