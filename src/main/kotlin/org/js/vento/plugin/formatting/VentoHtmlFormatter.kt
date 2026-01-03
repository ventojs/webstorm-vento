/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.formatting

import com.intellij.formatting.ASTBlock
import com.intellij.formatting.Alignment
import com.intellij.formatting.Block
import com.intellij.formatting.ChildAttributes
import com.intellij.formatting.FormattingDocumentModel
import com.intellij.formatting.Indent
import com.intellij.formatting.Spacing
import com.intellij.formatting.Wrap
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.NlsSafe
import com.intellij.openapi.util.TextRange
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.formatter.xml.HtmlPolicy
import com.intellij.psi.tree.IElementType
import org.js.vento.plugin.lexer.LexerTokens
import org.js.vento.plugin.parser.ParserElements

/**
 * Custom HTML formatter aware of VENTO_ELEMENTS.
 * This formatter extends the existing default [HtmlPolicy].
 */
class VentoHtmlFormatter(settings: CodeStyleSettings, documentModel: FormattingDocumentModel) :
    HtmlPolicy(settings, documentModel) {
    companion object {
        fun getVentoType(block: Block): IElementType? {
            val node = (block as? ASTBlock)?.node ?: return null
            return getVentoType(node)
        }

        fun getVentoType(node: ASTNode?): IElementType? {
            if (node == null) return null
            val type = node.elementType
            if (ParserElements.VENTO_ELEMENTS.contains(type) ||
                ParserElements.VENTO_CLOSE_ELEMENTS.contains(type) ||
                type == ParserElements.ELSE_ELEMENT ||
                type == ParserElements.ELSEIF_ELEMENT ||
                type == LexerTokens.SET_CLOSE_KEY ||
                type == LexerTokens.IF_CLOSE_KEY ||
                type == LexerTokens.FOR_CLOSE_KEY ||
                type == LexerTokens.EXPORT_CLOSE_KEY ||
                type == LexerTokens.LAYOUT_CLOSE_KEY ||
                type == LexerTokens.FRAGMENT_CLOSE_KEY ||
                type == LexerTokens.LAYOUT_SLOT_CLOSE_KEY ||
                type == LexerTokens.ECHO_CLOSE_KEY ||
                type == LexerTokens.FUNCTION_CLOSE_KEY
            ) {
                return type
            }

            // Fallback for malformed blocks
            if (type == ParserElements.VENTO_BLOCK) {
                val text = node.text
                if (text.contains("/set")) return ParserElements.SET_CLOSE_ELEMENT
                if (text.contains("/if")) return ParserElements.IF_CLOSE_ELEMENT
                if (text.contains("/for")) return ParserElements.FOR_CLOSE_ELEMENT
                if (text.contains("/export")) return ParserElements.EXPORT_CLOSE_ELEMENT
                if (text.contains("/layout")) return ParserElements.LAYOUT_CLOSE_ELEMENT
                if (text.contains("/fragment")) return ParserElements.FRAGMENT_CLOSE_ELEMENT
                if (text.contains("/slot")) return ParserElements.LAYOUT_SLOT_CLOSE_ELEMENT
                if (text.contains("/echo")) return ParserElements.ECHO_CLOSE_ELEMENT
                if (text.contains("/function")) return ParserElements.FUNCTION_CLOSE_ELEMENT

                if (text.contains("set ")) return ParserElements.SET_ELEMENT
                if (text.contains("if ")) return ParserElements.IF_ELEMENT
                if (text.contains("for ")) return ParserElements.FOR_ELEMENT
            }

            var child = node.firstChildNode
            while (child != null) {
                val found = getVentoType(child)
                if (found != null) return found
                child = child.treeNext
            }
            return null
        }

        fun isOpening(type: IElementType?, node: ASTNode?): Boolean {
            if (type == null || !ParserElements.VENTO_ELEMENTS.contains(type)) return false

            // SET_ELEMENT is only considered opening if it doesn't have an EQUAL child
            if (type == ParserElements.SET_ELEMENT) {
                val targetNode =
                    if (node?.elementType == ParserElements.SET_ELEMENT) {
                        node
                    } else {
                        node?.findChildByType(ParserElements.SET_ELEMENT)
                    }
                return targetNode?.findChildByType(LexerTokens.EQUAL) == null
            }

            return true
        }

        fun isClosing(type: IElementType?): Boolean =
            type != null &&
                (
                    ParserElements.VENTO_CLOSE_ELEMENTS.contains(type) ||
                        type == LexerTokens.SET_CLOSE_KEY ||
                        type == LexerTokens.IF_CLOSE_KEY ||
                        type == LexerTokens.FOR_CLOSE_KEY ||
                        type == LexerTokens.EXPORT_CLOSE_KEY ||
                        type == LexerTokens.LAYOUT_CLOSE_KEY ||
                        type == LexerTokens.FRAGMENT_CLOSE_KEY ||
                        type == LexerTokens.LAYOUT_SLOT_CLOSE_KEY ||
                        type == LexerTokens.ECHO_CLOSE_KEY ||
                        type == LexerTokens.FUNCTION_CLOSE_KEY
                )

        fun isIntermediate(type: IElementType?): Boolean =
            type == ParserElements.ELSE_ELEMENT || type == ParserElements.ELSEIF_ELEMENT

        fun applyVentoIndent(blocks: List<Block>): List<Block> {
            val result = mutableListOf<Block>()
            var i = 0
            while (i < blocks.size) {
                val block = blocks[i]
                val type = getVentoType(block)
                if (isOpening(type, (block as? ASTBlock)?.node) || isIntermediate(type)) {
                    result.add(VentoSubBlockWrapper(block))
                    i++
                    val group = mutableListOf<Block>()
                    var balance = 1
                    while (i < blocks.size) {
                        val subBlock = blocks[i]
                        val subType = getVentoType(subBlock)
                        if (isOpening(subType, (subBlock as? ASTBlock)?.node)) balance++
                        if (isClosing(subType)) balance--
                        if (isIntermediate(subType) && balance == 1) break
                        if (balance == 0) break
                        group.add(subBlock)
                        i++
                    }
                    if (group.isNotEmpty()) {
                        if (type == ParserElements.SET_ELEMENT && balance > 0) {
                            result.addAll(group)
                        } else {
                            result.add(VentoGroupBlock(group))
                        }
                    }
                } else {
                    result.add(VentoSubBlockWrapper(block))
                    i++
                }
            }
            return result
        }
    }
}

interface VentoWrapper : Block {
    val delegate: Block
}

class VentoGroupBlock(private val blocks: List<Block>) : Block {
    override fun getTextRange(): TextRange {
        val start = blocks.first().textRange.startOffset
        val end = blocks.last().textRange.endOffset
        return TextRange(start, end)
    }

    override fun getSubBlocks(): List<Block> = VentoHtmlFormatter.applyVentoIndent(blocks)

    override fun getWrap(): Wrap? = null

    override fun getIndent(): Indent? = Indent.getNormalIndent()

    override fun getAlignment(): Alignment? = null

    override fun getSpacing(child1: Block?, child2: Block): Spacing? = null

    override fun getChildAttributes(newChildIndex: Int): ChildAttributes =
        ChildAttributes(Indent.getNormalIndent(), null)

    override fun isIncomplete(): Boolean = false

    override fun isLeaf(): Boolean = false
}

class VentoSubBlockWrapper(override val delegate: Block) : VentoWrapper, Block by delegate {
    override fun getSubBlocks(): List<Block> = VentoHtmlFormatter.applyVentoIndent(delegate.subBlocks)

    override fun getDebugName(): @NlsSafe String? = delegate.debugName
}
