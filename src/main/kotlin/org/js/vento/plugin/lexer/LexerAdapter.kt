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
class LexerAdapter @JvmOverloads constructor(
    val debug: Boolean = false,
    val loopTolerance: Int = 1000,
) : FlexAdapter(createLexer(debug, loopTolerance)) {
    companion object {
        @JvmStatic
        @JvmOverloads
        fun createLexer(
            debug: Boolean = false,
            loopTolerance: Int = 1000,
        ): FlexLexer {
            val lexer = VentoLexer(null)
            lexer.setStrategy(LexerStrategyImpl(lexer, debug, loopTolerance))
            return lexer as FlexLexer
        }

        @JvmStatic
        @JvmOverloads
        fun createTestLexer(
            debug: Boolean = false,
            loopTolerance: Int = 1000,
        ): FlexLexer = createLexer(debug, loopTolerance)
    }
}
