/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.lexer

/**
 * Represents the state of the lexer during parsing.
 *
 * @property state The current state identifier
 * @property name The name of the current state
 * @property depth Triple containing nesting level count for braces `{}`, brackets `[]`, parentheses `()`
 */
class LexerState(val state: Int, val name: String, val depth: Triple<Int, Int, Int>)
