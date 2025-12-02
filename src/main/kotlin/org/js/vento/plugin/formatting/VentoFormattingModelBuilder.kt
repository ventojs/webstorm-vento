/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.formatting

import com.intellij.formatting.Alignment
import com.intellij.formatting.Block
import com.intellij.formatting.FormattingContext
import com.intellij.formatting.FormattingModel
import com.intellij.formatting.FormattingModelBuilder
import com.intellij.formatting.FormattingModelProvider
import com.intellij.formatting.Indent
import com.intellij.formatting.Spacing
import com.intellij.formatting.Wrap
import com.intellij.lang.ASTNode
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.formatter.FormatterUtil
import com.intellij.psi.formatter.common.AbstractBlock

class VentoFormattingModelBuilder : FormattingModelBuilder {
    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val element = formattingContext.psiElement
        val settings = formattingContext.codeStyleSettings

        val rootBlock =
            VentoBlock(
                element.node,
                null,
                Indent.getNoneIndent(),
                null,
                settings,
            )

        return FormattingModelProvider.createFormattingModelForPsiFile(
            element.containingFile,
            rootBlock,
            settings,
        )
    }
}

class VentoBlock(
    node: ASTNode,
    wrap: Wrap?,
    indent: Indent?,
    alignment: Alignment?,
    private val settings: CodeStyleSettings,
) : AbstractBlock(node, wrap, alignment) {
    override fun buildChildren(): List<Block> {
        val blocks = mutableListOf<Block>()
        var child = myNode.firstChildNode

        while (child != null) {
            if (!FormatterUtil.containsWhiteSpacesOnly(child) && child.textLength > 0) {
                blocks.add(
                    VentoBlock(
                        child,
                        null,
                        Indent.getNoneIndent(),
                        null,
                        settings,
                    ),
                )
            }
            child = child.treeNext
        }

        return blocks
    }

    override fun getIndent(): Indent? = Indent.getNoneIndent()

    override fun getSpacing(child1: Block?, child2: Block): Spacing? = null

    override fun isLeaf(): Boolean = myNode.firstChildNode == null
}
