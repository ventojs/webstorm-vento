// Copyright 2000-2025 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package org.js.vento.plugin.formatting

import com.intellij.formatting.Alignment
import com.intellij.formatting.Block
import com.intellij.formatting.ChildAttributes
import com.intellij.formatting.FormattingContext
import com.intellij.formatting.FormattingModel
import com.intellij.formatting.Indent
import com.intellij.formatting.Wrap
import com.intellij.formatting.templateLanguages.BlockWithParent
import com.intellij.formatting.templateLanguages.DataLanguageBlockWrapper
import com.intellij.formatting.templateLanguages.TemplateLanguageBlock
import com.intellij.formatting.templateLanguages.TemplateLanguageBlockFactory
import com.intellij.formatting.templateLanguages.TemplateLanguageFormattingModelBuilder
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.formatter.DocumentBasedFormattingModel
import com.intellij.psi.formatter.FormattingDocumentModelImpl
import com.intellij.psi.formatter.common.AbstractBlock
import com.intellij.psi.formatter.xml.SyntheticBlock
import com.intellij.psi.templateLanguages.SimpleTemplateLanguageFormattingModelBuilder
import com.intellij.psi.tree.IElementType
import com.intellij.psi.xml.XmlTag
import org.js.vento.plugin.lexer.LexerTokens
import org.js.vento.plugin.parser.ParserElements

/**
 * Shamelessly copied from https://github.com/JetBrains/intellij-plugins/blob/master/handlebars/src/com/dmarcotte/handlebars/format/HbFormattingModelBuilder.java and adapted.
 */
class FormattingModelBuilder : TemplateLanguageFormattingModelBuilder() {
    override fun createTemplateLanguageBlock(
        node: ASTNode,
        wrap: Wrap?,
        alignment: Alignment?,
        foreignChildren: MutableList<DataLanguageBlockWrapper?>?,
        codeStyleSettings: CodeStyleSettings,
    ): TemplateLanguageBlock {
        val documentModel: FormattingDocumentModelImpl =
            FormattingDocumentModelImpl.createOn(node.psi.containingFile)
        val policy = VentoHtmlFormatter(codeStyleSettings, documentModel)
        return if (LexerTokens.TAGS.contains(node.elementType)) {
            VentoTagBlock(
                node,
                wrap,
                alignment,
                this,
                codeStyleSettings,
                foreignChildren,
                policy,
            )
        } else {
            VentoBlock(node, wrap, alignment, this, codeStyleSettings, foreignChildren, policy)
        }
    }

    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val file: PsiFile = formattingContext.containingFile
        val rootBlock: Block?

        val node: ASTNode = formattingContext.node

        if (node.elementType === LexerTokens.VENTO_OUTER) {
            return SimpleTemplateLanguageFormattingModelBuilder().createModel(formattingContext)
        } else {
            rootBlock = getRootBlock(file, file.viewProvider, formattingContext.codeStyleSettings)
        }
        return DocumentBasedFormattingModel(
            VentoSubBlockWrapper(rootBlock),
            formattingContext.project,
            formattingContext.codeStyleSettings,
            file.fileType,
            file,
        )
    }

    override fun dontFormatMyModel(): Boolean = false

    private class VentoTagBlock(
        node: ASTNode,
        wrap: Wrap?,
        alignment: Alignment?,
        blockFactory: TemplateLanguageBlockFactory,
        settings: CodeStyleSettings,
        foreignChildren: MutableList<DataLanguageBlockWrapper?>?,
        htmlPolicy: VentoHtmlFormatter,
    ) : VentoBlock(node, wrap, alignment, blockFactory, settings, foreignChildren, htmlPolicy) {
        override fun getChildAttributes(newChildIndex: Int): ChildAttributes {
            if (newChildIndex > 0) {
                val blocks = getSubBlocks()
                if (blocks.size > newChildIndex - 1) {
                    val prevBlock = blocks[newChildIndex - 1]
                    if (prevBlock is AbstractBlock) {
                        return ChildAttributes(null, prevBlock.alignment)
                    }
                }
            }

            return super.getChildAttributes(newChildIndex)
        }

        override fun createChildAlignment(child: ASTNode): Alignment? = super.createChildAlignment(child)

        override fun createChildWrap(child: ASTNode): Wrap? = null
    }

    private open class VentoBlock(
        node: ASTNode,
        wrap: Wrap?,
        alignment: Alignment?,
        blockFactory: TemplateLanguageBlockFactory,
        settings: CodeStyleSettings,
        foreignChildren: MutableList<DataLanguageBlockWrapper?>?,
        htmlPolicy: VentoHtmlFormatter,
    ) : TemplateLanguageBlock(node, wrap, alignment, blockFactory, settings, foreignChildren) {
        protected val myHtmlPolicy: VentoHtmlFormatter = htmlPolicy

        override fun getIndent(): Indent? {
            // ignore whitespace
            if (myNode.text.trim { it <= ' ' }.isEmpty()) {
                return Indent.getNoneIndent()
            }

            val foreignParent: DataLanguageBlockWrapper? = getForeignBlockParent(true)
            if (foreignParent != null) {
                if (foreignParent.node is XmlTag &&
                    !myHtmlPolicy.indentChildrenOf(foreignParent.node as XmlTag?)
                ) {
                    return Indent.getNoneIndent()
                }
                return Indent.getNormalIndent()
            }

            return Indent.getNoneIndent()
        }

        override fun getSubBlocks(): List<Block> = VentoHtmlFormatter.applyVentoIndent(super.getSubBlocks())

        override fun getTemplateTextElementType(): IElementType = ParserElements.HTML_CONTENT

        override fun isRequiredRange(range: TextRange?): Boolean = false

        override fun getChildAttributes(newChildIndex: Int): ChildAttributes =
//            VentoHtmlFormatter.getChildAttributes(this, newChildIndex)
            super.getChildAttributes(newChildIndex)

        fun getForeignBlockParent(immediate: Boolean): DataLanguageBlockWrapper? {
            var foreignBlockParent: DataLanguageBlockWrapper? = null
            var parent: BlockWithParent? = getParent()

            while (parent != null) {
                if (parent is DataLanguageBlockWrapper && parent.original !is SyntheticBlock) {
                    foreignBlockParent = parent
                    break
                } else if (immediate && parent is VentoBlock) {
                    break
                }
                parent = parent.parent
            }

            return foreignBlockParent
        }
    }
}
