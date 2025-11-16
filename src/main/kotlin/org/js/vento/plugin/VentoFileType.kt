/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin

import com.intellij.lang.html.HTMLLanguage
import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

object VentoFileType : LanguageFileType(HTMLLanguage.INSTANCE) {
    override fun getName() = "Vento File"

    override fun getDescription() = "Vento template"

    override fun getDefaultExtension() = "vto"

    override fun getIcon(): Icon = Vento.ICON
}
