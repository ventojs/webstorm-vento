/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.editor

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.lang.ASTNode
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NlsSafe
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import org.js.vento.plugin.file.VentoFileType
import org.js.vento.plugin.lexer.LexerTokens
import org.js.vento.plugin.parser.ParserElements

/**
 * Handles auto-closing of Vento template delimiters.
 * When the user types "{{", this automatically inserts "}}" and positions the cursor between them.
 */
class VentoTypedHandler : TypedHandlerDelegate() {
    override fun beforeCharTyped(c: Char, project: Project, editor: Editor, file: PsiFile, fileType: FileType): Result {
        // Only handle Vento files
        if (file.fileType != VentoFileType) {
            return Result.CONTINUE
        }

        if (c == '{') {
            val offset = editor.caretModel.offset
            val document = editor.document
            val text = document.charsSequence

            if (previousIs('{', offset, text)) {
                if (!alreadyClosed(offset, text)) {
                    // Consume the '{' and handle it ourselves
                    WriteCommandAction.runWriteCommandAction(project) {
                        // Insert the second '{' plus the closing '}}'
                        document.insertString(offset, "{}}")
                        // Position cursor between {{ and }}
                        editor.caretModel.moveToOffset(offset + 1)
                    }
                    return Result.STOP
                }
            }
        }

        if (c == '}') {
            val offset = editor.caretModel.offset
            val text = editor.document.charsSequence

            // Type over an already-present '}' that's part of a `}}` pair (most likely the
            // one our own `{{` auto-close inserted) instead of inserting a duplicate. Without
            // this, typing out the closing braces of a block by hand after auto-close already
            // supplied them leaves a stray extra `}}` behind.
            if (isPartOfCloseDelimiter(offset, text)) {
                // Typing over the second '}' of the pair completes it - but since the actual
                // character insertion is suppressed below (Result.STOP), charTyped() never
                // fires for this keystroke, so the close-tag insertion has to happen here.
                val completesPair = offset >= 1 && text[offset - 1] == '}'
                editor.caretModel.moveToOffset(offset + 1)
                if (completesPair) {
                    insertMatchingCloseTag(project, editor, offset + 1)
                }
                return Result.STOP
            }
        }

        return Result.CONTINUE
    }

    private fun isPartOfCloseDelimiter(offset: Int, text: @NlsSafe CharSequence): Boolean {
        if (offset >= text.length || text[offset] != '}') return false
        val nextIsClose = offset + 1 < text.length && text[offset + 1] == '}'
        val previousIsClose = offset >= 1 && text[offset - 1] == '}'
        return nextIsClose || previousIsClose
    }

    private fun alreadyClosed(offset: Int, text: @NlsSafe CharSequence): Boolean {
        val hasClosingAhead =
            offset + 1 < text.length &&
                text[offset] == '}' &&
                text[offset + 1] == '}'
        return hasClosingAhead
    }

    private fun previousIs(
        character: Char,
        offset: Int,
        text: @NlsSafe CharSequence,
    ): Boolean = offset >= 1 && text[offset - 1] == character

    /**
     * When a `}}` just completed a block-shaped opening tag (`if`, `for`, `function`,
     * `fragment`, `slot`, block-form `echo`/`export`/`set`/`default`), auto-insert the
     * matching `{{ /xxx }}` on a new line below, with the cursor on a blank line between,
     * mirroring how the completion snippets in `openingKeywords.kt` already do this when a
     * suggestion is accepted instead of typed out by hand.
     */
    override fun charTyped(c: Char, project: Project, editor: Editor, file: PsiFile): Result {
        if (file.fileType != VentoFileType) {
            return Result.CONTINUE
        }

        if (c == '}') {
            val document = editor.document
            val offset = editor.caretModel.offset
            val text = document.charsSequence
            if (offset >= 2 && text[offset - 1] == '}' && text[offset - 2] == '}') {
                insertMatchingCloseTag(project, editor, offset)
            }
        }

        return Result.CONTINUE
    }

