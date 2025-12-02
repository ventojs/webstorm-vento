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
import com.intellij.lang.injection.InjectedLanguageManager
import org.js.vento.plugin.Vento

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
                    templateManager.startTemplate(context.editor, template)
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
                    templateManager.startTemplate(context.editor, template)
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
                    templateManager.startTemplate(context.editor, template)
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
                    template.addTextSegment("\n{{ /echo }}")
                    templateManager.startTemplate(context.editor, template)
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
                    templateManager.startTemplate(context.editor, template)
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
                    template.addTextSegment("\n{{ /export }}")
                    templateManager.startTemplate(context.editor, template)
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
                    template.addTextSegment("\n{{ /for }}")
                    templateManager.startTemplate(context.editor, template)
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
                    template.addTextSegment("\n{{ /function }}")
                    templateManager.startTemplate(context.editor, template)
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
                    template.addTextSegment("\n{{ /if }}")
                    templateManager.startTemplate(context.editor, template)
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
                    templateManager.startTemplate(context.editor, template)
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
                    templateManager.startTemplate(context.editor, template)
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
                    template.addTextSegment("\n{{ /layout }}")
                    templateManager.startTemplate(context.editor, template)
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
                    template.addTextSegment("\n{{ /set }}")
                    templateManager.startTemplate(context.editor, template)
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
                    templateManager.startTemplate(context.editor, template)
                }.bold(),
            priority,
        ),
    )
}

private fun Template.addClosingBraceIfMissing(context: InsertionContext) {
    val project = context.project
    val file = context.file
    val manager = InjectedLanguageManager.getInstance(project)

    var hasClosing = false
    var deleteWhitespace = false
    var whitespaceEnd = context.tailOffset

    if (manager.isInjectedFragment(file)) {
        val host = manager.getInjectionHost(file)
        // If we are injected, the host usually contains the braces.
        // We check if the host text ends with }}
        if (host != null && host.text.trimEnd().endsWith("}}")) {
            hasClosing = true
        }
    } else {
        // Not injected, look ahead in the document
        val text = context.document.charsSequence
        var offset = context.tailOffset
        // Skip whitespace
        while (offset < text.length && Character.isWhitespace(text[offset])) {
            offset++
        }
        // Check for }}
        if (offset + 1 < text.length && text[offset] == '}' && text[offset + 1] == '}') {
            hasClosing = true
            deleteWhitespace = true
            whitespaceEnd = offset
        }
    }

    if (hasClosing) {
        if (deleteWhitespace) {
            // Remove the whitespace we skipped so the template is snug against the existing }}
            context.document.deleteString(context.tailOffset, whitespaceEnd)
        }
        // Just add a space to separate the content from the existing }}
        this.addTextSegment(" ")
    } else {
        // Add both space and braces
        this.addTextSegment(" }}")
    }
}
