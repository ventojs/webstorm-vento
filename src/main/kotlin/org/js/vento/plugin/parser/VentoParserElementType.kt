/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.psi.tree.IElementType
import org.js.vento.plugin.VentoLanguage

/**
 * Represents an element type for the Vento language.
 * Typically used for syntax/AST nodes in the PSI tree.
 */
class VentoParserElementType(debugName: String) : IElementType(debugName, VentoLanguage)