    private fun insertMatchingCloseTag(project: Project, editor: Editor, blockCloseOffset: Int) {
        val document = editor.document

        val psiDocumentManager = PsiDocumentManager.getInstance(project)
        psiDocumentManager.commitDocument(document)
        val psiFile = psiDocumentManager.getPsiFile(document) ?: return

        val closeLeaf = psiFile.findElementAt(blockCloseOffset - 1) ?: return
        val ventoBlock = closeLeaf.parent ?: return
        val contentNode =
            ventoBlock.node.getChildren(null).firstOrNull {
                it.elementType != LexerTokens.VBLOCK_OPEN && it.elementType != LexerTokens.VBLOCK_CLOSE
            } ?: return

        val closingText = closingTagFor(contentNode) ?: return

        val lineNumber = document.getLineNumber(ventoBlock.textRange.startOffset)
        val lineStart = document.getLineStartOffset(lineNumber)
        val indent =
            document
                .getText(TextRange(lineStart, ventoBlock.textRange.startOffset))
                .takeWhile { it == ' ' || it == '\t' }

        val insertText = "\n$indent\n$indent$closingText"
        val caretTargetOffset = blockCloseOffset + 1 + indent.length

        WriteCommandAction.runWriteCommandAction(project) {
            document.insertString(blockCloseOffset, insertText)
            editor.caretModel.moveToOffset(caretTargetOffset)
        }
    }

    /**
     * Maps the parsed content of a `{{ ... }}` block to the text of its matching close tag,
     * or `null` when the block has no closer at all, or the specific instance turned out to
     * be a self-closing inline form (e.g. `{{ set x = 1 }}` vs. block-form `{{ set x }}`).
     */
    private fun closingTagFor(contentNode: ASTNode): String? =
        when (contentNode.elementType) {
            ParserElements.IF_ELEMENT -> "{{ /if }}"
            ParserElements.FOR_ELEMENT -> "{{ /for }}"
            // Top-level `{{ function ... }}` / `{{ async function ... }}` dispatch to
            // parseFunctionSignature() directly, producing FUNCTION_SIGNATURE_ELEMENT as the
            // block child - FUNCTION_ELEMENT only occurs for an inline `function(){...}`
            // nested inside a set/default RHS and has no `{{ /function }}` relationship.
            ParserElements.FUNCTION_SIGNATURE_ELEMENT -> "{{ /function }}"
            ParserElements.FRAGMENT_ELEMENT -> "{{ /fragment }}"
            ParserElements.LAYOUT_SLOT_ELEMENT -> "{{ /slot }}"
            // The block-open form of export (bare `export name`, or `export function ...`
            // without `=`) - EXPORT_ELEMENT is the separate, self-closing `export name = value`
            // form and never needs a closer.
            ParserElements.EXPORT_OPEN_ELEMENT -> "{{ /export }}"
            ParserElements.ECHO_ELEMENT ->
                if (hasChild(contentNode, ParserElements.STRING_ELEMENT)) null else "{{ /echo }}"
            ParserElements.SET_ELEMENT -> if (isInlineForm(contentNode)) null else "{{ /set }}"
            ParserElements.DEFAULT_ELEMENT -> if (isInlineForm(contentNode)) null else "{{ /default }}"
            // LAYOUT_ELEMENT is intentionally excluded: `{{ layout "file.vto" data }}` with no
            // closing tag is a valid, commonly-used form that sets the layout for the rest of
            // the file - auto-inserting `{{ /layout }}` would be wrong far more often than right.
            else -> null
        }

    /**
     * `set`/`default` share one element type for both their inline self-closing form
     * (`{{ set x = 1 }}`) and their block form (`{{ set x }}`). Both parsers call
     * `optional(builder, EQUAL, ...)` unconditionally right after the variable name, so an
     * `EQUAL` leaf is present among direct children exactly when this is the inline form.
     */
    private fun isInlineForm(contentNode: ASTNode): Boolean = hasChild(contentNode, LexerTokens.EQUAL)

    private fun hasChild(node: ASTNode, type: IElementType): Boolean = node.getChildren(null).any { it.elementType == type }
}
