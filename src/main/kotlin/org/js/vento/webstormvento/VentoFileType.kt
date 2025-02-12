/*
 * Copyright (c) 2023 Óscar Otero
 * All rights reserved.
 */

package org.js.vento.webstormvento

import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.util.NlsContexts
import com.intellij.openapi.util.NlsSafe
import org.jetbrains.annotations.NonNls
import javax.swing.Icon

/**
 * Represents the file type for Vento template files within the IntelliJ Platform.
 *
 * The [VentoFileType] object extends [LanguageFileType], associating the Vento language
 * with its specific file characteristics such as name, description, default extension, and icon.
 *
 * @constructor Creates an instance of [VentoFileType] with the specified language support.
 */
object VentoFileType : LanguageFileType(VentoLanguage) {
    override fun getName(): @NonNls String = "Vento File"
    override fun getDescription(): @NlsContexts.Label String = "Vento template file"

    @Suppress("UnstableApiUsage")
    override fun getDefaultExtension(): @NlsSafe String = "vto"
    override fun getIcon(): Icon? = Vento.ICON
}