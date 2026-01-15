/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.lexer

import com.intellij.lexer.Lexer
import com.intellij.lexer.MergeFunction
import com.intellij.lexer.MergingLexerAdapterBase
import com.intellij.psi.tree.IElementType

class VentoMergingLexer : MergingLexerAdapterBase(LexerAdapter()) {
    override fun getMergeFunction(): MergeFunction = MERGE_FUNCTION

    companion object {
        private val MERGE_FUNCTION: MergeFunction =
            object : MergeFunction {
                override fun merge(type: IElementType?, originalLexer: Lexer): IElementType? {
//                    if (type === LexerTokens.UNKNOWN) {
//                        return LexerTokens.UNKNOWN
//                    }

//                    if (originalLexer.getTokenType() === LexerTokens.UNKNOWN) {
//                        originalLexer.advance()
//                        return LexerTokens.UNKNOWN
//                    }

                    return type
                }
            }
    }
}
