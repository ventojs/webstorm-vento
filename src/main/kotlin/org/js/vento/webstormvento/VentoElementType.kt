/*
 * Copyright (c) 2023 Óscar Otero
 * All rights reserved.
 */

package org.js.vento.webstormvento

import com.intellij.psi.tree.IElementType

/**
 * Represents an element type for the Vento language.
 * Typically used for syntax/AST nodes in the PSI tree.
 */
class VentoElementType(debugName: String) : IElementType(debugName, VentoLanguage)