/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.actions

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import org.js.vento.plugin.BaseElementImpl
import org.js.vento.plugin.HtmlElement
import org.js.vento.plugin.JavaScriptElement
import org.js.vento.plugin.JavaScriptExpressionElement
import org.js.vento.plugin.VentoLanguage

class ToggleCommentAction : AnAction() {
    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun actionPerformed(e: AnActionEvent) {
        val editor = e.getData(CommonDataKeys.EDITOR)
        val project = e.getData(CommonDataKeys.PROJECT)

        if (editor == null || project == null) {
            return
        }

        // Determine if we should comment or uncomment
        if (shouldUncomment(editor)) {
            performUncomment(editor, project)
        } else {
            performComment(editor, project)
        }
    }

    private fun shouldUncomment(editor: Editor): Boolean {
        val document = editor.document
        val selectionModel = editor.selectionModel

        return if (selectionModel.hasSelection()) {
            val selectionStart = selectionModel.selectionStart
            val selectionEnd = selectionModel.selectionEnd
            findCommentBounds(document.text, selectionStart, selectionEnd) != null
        } else {
            // No selection - check around the cursor
            val cursorPos = editor.caretModel.offset
            findCommentAroundCursor(document.text, cursorPos) != null
        }
    }

    private fun performComment(editor: Editor, project: Project) {
        val document = editor.document
        val selectionModel = editor.selectionModel

        // Get the text to comment - either selection or logical selection around the cursor
        val (textToComment, rangeStart, rangeEnd) = getTextToComment(editor) ?: return

        WriteCommandAction.runWriteCommandAction(project) {
            val commentedText =
                if (textToComment.startsWith("{{") && textToComment.endsWith("}}") && !textToComment.startsWith("{{#")) {
                    val isTrimmingStart = textToComment.startsWith("{{-")
                    val isTrimmingEnd = textToComment.endsWith("-}}")

                    val startMarker = if (isTrimmingStart) "{{#-" else "{{#"
                    val endMarker = if (isTrimmingEnd) "-#}}" else "#}}"

                    val contentStart = if (isTrimmingStart) 3 else 2
                    val contentEnd = if (isTrimmingEnd) textToComment.length - 3 else textToComment.length - 2
                    val content = textToComment.substring(contentStart, contentEnd)

                    "$startMarker$content$endMarker"
                } else {
                    "{{# $textToComment #}}"
                }
            document.replaceString(rangeStart, rangeEnd, commentedText)
            selectionModel.setSelection(rangeStart, rangeStart + commentedText.length)
        }
    }

    private fun getTextToComment(editor: Editor): Triple<String, Int, Int>? {
        val selectionModel = editor.selectionModel

        return if (selectionModel.hasSelection()) {
            // Use existing selection
            val selectedText = selectionModel.selectedText ?: return null
            val selectionStart = selectionModel.selectionStart
            val selectionEnd = selectionModel.selectionEnd
            Triple(selectedText, selectionStart, selectionEnd)
        } else {
            // Create logical selection around cursor
            createLogicalSelection(editor)
        }
    }

    private fun createLogicalSelection(editor: Editor): Triple<String, Int, Int>? {
        val project = editor.project ?: return null
        val document = editor.document
        val cursorPos = editor.caretModel.offset
        val psiFile = PsiDocumentManager.getInstance(project).getPsiFile(document) ?: return null

        val elementAtCaret = psiFile.findElementAt(cursorPos)
        val elementAtCaretBack = if (cursorPos > 0) psiFile.findElementAt(cursorPos - 1) else null

        val ventoBlock = findVentoBlock(elementAtCaret) ?: findVentoBlock(elementAtCaretBack)
        if (ventoBlock != null) {
            val range = ventoBlock.textRange
            return Triple(ventoBlock.text, range.startOffset, range.endOffset)
        }

        // Fallback to word or line selection only if we are in a Vento context
        if (isVentoContext(elementAtCaret) || isVentoContext(elementAtCaretBack)) {
            val text = document.text
            // Strategy 1: Select word around cursor
            val wordBounds = findWordBounds(text, cursorPos)
            if (wordBounds != null) {
                val (wordStart, wordEnd) = wordBounds
                return Triple(text.substring(wordStart, wordEnd), wordStart, wordEnd)
            }

            // Strategy 2: Select logical block around cursor (between newlines with content)
            val blockBounds = findLogicalBlock(text, cursorPos)
            if (blockBounds != null) {
                val (blockStart, blockEnd) = blockBounds
                return Triple(text.substring(blockStart, blockEnd), blockStart, blockEnd)
            }
        }

        return null
    }

