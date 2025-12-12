/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.ex.EditorEx
import com.intellij.openapi.editor.highlighter.EditorHighlighterFactory
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.psi.PsiManager
import org.js.vento.plugin.file.VentoFileType

/**
 * Listener that rehighlights Vento files when settings change.
 */
class VentoSettingsListener(private val project: Project) : Settings.SettingsListener {
    override fun settingsChanged() {
        ApplicationManager.getApplication().invokeLater {
            rehighlightOpenVentoFiles()
        }
    }

    private fun rehighlightOpenVentoFiles() {
        val fileEditorManager = FileEditorManager.getInstance(project)
        val psiManager = PsiManager.getInstance(project)

        fileEditorManager.openFiles.forEach { virtualFile ->
            if (virtualFile.fileType == VentoFileType) {
                // 1) Replace the EditorHighlighter so it reflects the latest settings
                fileEditorManager.getEditors(virtualFile).forEach { editor ->
                    if (editor is TextEditor) {
                        val editorEx = editor.editor as? EditorEx
                        if (editorEx != null) {
                            val factory = EditorHighlighterFactory.getInstance()
                            val newHighlighter = factory.createEditorHighlighter(project, virtualFile)
                            ApplicationManager.getApplication().runWriteAction {
                                editorEx.setHighlighter(newHighlighter)
                            }
                        }
                    }
                }

                // 2) Restart daemon to force re-analysis and repaint
                psiManager.findFile(virtualFile)?.let { psiFile ->
                    ApplicationManager.getApplication().runWriteAction {
                        com.intellij.codeInsight.daemon.DaemonCodeAnalyzer
                            .getInstance(project)
                            .restart(psiFile)
                    }
                }
            }
        }
    }
}

/**
 * Project startup activity that registers the settings listener.
 */
class SettingsStartupActivity : ProjectActivity {
    override suspend fun execute(project: Project) {
        val listener = VentoSettingsListener(project)
        project.messageBus.connect().subscribe(Settings.SETTINGS_TOPIC, listener)
    }
}
