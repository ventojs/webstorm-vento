/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.editor.completions

import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.InsertionContext
import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.template.Template
import com.intellij.codeInsight.template.TemplateManager
import com.intellij.codeInsight.template.impl.ConstantNode
import com.intellij.injected.editor.EditorWindow
import com.intellij.lang.ASTNode
import com.intellij.lang.injection.InjectedLanguageManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.progress.ProcessCanceledException
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import org.js.vento.plugin.Vento
import org.js.vento.plugin.lexer.LexerTokens
import org.js.vento.plugin.parser.ParserElements

fun openingKeywords(result: CompletionResultSet) {
    val priority = 75.0

    result.addElement(
        PrioritizedLookupElement.withPriority(
            LookupElementBuilder
                .create("else")
                .withIcon(Vento.ICON)
                .withTypeText("Vento")
                .withInsertHandler { context, _ ->
                    val templateManager = TemplateManager.getInstance(context.project)
                    val template = templateManager.createTemplate("", "")
                    template.addTextSegment(" ")
                    template.addClosingBraceIfMissing(context)
                    template.addTextSegment("\n")
                    template.addEndVariable()
                    templateManager.startTemplate(hostEditorOf(context), template)
                }.bold(),
            priority,
        ),
    )

    result.addElement(
        PrioritizedLookupElement.withPriority(
            LookupElementBuilder
                .create("else if")
                .withTailText(" condition }}")
                .withTypeText("Vento", true)
                .withIcon(Vento.ICON)
                .withInsertHandler { context, _ ->
                    val templateManager = TemplateManager.getInstance(context.project)
                    val template = templateManager.createTemplate("", "")
                    template.addTextSegment(" ")
                    template.addVariable("condition", ConstantNode("condition"), true)
                    template.addTextSegment(" ")
                    template.addClosingBraceIfMissing(context)
                    template.addTextSegment("\n")
                    template.addEndVariable()
                    templateManager.startTemplate(hostEditorOf(context), template)
                }.bold(),
            priority,
        ),
    )

    result.addElement(
        PrioritizedLookupElement.withPriority(
            LookupElementBuilder
                .create("break")
                .withIcon(Vento.ICON)
                .withTypeText("Vento")
                .withInsertHandler { context, _ ->
                    val templateManager = TemplateManager.getInstance(context.project)
                    val template = templateManager.createTemplate("", "")
                    template.addTextSegment(" ")
                    template.addClosingBraceIfMissing(context)
                    template.addEndVariable()
                    templateManager.startTemplate(hostEditorOf(context), template)
                }.bold(),
            priority,
        ),
    )

    result.addElement(
        PrioritizedLookupElement.withPriority(
            LookupElementBuilder
                .create("continue")
                .withIcon(Vento.ICON)
                .withTypeText("Vento")
                .withInsertHandler { context, _ ->
                    val templateManager = TemplateManager.getInstance(context.project)
                    val template = templateManager.createTemplate("", "")
                    template.addTextSegment(" ")
                    template.addClosingBraceIfMissing(context)
                    template.addEndVariable()
                    templateManager.startTemplate(hostEditorOf(context), template)
                }.bold(),
            priority,
        ),
    )

    result.addElement(
        PrioritizedLookupElement.withPriority(
            LookupElementBuilder
                .create("echo")
                .withIcon(Vento.ICON)
                .withTailText(" \"text\" }}")
                .withTypeText("Vento")
                .withInsertHandler { context, _ ->
                    val templateManager = TemplateManager.getInstance(context.project)
                    val template = templateManager.createTemplate("", "")
                    template.addTextSegment(" \"")
                    template.addVariable("text", ConstantNode("text"), true)
                    template.addTextSegment("\"")
                    template.addClosingBraceIfMissing(context)
                    template.addEndVariable()
                    templateManager.startTemplate(hostEditorOf(context), template)
                }.bold(),
            priority,
        ),
    )

    result.addElement(
        PrioritizedLookupElement.withPriority(
            LookupElementBuilder
                .create("echo")
                .withIcon(Vento.ICON)
                .withTailText(" }} content {{ /echo }}")
                .withTypeText("Vento")
                .withInsertHandler { context, _ ->
                    val templateManager = TemplateManager.getInstance(context.project)
                    val template = templateManager.createTemplate("", "")
                    template.addClosingBraceIfMissing(context)
                    template.addTextSegment("\n")
                    template.addEndVariable()
                    template.addBlockCloserIfMissing(
                        context,
                        ParserElements.ECHO_ELEMENT,
                        ParserElements.ECHO_CLOSE_ELEMENT,
                        "{{ /echo }}",
                        ::isBlockEcho,
                    )
                    templateManager.startTemplate(hostEditorOf(context), template)
                }.bold(),
            priority,
        ),
    )

    result.addElement(
        PrioritizedLookupElement.withPriority(
            LookupElementBuilder
                .create("export")
                .withIcon(Vento.ICON)
                .withTailText(" name = value }}")
                .withTypeText("Vento")
                .withInsertHandler { context, _ ->
                    val templateManager = TemplateManager.getInstance(context.project)
                    val template = templateManager.createTemplate("", "")
                    template.addTextSegment(" ")
                    template.addVariable("name", ConstantNode("name"), true)
                    template.addTextSegment(" = ")
                    template.addVariable("value", ConstantNode("value"), true)
                    template.addClosingBraceIfMissing(context)
                    template.addEndVariable()
                    templateManager.startTemplate(hostEditorOf(context), template)
                }.bold(),
            priority,
        ),
    )

    result.addElement(
        PrioritizedLookupElement.withPriority(
            LookupElementBuilder
                .create("export")
                .withIcon(Vento.ICON)
                .withTailText(" name }} content {{ /export }}")
                .withTypeText("Vento")
                .withInsertHandler { context, _ ->
                    val templateManager = TemplateManager.getInstance(context.project)
                    val template = templateManager.createTemplate("", "")
                    template.addTextSegment(" ")
                    template.addVariable("name", ConstantNode("name"), true)
                    template.addClosingBraceIfMissing(context)
                    template.addTextSegment("\n")
                    template.addEndVariable()
                    template.addBlockCloserIfMissing(
                        context,
                        ParserElements.EXPORT_OPEN_ELEMENT,
                        ParserElements.EXPORT_CLOSE_ELEMENT,
                        "{{ /export }}",
                    )
                    templateManager.startTemplate(hostEditorOf(context), template)
                }.bold(),
            priority,
        ),
    )

    result.addElement(
        PrioritizedLookupElement.withPriority(
            LookupElementBuilder
                .create("export function")
                .withIcon(Vento.ICON)
                .withTailText(" name(arg) }}")
                .withTypeText("Vento")
                .withInsertHandler { context, _ ->
                    val templateManager = TemplateManager.getInstance(context.project)
                    val template = templateManager.createTemplate("", "")
                    template.addTextSegment(" ")
                    template.addVariable("name", ConstantNode("name"), true)
                    template.addTextSegment("(")
                    template.addVariable("arg", ConstantNode("arg"), true)
                    template.addTextSegment(")")
                    template.addClosingBraceIfMissing(context)
                    template.addTextSegment("\n")
                    template.addEndVariable()
                    template.addBlockCloserIfMissing(
                        context,
                        ParserElements.EXPORT_OPEN_ELEMENT,
                        ParserElements.EXPORT_CLOSE_ELEMENT,
                        "{{ /export }}",
                    )
                    templateManager.startTemplate(hostEditorOf(context), template)
                }.bold(),
            priority,
        ),
    )
    result.addElement(
        PrioritizedLookupElement.withPriority(
            LookupElementBuilder
                .create("for")
                .withTailText(" value of collection }}", true)
                .withTypeText("Vento")
                .withIcon(Vento.ICON)
                .withInsertHandler { context, _ ->
                    val templateManager = TemplateManager.getInstance(context.project)
                    val template = templateManager.createTemplate("", "")
                    template.addTextSegment(" ")
                    template.addVariable("value", ConstantNode("value"), true)
                    template.addTextSegment(" of ")
                    template.addVariable("collection", ConstantNode("collection"), true)
                    template.addClosingBraceIfMissing(context)
                    template.addTextSegment("\n")
                    template.addEndVariable()
                    template.addBlockCloserIfMissing(
                        context,
                        ParserElements.FOR_ELEMENT,
                        ParserElements.FOR_CLOSE_ELEMENT,
                        "{{ /for }}",
                    )
                    templateManager.startTemplate(hostEditorOf(context), template)
                }.bold(),
            priority,
        ),
    )

    result.addElement(
        PrioritizedLookupElement.withPriority(
            LookupElementBuilder
                .create("for")
                .withTailText(" key, value of collection }}", true)
                .withTypeText("Vento")
                .withIcon(Vento.ICON)
                .withInsertHandler { context, _ ->
                    val templateManager = TemplateManager.getInstance(context.project)
                    val template = templateManager.createTemplate("", "")
                    template.addTextSegment(" ")
                    template.addVariable("key", ConstantNode("key"), true)
                    template.addTextSegment(", ")
                    template.addVariable("value", ConstantNode("value"), true)
                    template.addTextSegment(" of ")
                    template.addVariable("collection", ConstantNode("collection"), true)
                    template.addClosingBraceIfMissing(context)
                    template.addTextSegment("\n")
                    template.addEndVariable()
                    template.addBlockCloserIfMissing(
                        context,
                        ParserElements.FOR_ELEMENT,
                        ParserElements.FOR_CLOSE_ELEMENT,
                        "{{ /for }}",
                    )
                    templateManager.startTemplate(hostEditorOf(context), template)
                }.bold(),
            priority,
        ),
    )

    result.addElement(
        PrioritizedLookupElement.withPriority(
            LookupElementBuilder
                .create("function")
                .withIcon(Vento.ICON)
                .withTailText(" name(arg) }}")
                .withTypeText("Vento")
                .withInsertHandler { context, _ ->
                    val templateManager = TemplateManager.getInstance(context.project)
                    val template = templateManager.createTemplate("", "")
                    template.addTextSegment(" ")
                    template.addVariable("name", ConstantNode("name"), true)
                    template.addTextSegment("(")
                    template.addVariable("arg", ConstantNode("arg"), true)
                    template.addTextSegment(")")
                    template.addClosingBraceIfMissing(context)
                    template.addTextSegment("\n")
                    template.addEndVariable()
                    template.addBlockCloserIfMissing(
                        context,
                        ParserElements.FUNCTION_SIGNATURE_ELEMENT,
                        ParserElements.FUNCTION_CLOSE_ELEMENT,
                        "{{ /function }}",
                    )
                    templateManager.startTemplate(hostEditorOf(context), template)
                }.bold(),
            priority,
        ),
    )
    result.addElement(
        PrioritizedLookupElement.withPriority(
            LookupElementBuilder
                .create("if")
                .withTailText(" condition }}")
                .withTypeText("Vento", true)
                .withIcon(Vento.ICON)
                .withInsertHandler { context, _ ->
                    val templateManager = TemplateManager.getInstance(context.project)
                    val template = templateManager.createTemplate("", "")
                    template.addTextSegment(" ")
                    template.addVariable("condition", ConstantNode("condition"), true)
                    template.addClosingBraceIfMissing(context)
                    template.addTextSegment("\n")
                    template.addEndVariable()
                    template.addBlockCloserIfMissing(
                        context,
                        ParserElements.IF_ELEMENT,
                        ParserElements.IF_CLOSE_ELEMENT,
                        "{{ /if }}",
                    )
                    templateManager.startTemplate(hostEditorOf(context), template)
                }.bold(),
            priority,
        ),
    )

    result.addElement(
        PrioritizedLookupElement.withPriority(
            LookupElementBuilder
                .create("import")
                .withIcon(Vento.ICON)
                .withTailText(" { symbol} from \"file\" }}")
                .withTypeText("Vento")
                .withInsertHandler { context, _ ->
                    val templateManager = TemplateManager.getInstance(context.project)
                    val template = templateManager.createTemplate("", "")
                    template.addTextSegment(" { ")
                    template.addVariable("symbol", ConstantNode("symbol"), true)
                    template.addTextSegment(" } from \"")
                    template.addVariable("file", ConstantNode("file"), true)
                    template.addTextSegment("\"")
                    template.addClosingBraceIfMissing(context)
                    template.addEndVariable()
                    templateManager.startTemplate(hostEditorOf(context), template)
                }.bold(),
            priority,
        ),
    )

    result.addElement(
        PrioritizedLookupElement.withPriority(
            LookupElementBuilder
                .create("include")
                .withIcon(Vento.ICON)
                .withTailText(" \"file\" }}")
                .withTypeText("Vento")
                .withInsertHandler { context, _ ->
                    val templateManager = TemplateManager.getInstance(context.project)
                    val template = templateManager.createTemplate("", "")
                    template.addTextSegment(" \"")
                    template.addVariable("file", ConstantNode("file"), true)
                    template.addTextSegment("\"")
                    template.addClosingBraceIfMissing(context)
                    template.addEndVariable()
                    templateManager.startTemplate(hostEditorOf(context), template)
                }.bold(),
            priority,
        ),
    )

    result.addElement(
        PrioritizedLookupElement.withPriority(
            LookupElementBuilder
                .create("layout")
                .withIcon(Vento.ICON)
                .withTailText(" \"file\" }} content {{ /layout }}")
                .withTypeText("Vento")
                .withInsertHandler { context, _ ->
                    val templateManager = TemplateManager.getInstance(context.project)
                    val template = templateManager.createTemplate("", "")
                    template.addTextSegment(" \"")
                    template.addVariable("file", ConstantNode("file"), true)
                    template.addTextSegment("\"")
                    template.addClosingBraceIfMissing(context)
                    template.addTextSegment("\n")
                    template.addEndVariable()
                    template.addBlockCloserIfMissing(
                        context,
                        ParserElements.LAYOUT_ELEMENT,
                        ParserElements.LAYOUT_CLOSE_ELEMENT,
                        "{{ /layout }}",
                    )
                    templateManager.startTemplate(hostEditorOf(context), template)
                }.bold(),
            priority,
        ),
    )

    result.addElement(
        PrioritizedLookupElement.withPriority(
            LookupElementBuilder
                .create("slot")
                .withIcon(Vento.ICON)
                .withTailText(" name }} content {{ /slot }}")
                .withTypeText("Vento")
                .withInsertHandler { context, _ ->
                    val templateManager = TemplateManager.getInstance(context.project)
                    val template = templateManager.createTemplate("", "")
                    template.addTextSegment(" ")
                    template.addVariable("name", ConstantNode("name"), true)
                    template.addClosingBraceIfMissing(context)
                    template.addTextSegment("\n")
                    template.addEndVariable()
                    template.addBlockCloserIfMissing(
                        context,
                        ParserElements.LAYOUT_SLOT_ELEMENT,
                        ParserElements.LAYOUT_SLOT_CLOSE_ELEMENT,
                        "{{ /slot }}",
                    )
                    templateManager.startTemplate(hostEditorOf(context), template)
                }.bold(),
            priority,
        ),
    )

    result.addElement(
        PrioritizedLookupElement.withPriority(
            LookupElementBuilder
                .create("set")
                .withIcon(Vento.ICON)
                .withTailText(" name }} content {{ /set }}")
                .withTypeText("Vento")
                .withInsertHandler { context, _ ->
                    val templateManager = TemplateManager.getInstance(context.project)
                    val template = templateManager.createTemplate("", "")
                    template.addTextSegment(" ")
                    template.addVariable("name", ConstantNode("name"), true)
                    template.addClosingBraceIfMissing(context)
                    template.addTextSegment("\n")
                    template.addEndVariable()
                    template.addBlockCloserIfMissing(
                        context,
                        ParserElements.SET_ELEMENT,
                        ParserElements.SET_CLOSE_ELEMENT,
                        "{{ /set }}",
                        ::isBlockForm,
                    )
                    templateManager.startTemplate(hostEditorOf(context), template)
                }.bold(),
            priority,
        ),
    )

    result.addElement(
        PrioritizedLookupElement.withPriority(
            LookupElementBuilder
                .create("set")
                .withIcon(Vento.ICON)
                .withTailText(" name = value }}", true)
                .withTypeText("Vento")
                .withInsertHandler { context, _ ->
                    val templateManager = TemplateManager.getInstance(context.project)
                    val template = templateManager.createTemplate("", "")
                    template.addTextSegment(" ")
                    template.addVariable("name", ConstantNode("name"), true)
                    template.addTextSegment(" = ")
                    template.addVariable("value", ConstantNode("value"), true)
                    template.addTextSegment(" ")
                    template.addClosingBraceIfMissing(context)
                    templateManager.startTemplate(hostEditorOf(context), template)
                }.bold(),
            priority,
        ),
    )

    result.addElement(
        PrioritizedLookupElement.withPriority(
            LookupElementBuilder
                .create("default")
                .withIcon(Vento.ICON)
                .withTailText(" name }} content {{ /default }}")
                .withTypeText("Vento")
                .withInsertHandler { context, _ ->
                    val templateManager = TemplateManager.getInstance(context.project)
                    val template = templateManager.createTemplate("", "")
                    template.addTextSegment(" ")
                    template.addVariable("name", ConstantNode("name"), true)
                    template.addClosingBraceIfMissing(context)
                    template.addTextSegment("\n")
                    template.addEndVariable()
                    template.addBlockCloserIfMissing(
                        context,
                        ParserElements.DEFAULT_ELEMENT,
                        ParserElements.DEFAULT_CLOSE_ELEMENT,
                        "{{ /default }}",
                        ::isBlockForm,
                    )
                    templateManager.startTemplate(hostEditorOf(context), template)
                }.bold(),
            priority,
        ),
    )

    result.addElement(
        PrioritizedLookupElement.withPriority(
            LookupElementBuilder
                .create("default")
                .withIcon(Vento.ICON)
                .withTailText(" name = value }}", true)
                .withTypeText("Vento")
                .withInsertHandler { context, _ ->
                    val templateManager = TemplateManager.getInstance(context.project)
                    val template = templateManager.createTemplate("", "")
                    template.addTextSegment(" ")
                    template.addVariable("name", ConstantNode("name"), true)
                    template.addTextSegment(" = ")
                    template.addVariable("value", ConstantNode("value"), true)
                    template.addTextSegment(" ")
                    template.addClosingBraceIfMissing(context)
                    templateManager.startTemplate(hostEditorOf(context), template)
                }.bold(),
            priority,
        ),
    )
}

