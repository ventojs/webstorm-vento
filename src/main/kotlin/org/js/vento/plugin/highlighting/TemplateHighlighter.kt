/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.highlighting

import com.intellij.openapi.editor.colors.EditorColorsScheme
import com.intellij.openapi.editor.ex.util.LayerDescriptor
import com.intellij.openapi.editor.ex.util.LayeredLexerEditorHighlighter
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.fileTypes.FileTypes
import com.intellij.openapi.fileTypes.PlainTextFileType
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.templateLanguages.TemplateDataLanguageMappings
import org.js.vento.plugin.VentoLanguage
import org.js.vento.plugin.lexer.LexerTokens
import org.js.vento.plugin.settings.Settings

class TemplateHighlighter(project: Project?, virtualFile: VirtualFile?, colors: EditorColorsScheme) :
    LayeredLexerEditorHighlighter(
        if (project != null && Settings.getInstance(project).isFrontmatterHighlightingEnabled) {
            SyntaxHighlighter()
        } else {
            SyntaxHighlighterWithoutFrontmatter()
        },
        colors,
    ) {
    init {
        var type: FileType =
            if (project == null || virtualFile == null) {
                FileTypes.PLAIN_TEXT
            } else {
                var type: FileType? = null
                val language = TemplateDataLanguageMappings.getInstance(project).getMapping(virtualFile)
                if (language != null) type = language.associatedFileType
                if (type == null) {
                    type =
                        if (Settings.getInstance(project).isHtmlHighlightingEnabled) {
                            VentoLanguage.getDefaultTemplateLang()
                        } else {
                            PlainTextFileType.INSTANCE
                        }
                }
                type
            }

        val outerHighlighter: SyntaxHighlighter? =
            SyntaxHighlighterFactory.getSyntaxHighlighter(type, project, virtualFile)

        registerLayer(LexerTokens.HTML, LayerDescriptor(outerHighlighter!!, ""))
    }
}
