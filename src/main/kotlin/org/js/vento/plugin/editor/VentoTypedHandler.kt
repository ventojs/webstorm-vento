/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.editor

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NlsSafe
import com.intellij.psi.PsiFile
import org.js.vento.plugin.file.VentoFileType

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

        return Result.CONTINUE
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

    override fun charTyped(c: Char, project: Project, editor: Editor, file: PsiFile): Result {
//        // Only handle Vento files
//        if (file.fileType != VentoFileType) {
//            return Result.CONTINUE
//        }
//
//        val offset = editor.caretModel.offset
//        val previousChars = editor.document.getText(TextRange(offset - 3, offset))
//
//        if (previousChars == "{{ ") {
//            val document = editor.document
//
//            document.insertString(offset, "}")
//            editor.caretModel.moveToOffset(offset)
//            return Result.STOP
//        }

        return Result.CONTINUE
    }
}
