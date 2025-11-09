/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.lexer

import com.intellij.psi.tree.IElementType

/**
 * Defines the strategy interface for lexical analysis operations in the Vento template engine.
 * Provides methods for state management, token handling, and nested structure tracking
 * during the lexical analysis of Vento templates.
 *
 * This interface handles:
 * - State management for the lexical analyzer
 * - Token type determination and manipulation
 * - Depth tracking for objects, arrays, and parentheses
 * - Debug functionality for lexer operations
 */
interface LexerStrategy {
    fun stName(s: Int): String

    fun enter(nextState: Int)

    fun leave()

    fun remaining(): String

    fun resetAt(s: Int)

    fun debug()

    fun debug(rule: String?)

    fun pushbackall()

    fun dumpDiag(reason: String?)

    fun getTokenType(): IElementType

    fun t(tt: IElementType): IElementType

    fun incObjDepth(): Int

    fun incArrDepth(): Int

    fun incParDepth(): Int

    fun decObjDepth(): Int

    fun decArrDepth(): Int

    fun decParDepth(): Int

    fun currentDepth(): Triple<Int, Int, Int>

    fun resetObjDepth()

    fun resetArrDepth()

    fun resetParDepth()

    fun debugModeOff()

    fun debugModeRestore()

    fun debugMsg(msg: String)

    fun parentStateIs(state: Int): Boolean
}
