/*
 * Copyright (c) 2023 Óscar Otero
 * All rights reserved.
 */

package org.js.vento.webstormvento

import com.intellij.psi.tree.IElementType

/**
 * Represents a token type for the Vento language.
 * Typically used for lexical tokens.
 */
class VentoTokenType(debugName: String) : IElementType(debugName, VentoLanguage)