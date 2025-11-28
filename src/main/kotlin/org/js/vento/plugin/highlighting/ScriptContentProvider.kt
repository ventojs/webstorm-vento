/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.highlighting

import com.intellij.lang.HtmlScriptContentProvider
import com.intellij.lexer.Lexer
import com.intellij.psi.tree.IElementType

class ScriptContentProvider : HtmlScriptContentProvider {
    override fun getScriptElementType(): IElementType? = null

    override fun getHighlightingLexer(): Lexer? = null
}
