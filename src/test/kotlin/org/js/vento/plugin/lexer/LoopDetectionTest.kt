/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.lexer

import org.junit.Test
import kotlin.test.assertFailsWith

class LoopDetectionTest {
    @Test
    fun testInfiniteLoopDetection() {
        val lexer = VentoLexer(null)
        val strategy = LexerStrategyImpl(lexer)
        strategy.loopTolerance = 5

        // Simulate a loop: S0 -> enter(S1) -> leave() -> S0 -> enter(S1) ...
        for (i in 1..5) {
            strategy.enter(VentoLexer.BLOCK)
            strategy.leave()
        }

        assertFailsWith<IllegalStateException> {
            strategy.enter(VentoLexer.BLOCK)
        }
    }

    @Test
    fun testNoLoopWhenPositionChanges() {
        val lexer = object : VentoLexer(null) {
            private var pos = 0
            override fun getzzCurrentPos(): Int = pos
            fun incrementPos() { pos++ }
        }
        val strategy = LexerStrategyImpl(lexer)
        strategy.loopTolerance = 5

        for (i in 1..10) {
            strategy.enter(VentoLexer.BLOCK)
            lexer.incrementPos()
            strategy.leave()
        }
        // Should not throw exception because position changes
    }
}
