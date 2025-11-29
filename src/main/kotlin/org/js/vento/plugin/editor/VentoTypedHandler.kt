/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.editor

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import org.js.vento.plugin.file.VentoFileType

/**
 * Handles auto-closing of Vento template delimiters.
 * When the user types "{{", this automatically inserts "}}" and positions the cursor between them.
 */
class VentoTypedHandler : TypedHandlerDelegate() {
    override fun charTyped(c: Char, project: Project, editor: Editor, file: PsiFile): Result {
        // Only handle Vento files
        if (file.fileType != VentoFileType) {
            return Result.CONTINUE
        }

        // Handle the second '{' character to trigger auto-closing
        if (c == '{') {
            val offset = editor.caretModel.offset
            val document = editor.document
            val text = document.charsSequence

            // Check if the previous character is also '{'
            if (offset >= 2 && text[offset - 2] == '{' && text[offset - 1] == '{') {
                // Check if we already have closing braces
                if (offset < text.length - 1 && text[offset] == '}' && text[offset + 1] == '}') {
                    return Result.CONTINUE
                }

                // Insert closing braces
                document.insertString(offset, "}}")
                editor.caretModel.moveToOffset(offset)
                return Result.STOP
            }
        }

        return Result.CONTINUE
    }

    override fun beforeCharTyped(c: Char, project: Project, editor: Editor, file: PsiFile, fileType: FileType): Result {
        // Only handle Vento files
        if (file.fileType != VentoFileType) {
            return Result.CONTINUE
        }

        // Handle auto-removal of closing braces when backspacing
        // This is handled by the IDE's smart backspace, but we can add custom logic here if needed

        return Result.CONTINUE
    }
}
