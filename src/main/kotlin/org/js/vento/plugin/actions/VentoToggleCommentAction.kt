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
import com.intellij.psi.PsiFile
import org.js.vento.plugin.filetype.VentoFileType

class VentoToggleCommentAction : AnAction() {
    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun actionPerformed(e: AnActionEvent) {
        val editor = e.getData(CommonDataKeys.EDITOR)
        val project = e.getData(CommonDataKeys.PROJECT)

        if (editor == null || project == null) {
            return
        }

        val document = editor.document
        val selectionModel = editor.selectionModel

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
            // No selection - check around cursor
            val cursorPos = editor.caretModel.offset
            findCommentAroundCursor(document.text, cursorPos) != null
        }
    }

    private fun performComment(editor: Editor, project: Project) {
        val document = editor.document
        val selectionModel = editor.selectionModel

        // Get the text to comment - either selection or logical selection around cursor
        val (textToComment, rangeStart, rangeEnd) = getTextToComment(editor) ?: return

        WriteCommandAction.runWriteCommandAction(project) {
            val commentedText = "{{# $textToComment #}}"
            document.replaceString(rangeStart, rangeEnd, commentedText)
            selectionModel.setSelection(rangeStart, rangeStart + commentedText.length)
        }
    }

    private fun getTextToComment(editor: Editor): Triple<String, Int, Int>? {
        val document = editor.document
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
        val document = editor.document
        val cursorPos = editor.caretModel.offset
        val text = document.text

        // Strategy 1: Select current line if it's not empty
        val lineNumber = document.getLineNumber(cursorPos)
        val lineStart = document.getLineStartOffset(lineNumber)
        val lineEnd = document.getLineEndOffset(lineNumber)
        val lineText = text.substring(lineStart, lineEnd).trim()

        if (lineText.isNotEmpty()) {
            // Find the actual content bounds (excluding leading/trailing whitespace)
            val trimmedStart = lineStart + text.substring(lineStart, lineEnd).indexOfFirst { !it.isWhitespace() }
            val trimmedEnd = lineStart + text.substring(lineStart, lineEnd).indexOfLast { !it.isWhitespace() } + 1
            return Triple(text.substring(trimmedStart, trimmedEnd), trimmedStart, trimmedEnd)
        }

        // Strategy 2: Select word around cursor if on a word
        val wordBounds = findWordBounds(text, cursorPos)
        if (wordBounds != null) {
            val (wordStart, wordEnd) = wordBounds
            return Triple(text.substring(wordStart, wordEnd), wordStart, wordEnd)
        }

        // Strategy 3: Select logical block around cursor (between newlines with content)
        val blockBounds = findLogicalBlock(text, cursorPos)
        if (blockBounds != null) {
            val (blockStart, blockEnd) = blockBounds
            return Triple(text.substring(blockStart, blockEnd), blockStart, blockEnd)
        }

        return null
    }

    private fun findWordBounds(text: String, cursorPos: Int): Pair<Int, Int>? {
        if (cursorPos >= text.length) return null

        // Check if cursor is on a word character
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
        // Find the start of meaningful content before cursor
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

        // Find the end of meaningful content after cursor
        var blockEnd = cursorPos
        foundContent = false

        // Go forwards to find end of block
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
                commentText.startsWith("{{#-") && commentText.endsWith("-#}}") -> {
                    commentText.substring(4, commentText.length - 4).trim()
                }
                commentText.startsWith("{{#") && commentText.endsWith("#}}") -> {
                    commentText.substring(3, commentText.length - 3).trim()
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
        var start = cursorPos
        while (start > 3) {
            if (text.substring(maxOf(0, start - 3), minOf(text.length, start + 1)) == "{{#" ||
                (start > 4 && text.substring(maxOf(0, start - 4), start) == "{{#-")
            ) {
                start = if (start > 4 && text.substring(start - 4, start) == "{{#-") start - 4 else start - 3
                break
            }
            start--
        }

        // Look forwards for comment end
        var end = cursorPos
        while (end < text.length - 3) {
            if (end + 3 <= text.length && text.substring(end, end + 3) == "#}}" ||
                (end + 4 <= text.length && text.substring(end, end + 4) == "-#}}")
            ) {
                end += if (end + 3 <= text.length && text.substring(end, end + 3) == "#}}") 3 else 4
                break
            }
            end++
        }

        // Verify we found a valid comment
        if (start < end && start >= 0 && end <= text.length) {
            val commentText = text.substring(start, end)
            return if ((commentText.startsWith("{{#-") && commentText.endsWith("-#}}")) ||
                (commentText.startsWith("{{#") && commentText.endsWith("#}}"))
            ) {
                start to end
            } else {
                null
            }
        }
        return null
    }

    private fun findCommentBounds(text: String, selStart: Int, selEnd: Int): Pair<Int, Int>? {
        // Check if selection already includes the comment markers
        val selectedText = text.substring(selStart, selEnd)
        if ((selectedText.startsWith("{{#-") && selectedText.endsWith("-#}}")) ||
            (selectedText.startsWith("{{#") && selectedText.endsWith("#}}"))
        ) {
            return selStart to selEnd
        }

        // Try to expand selection to include comment markers
        val expandedStart = maxOf(0, selStart - 4)
        val expandedEnd = minOf(text.length, selEnd + 4)

        // Look for comment start before selection
        var commentStart = selStart
        for (i in expandedStart..selStart) {
            if (i + 3 <= text.length && text.substring(i, i + 3) == "{{#") {
                commentStart = i
                break
            }
            if (i + 4 <= text.length && text.substring(i, i + 4) == "{{#-") {
                commentStart = i
                break
            }
        }

        // Look for comment end after selection
        var commentEnd = selEnd
        for (i in selEnd..expandedEnd - 3) {
            if (i + 3 <= text.length && text.substring(i, i + 3) == "#}}") {
                commentEnd = i + 3
                break
            }
            if (i + 4 <= text.length && text.substring(i, i + 4) == "-#}}") {
                commentEnd = i + 4
                break
            }
        }

        // Verify we found a valid comment
        if (commentStart < selStart && commentEnd > selEnd) {
            val commentText = text.substring(commentStart, commentEnd)
            if ((commentText.startsWith("{{#-") && commentText.endsWith("-#}}")) ||
                (commentText.startsWith("{{#") && commentText.endsWith("#}}"))
            ) {
                return commentStart to commentEnd
            }
        }

        return null
    }

    override fun update(e: AnActionEvent) {
        val editor = e.getData(CommonDataKeys.EDITOR)
        val psiFile = e.getData(CommonDataKeys.PSI_FILE)

        // Check if we're in a Vento file
        val isVentoFile = psiFile?.fileType == VentoFileType

        if (!isVentoFile || editor == null) {
            e.presentation.isEnabled = false
            return
        }

        // Enable if we have a selection, can find a comment around cursor, or can create logical selection
        val hasSelection = editor.selectionModel.hasSelection()
        val canUncomment = findCommentAroundCursor(editor.document.text, editor.caretModel.offset) != null
        val canComment = hasSelection || getTextToComment(editor) != null

        e.presentation.isEnabled = canUncomment || canComment

        // Update text based on current state
        e.presentation.text =
            if (shouldUncomment(editor)) {
                "Uncomment Vento Block"
            } else {
                "Comment Vento Block"
            }
    }
}
