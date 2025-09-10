/*
 * Copyright (c) 2023 Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin

import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.psi.tree.IElementType


/**
 * A basic parser for Vento files. This is a placeholder/skeleton parser
 * that you can later expand to properly handle your Vento grammar.
 */
class VentoParser : PsiParser {
    override fun parse(root: IElementType, builder: PsiBuilder) =
        builder.mark().apply { while (!builder.eof()) builder.advanceLexer() }.done(root).run { builder.treeBuilt }
}