    private fun isVentoContext(element: PsiElement?): Boolean {
        if (element == null) return false
        var current: PsiElement? = element
        while (current != null && current !is PsiFile) {
            if (current is JavaScriptExpressionElement) return true
            if (current is HtmlElement || current is JavaScriptElement) return false
            if (current.language is VentoLanguage) return true
            current = current.parent
        }
        return false
    }

    private fun findVentoBlock(element: PsiElement?): PsiElement? {
        if (element == null) return null
        var current: PsiElement? = element
        while (current != null && current !is PsiFile) {
            if (current is JavaScriptExpressionElement) return current.parent
            if (current is HtmlElement || current is JavaScriptElement) return null
            if (current is BaseElementImpl) return current
            current = current.parent
        }
        return null
    }

    private fun findWordBounds(text: String, cursorPos: Int): Pair<Int, Int>? {
        if (cursorPos >= text.length) return null

        // Check if the cursor is on a word character
        if (!text[cursorPos].isLetterOrDigit() && text[cursorPos] != '_') {
            return null
        }

        // Find word start
        var wordStart = cursorPos
        while (wordStart > 0 && (text[wordStart - 1].isLetterOrDigit() || text[wordStart - 1] == '_')) {
            wordStart--
        }

        // Find word end
        var wordEnd = cursorPos
        while (wordEnd < text.length && (text[wordEnd].isLetterOrDigit() || text[wordEnd] == '_')) {
            wordEnd++
        }

        return if (wordEnd > wordStart) Pair(wordStart, wordEnd) else null
    }

    private fun findLogicalBlock(text: String, cursorPos: Int): Pair<Int, Int>? {
        // Find the start of meaningful content before the cursor
        var blockStart = cursorPos
        var foundContent = false

        // Go backwards to find start of block
        while (blockStart > 0) {
            val char = text[blockStart - 1]
            if (char == '\n') {
                if (foundContent) {
                    // We've found a newline after finding content, so this is our start
                    break
                }
                // Skip empty lines
            } else if (!char.isWhitespace()) {
                foundContent = true
            }
            blockStart--
        }

        // Find the end of meaningful content after the cursor
        var blockEnd = cursorPos
        foundContent = false

        // Go forwards to find the end of the block
        while (blockEnd < text.length) {
            val char = text[blockEnd]
            if (char == '\n') {
                if (foundContent) {
                    // We've found a newline after finding content, so this is our end
                    break
                }
                // Skip empty lines
            } else if (!char.isWhitespace()) {
                foundContent = true
            }
            blockEnd++
        }

        // Trim whitespace from the bounds
        while (blockStart < blockEnd && text[blockStart].isWhitespace()) blockStart++
        while (blockEnd > blockStart && text[blockEnd - 1].isWhitespace()) blockEnd--

        return if (blockEnd > blockStart) Pair(blockStart, blockEnd) else null
    }

    private fun performUncomment(editor: Editor, project: Project) {
        val document = editor.document
        val selectionModel = editor.selectionModel

        // Get the current selection
        val selectionStart = selectionModel.selectionStart
        val selectionEnd = selectionModel.selectionEnd

        // Find comment bounds
        val (commentStart, commentEnd) =
            if (selectionStart == selectionEnd) {
                findCommentAroundCursor(document.text, selectionStart)
            } else {
                findCommentBounds(document.text, selectionStart, selectionEnd)
            } ?: return

        val commentText = document.text.substring(commentStart, commentEnd)

        // Extract uncommented text
        val uncommentedText =
            when {
                commentText.startsWith("{{#") && commentText.endsWith("#}}") -> {
                    val isStartTrimming = commentText.startsWith("{{#-")
                    val isEndTrimming = commentText.endsWith("-#}}")
                    val contentStart = if (isStartTrimming) 4 else 3
                    val contentEnd = if (isEndTrimming) commentText.length - 4 else commentText.length - 3
                    val content = commentText.substring(contentStart, contentEnd)

                    if (content.startsWith(" ") &&
                        content.endsWith(" ") &&
                        content.trim().startsWith("{{") &&
                        content.trim().endsWith("}}")
                    ) {
                        content.trim()
                    } else {
                        val start = if (isStartTrimming) "{{-" else "{{"
                        val end = if (isEndTrimming) "-}}" else "}}"
                        "$start$content$end"
                    }
                }
                else -> return
            }

        WriteCommandAction.runWriteCommandAction(project) {
            document.replaceString(commentStart, commentEnd, uncommentedText)
            selectionModel.setSelection(commentStart, commentStart + uncommentedText.length)
        }
    }

