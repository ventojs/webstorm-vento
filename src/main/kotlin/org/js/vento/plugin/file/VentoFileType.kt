/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.file

import com.intellij.ide.highlighter.XmlLikeFileType
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ReadAction
import com.intellij.openapi.fileTypes.CharsetUtil
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.fileTypes.PlainTextFileType
import com.intellij.openapi.fileTypes.TemplateLanguageFileType
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NlsContexts
import com.intellij.openapi.util.NlsSafe
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.templateLanguages.TemplateDataLanguageMappings
import org.jetbrains.annotations.NonNls
import org.js.vento.plugin.Vento
import org.js.vento.plugin.VentoLanguage
import org.js.vento.plugin.settings.Settings
import java.nio.charset.Charset
import javax.swing.Icon

/**
 * Represents the file type for Vento template files within the IntelliJ Platform.
 *
 * The [VentoFileType] object extends [com.intellij.openapi.fileTypes.LanguageFileType], associating the Vento language
 * with its specific file characteristics such as name, description, default extension, and icon.
 *
 * @constructor Creates an instance of [VentoFileType] with the specified language support.
 */
object VentoFileType : XmlLikeFileType(VentoLanguage), TemplateLanguageFileType {
    override fun getName(): @NonNls String = "Vento File"

    override fun getDescription(): @NlsContexts.Label String = "Vento template"

    override fun getDefaultExtension(): @NlsSafe String = "vto"

    override fun getIcon(): Icon = Vento.ICON

    override fun extractCharsetFromFileContent(project: Project?, file: VirtualFile?, content: CharSequence): Charset? {
        val associatedFileType = getAssociatedFileType(file, project) ?: return null

        return CharsetUtil.extractCharsetFromFileContent(project, file, associatedFileType, content)
    }

    internal fun getAssociatedFileType(file: VirtualFile?, project: Project?): LanguageFileType? {
        if (project == null || project.isDisposed) {
            return null
        }

        val app = ApplicationManager.getApplication()

        // Access TemplateDataLanguageMappings under read action, since it hits workspace model / file index.
        val language =
            if (app.isReadAccessAllowed) {
                TemplateDataLanguageMappings.getInstance(project).getMapping(file)
            } else {
                ReadAction.compute<com.intellij.lang.Language?, Throwable> {
                    TemplateDataLanguageMappings.getInstance(project).getMapping(file)
                }
            }

        var associatedFileType: LanguageFileType? = language?.associatedFileType

        if (language == null || associatedFileType == null) {
            val settings = Settings.getInstance(project)
            associatedFileType =
                if (settings.isHtmlHighlightingEnabled) {
                    VentoLanguage.getDefaultTemplateLang()
                } else {
                    PlainTextFileType.INSTANCE
                }
        }

        return associatedFileType
    }
}
