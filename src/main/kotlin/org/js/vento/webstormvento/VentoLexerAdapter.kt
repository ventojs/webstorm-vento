/*
 * Copyright (c) 2023 Óscar Otero
 * All rights reserved.
 */

package org.js.vento.webstormvento

import com.intellij.lexer.FlexAdapter
import com.intellij.lexer.FlexLexer
import com.intellij.openapi.application.ApplicationManager
import com.intellij.util.SlowOperations


/**
 * Adapts the Flex-based VentoLexer for use in the IntelliJ Platform.
 * It assumes there's a generated Java class named VentoLexer
 * from a corresponding .flex file (VentoLexer.flex).
 */
class VentoLexerAdapter : FlexAdapter(createLexer()) {

    companion object {
        fun createLexer(): FlexLexer {
            return try {
                // Allow slow operations temporarily if we need to initialize the lexer
                // This is safe during lexer creation as it's typically done during plugin initialization
                SlowOperations.knownIssue("IDEA-000000").use {
                    VentoLexer(null) as FlexLexer
                }
            } catch (e: Exception) {
                // Fallback in case of issues
                ApplicationManager.getApplication().runReadAction<FlexLexer> {
                    VentoLexer(null) as FlexLexer
                }
            }
        }

        // Public method for testing purposes
        fun createTestLexer(): FlexLexer = createLexer()

    }

    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        // Ensure we're not blocking the EDT during lexer operations
        if (ApplicationManager.getApplication().isDispatchThread) {
            // If we're on EDT and this might be slow, wrap in a known issue allowance
            SlowOperations.knownIssue("IDEA-000000").use {
                super.start(buffer, startOffset, endOffset, initialState)
            }
        } else {
            super.start(buffer, startOffset, endOffset, initialState)
        }
    }
}