/**
 * Adds the space-plus-closing-brace text that ends this entry's opening tag, as the template's
 * own segment - always, regardless of whether a `}}` already sits just ahead (only whitespace in
 * between). A template inserts every subsequent segment sequentially at the caret, so anything
 * this entry adds afterward (e.g. `addBlockCloserIfMissing`'s `{{ /for }}`) ends up *before* an
 * already-existing `}}` left in the document rather than after it - stranding it at the very end
 * (`{{ /for }}}}` instead of closing the opening tag right after "collection"). Deleting a
 * pre-existing `}}` first and always emitting a fresh one as part of this template keeps the
 * whole entry's content in one correctly-ordered sequence.
 *
 * Resolves through the host document/offset: when this completion was served by
 * InjectedJsCompletionProvider (i.e. context.file/context.document are the injected JS
 * file/document), scanning context.document directly would look at the wrong text entirely.
 */
private fun Template.addClosingBraceIfMissing(context: InsertionContext) {
    val manager = InjectedLanguageManager.getInstance(context.project)
    val hostFile = manager.getTopLevelFile(context.file)
    val hostDocument = PsiDocumentManager.getInstance(context.project).getDocument(hostFile)

    if (hostDocument != null) {
        val hostTailOffset =
            if (hostFile === context.file) context.tailOffset else manager.injectedToHost(context.file, context.tailOffset)

        val text = hostDocument.charsSequence
        var offset = hostTailOffset
        while (offset < text.length && Character.isWhitespace(text[offset])) {
            offset++
        }

        if (offset + 1 < text.length && text[offset] == '}' && text[offset + 1] == '}') {
            hostDocument.deleteString(hostTailOffset, offset + 2)
        }
    }

    addTextSegment(" }}")
}

