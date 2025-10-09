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
class VentoLexerAdapter : FlexAdapter(createLexer()) {
    companion object {
        fun createLexer(): FlexLexer = VentoLexer(null) as FlexLexer

        fun createTestLexer(): FlexLexer = createLexer()
    }
}
