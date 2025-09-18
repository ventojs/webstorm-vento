/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.lexer

import com.intellij.psi.tree.IElementType
import org.js.vento.plugin.VentoLanguage

/**
 * Represents a token type for the Vento language.
 * Typically used for lexical tokens.
 */
class VentoLexerTokenType(debugName: String) : IElementType("${debugName}_TOKEN", VentoLanguage)