/**
 * The real host editor for [context], unwrapping an injected JS `EditorWindow` back to its
 * delegate. Whenever the caret sits inside `{{ ... }}` content - which for a block keyword's
 * condition/name/value placeholder is essentially always, since typing anything past the
 * keyword itself lands in JS-injectable territory - the item was served by
 * InjectedJsCompletionProvider, whose context.editor is the injected editor, not the real one.
 * Starting a template against that leaves the injected PSI/document in a stale or invalid state
 * once the user interacts with it further.
 */
private fun hostEditorOf(context: InsertionContext): Editor = (context.editor as? EditorWindow)?.delegate ?: context.editor

/**
 * Whether the block just typed at [context]'s insertion point already has a matching closer
 * (`{{ /if }}`, `{{ /for }}`, ...) later in the document, so the caller shouldn't append
 * another one. Mirrors Parser.kt's own open/close stack (`openBlocks`) but scoped forward from
 * just this occurrence: Vento's PSI is flat (an open tag and its closer are siblings, not
 * parent/child - see Parser.parse()'s single top-level loop), so this walks sibling
 * `VENTO_BLOCK`s from the just-typed one, tracking nesting depth for [openType]/[closeType]
 * pairs so a same-named block nested inside this one's body isn't mistaken for its own closer.
 */