    private fun findCommentAroundCursor(text: String, cursorPos: Int): Pair<Int, Int>? {
        // Look backwards for comment start
        var start = -1
        for (i in cursorPos downTo 0) {
            if (i + 4 <= text.length && text.substring(i, i + 4) == "{{#-") {
                start = i
                break
            }
            if (i + 3 <= text.length && text.substring(i, i + 3) == "{{#") {
                start = i
                break
            }
        }
        if (start == -1) return null

        // Look forwards for the comment end
        var end = -1
        for (i in start until text.length) {
            if (i + 4 <= text.length && text.substring(i, i + 4) == "-#}}") {
                end = i + 4
                break
            }
            if (i + 3 <= text.length && text.substring(i, i + 3) == "#}}") {
                end = i + 3
                break
            }
        }
        if (end == -1 || end < cursorPos) return null

        return start to end
    }

    private fun findCommentBounds(text: String, selStart: Int, selEnd: Int): Pair<Int, Int>? {
        val commentAtStart = findCommentAroundCursor(text, selStart)
        if (commentAtStart != null && commentAtStart.second >= selEnd) {
            return commentAtStart
        }

        // Check if selection already includes the comment markers exactly
        val selectedText = text.substring(selStart, selEnd)
        if ((selectedText.startsWith("{{#") || selectedText.startsWith("{{#-")) &&
            (selectedText.endsWith("#}}") || selectedText.endsWith("-#}}"))
        ) {
            return selStart to selEnd
        }

        return null
    }

    override fun update(e: AnActionEvent) {
        val editor = e.getData(CommonDataKeys.EDITOR)
        val psiFile = e.getData(CommonDataKeys.PSI_FILE)

        if (editor == null || psiFile == null) {
            e.presentation.isEnabled = false
            return
        }

        // Check if we're in a Vento file
        val isVentoFile = psiFile.language is VentoLanguage || psiFile.viewProvider.baseLanguage is VentoLanguage
        if (!isVentoFile) {
            e.presentation.isEnabled = false
            return
        }

        val offset = editor.caretModel.offset
        val selectionModel = editor.selectionModel

        if (selectionModel.hasSelection()) {
            val selectedText = selectionModel.selectedText ?: ""
            // Enable if the selection is a Vento comment (for uncommenting)
            if (findCommentBounds(editor.document.text, selectionModel.selectionStart, selectionModel.selectionEnd) != null) {
                e.presentation.isEnabled = true
                e.presentation.text = "Uncomment Vento Block"
                return
            }

            // Enable if the selection is a Vento block or if we are in a Vento context
            val elementAtStart = psiFile.findElementAt(selectionModel.selectionStart)
            val elementAtEnd = if (selectionModel.selectionEnd > 0) psiFile.findElementAt(selectionModel.selectionEnd - 1) else null

            val isVentoSelection = isVentoContext(elementAtStart) || isVentoContext(elementAtEnd)
            if (!isVentoSelection) {
                e.presentation.isEnabled = false
                return
            }
        } else {
            val elementAtCaret = psiFile.findElementAt(offset)
            val elementAtCaretBack = if (offset > 0) psiFile.findElementAt(offset - 1) else null

            val isUncomment = findCommentAroundCursor(editor.document.text, offset) != null
            if (isUncomment) {
                e.presentation.isEnabled = true
                e.presentation.text = "Uncomment Vento Block"
                return
            }

            val isVentoContext = isVentoContext(elementAtCaret) || isVentoContext(elementAtCaretBack)
            if (!isVentoContext) {
                e.presentation.isEnabled = false
                return
            }
        }

        e.presentation.isEnabled = true
        e.presentation.text = "Comment Vento Block"
    }
}
