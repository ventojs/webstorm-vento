package org.js.vento.plugin.lexer

import com.intellij.psi.tree.IElementType
import org.js.vento.plugin.VentoLanguage

/**
 * Represents a token type for the Vento language.
 * Typically used for lexical tokens.
 */
class VentoTokenType(debugName: String) : IElementType(debugName, VentoLanguage)