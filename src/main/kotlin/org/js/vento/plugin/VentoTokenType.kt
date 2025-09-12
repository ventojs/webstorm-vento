package org.js.vento.plugin

import com.intellij.psi.tree.IElementType

/**
 * Represents a token type for the Vento language.
 * Typically used for lexical tokens.
 */
class VentoTokenType(debugName: String) : IElementType(debugName, VentoLanguage)