private fun alreadyHasCloser(
    context: InsertionContext,
    openType: IElementType,
    closeType: IElementType,
    isBlockShaped: (ASTNode) -> Boolean = { true },
): Boolean =
    try {
        alreadyHasCloserOrThrow(context, openType, closeType, isBlockShaped)
    } catch (e: ProcessCanceledException) {
        throw e
    } catch (e: Exception) {
        // Best-effort optimization: mid-typing PSI (e.g. "{{ for}}" before "value of
        // collection" is filled in) can be malformed enough that PsiElement.getNode() returns
        // null partway through the walk (it carries no @NotNull contract - see FakePsiElement
        // and friends), or some other assumption here doesn't hold. Falling back to "no closer
        // found" just means the caller adds one, same as before this check existed - never
        // worse, and never lets a bug here abort the rest of the template.
        false
    }

private fun alreadyHasCloserOrThrow(
    context: InsertionContext,
    openType: IElementType,
    closeType: IElementType,
    isBlockShaped: (ASTNode) -> Boolean,
): Boolean {
    val manager = InjectedLanguageManager.getInstance(context.project)
    val hostFile = manager.getTopLevelFile(context.file)
    val hostDocument = PsiDocumentManager.getInstance(context.project).getDocument(hostFile) ?: return false
    PsiDocumentManager.getInstance(context.project).commitDocument(hostDocument)
    if (hostFile.textLength == 0) return false

    val hostOffset =
        if (hostFile === context.file) context.tailOffset else manager.injectedToHost(context.file, context.tailOffset)
    val anchor = (hostOffset - 1).coerceIn(0, hostFile.textLength - 1)

    var element: PsiElement? = hostFile.findElementAt(anchor)
    while (element != null && element.node?.elementType != ParserElements.VENTO_BLOCK) {
        element = element.parent
    }

    var sibling = element?.nextSibling
    var depth = 0
    while (sibling != null) {
        if (sibling.node?.elementType == ParserElements.VENTO_BLOCK) {
            val contentNode =
                sibling.node?.getChildren(null)?.firstOrNull {
                    it.elementType != LexerTokens.VBLOCK_OPEN && it.elementType != LexerTokens.VBLOCK_CLOSE
                }
            when (contentNode?.elementType) {
                openType -> if (isBlockShaped(contentNode)) depth++
                closeType ->
                    if (depth == 0) {
                        return true
                    } else {
                        depth--
                    }
            }
        }
        sibling = sibling.nextSibling
    }
    return false
}

/** Appends [closerText] unless [alreadyHasCloser] finds this block already has one. */
private fun Template.addBlockCloserIfMissing(
    context: InsertionContext,
    openType: IElementType,
    closeType: IElementType,
    closerText: String,
    isBlockShaped: (ASTNode) -> Boolean = { true },
) {
    if (!alreadyHasCloser(context, openType, closeType, isBlockShaped)) {
        addTextSegment("\n$closerText")
    }
}

private fun hasChild(node: ASTNode, type: IElementType): Boolean = node.getChildren(null).any { it.elementType == type }

/** `set`/`default` share one element type for both their inline (`= value`) and block forms. */
private fun isBlockForm(node: ASTNode): Boolean = !hasChild(node, LexerTokens.EQUAL)

/** `echo` share one element type for both its inline (`"text"`) and block forms. */
private fun isBlockEcho(node: ASTNode): Boolean = !hasChild(node, ParserElements.STRING_ELEMENT)
