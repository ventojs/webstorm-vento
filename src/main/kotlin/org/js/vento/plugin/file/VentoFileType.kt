/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.file

import com.intellij.ide.highlighter.XmlLikeFileType
import com.intellij.openapi.fileTypes.CharsetUtil
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.fileTypes.TemplateLanguageFileType
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NlsContexts
import com.intellij.openapi.util.NlsSafe
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.templateLanguages.TemplateDataLanguageMappings
import org.jetbrains.annotations.NonNls
import org.js.vento.plugin.Vento
import org.js.vento.plugin.VentoLanguage
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

    private fun getAssociatedFileType(file: VirtualFile?, project: Project?): LanguageFileType? {
        if (project == null) {
            return null
        }

        val language = TemplateDataLanguageMappings.getInstance(project).getMapping(file)

        var associatedFileType: LanguageFileType? = null

        if (language != null) {
            associatedFileType = language.getAssociatedFileType()
        }

        if (language == null || associatedFileType == null) {
            associatedFileType = VentoLanguage.getDefaultTemplateLang()
        }

        return associatedFileType
    }
}
