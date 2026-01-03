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
import org.js.vento.plugin.parser.ParserElements

/**
 * Custom HTML formatter aware of VENTO_ELEMENTS.
 * This formatter extends the existing default [HtmlPolicy].
 */
class VentoHtmlFormatter(settings: CodeStyleSettings, documentModel: FormattingDocumentModel) :
    HtmlPolicy(settings, documentModel) {
    companion object {
        fun getVentoType(block: Block): com.intellij.psi.tree.IElementType? {
            val node = (block as? ASTBlock)?.node ?: return null
            return getVentoType(node)
        }

        fun getVentoType(node: ASTNode?): com.intellij.psi.tree.IElementType? {
            if (node == null) return null
            val type = node.elementType
            if (ParserElements.VENTO_ELEMENTS.contains(type) ||
                ParserElements.VENTO_CLOSE_ELEMENTS.contains(type) ||
                type == ParserElements.ELSE_ELEMENT ||
                type == ParserElements.ELSEIF_ELEMENT
            ) {
                return type
            }

            var child = node.firstChildNode
            while (child != null) {
                val childType = child.elementType
                if (ParserElements.VENTO_ELEMENTS.contains(childType) ||
                    ParserElements.VENTO_CLOSE_ELEMENTS.contains(childType) ||
                    childType == ParserElements.ELSE_ELEMENT ||
                    childType == ParserElements.ELSEIF_ELEMENT
                ) {
                    return childType
                }
                child = child.treeNext
            }
            return null
        }

        fun isOpening(type: com.intellij.psi.tree.IElementType?): Boolean = type != null && ParserElements.VENTO_ELEMENTS.contains(type)

        fun isClosing(type: com.intellij.psi.tree.IElementType?): Boolean =
            type != null &&
                ParserElements.VENTO_CLOSE_ELEMENTS.contains(
                    type,
                )

        fun isIntermediate(type: com.intellij.psi.tree.IElementType?): Boolean =
            type == ParserElements.ELSE_ELEMENT || type == ParserElements.ELSEIF_ELEMENT

        fun applyVentoIndent(blocks: List<Block>): List<Block> {
            val result = mutableListOf<Block>()
            var i = 0
            while (i < blocks.size) {
                val block = blocks[i]
                val type = getVentoType(block)
                if (isOpening(type) || isIntermediate(type)) {
                    result.add(VentoSubBlockWrapper(block))
                    i++
                    val group = mutableListOf<Block>()
                    var balance = 1
                    while (i < blocks.size) {
                        val subBlock = blocks[i]
                        val subType = getVentoType(subBlock)
                        if (isOpening(subType)) balance++
                        if (isClosing(subType)) balance--
                        if (isIntermediate(subType) && balance == 1) break
                        if (balance == 0) break
                        group.add(subBlock)
                        i++
                    }
                    if (group.isNotEmpty()) {
                        result.add(VentoGroupBlock(group))
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
