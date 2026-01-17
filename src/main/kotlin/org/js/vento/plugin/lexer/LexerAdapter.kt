/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.lexer

import com.intellij.lexer.FlexAdapter
import com.intellij.lexer.FlexLexer

/**
 * Adapts the Flex-based VentoLexer for use in the IntelliJ Platform.
 * It assumes there's a generated Java class named VentoLexer
 * from a corresponding .flex file (VentoLexer.flex).
 */
class LexerAdapter
    @JvmOverloads
    constructor(debug: Boolean = false) : FlexAdapter(createLexer(debug)) {
        companion object {
            @JvmStatic
            @JvmOverloads
            fun createLexer(debug: Boolean = false): FlexLexer {
                val lexer = VentoLexer(null)
                lexer.setStrategy(LexerStrategyImpl(lexer, debug))
                return lexer as FlexLexer
            }

            @JvmStatic
            @JvmOverloads
            fun createTestLexer(
                debug: Boolean = false,
            ): FlexLexer = createLexer(debug)
        }
    }
