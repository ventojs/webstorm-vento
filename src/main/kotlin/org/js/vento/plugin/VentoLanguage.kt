/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin

import com.intellij.ide.highlighter.HtmlFileType
import com.intellij.lang.InjectableLanguage
import com.intellij.lang.Language
import com.intellij.lang.html.HTMLLanguage
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.psi.templateLanguages.TemplateLanguage

/**
 * Represents the Vento programming language within the IntelliJ Platform.
 *
 * The [VentoLanguage] object extends the [Language] class, specifying "Vento" as its unique identifier.
 * This object is essential for integrating the Vento language support into JetBrains IDEs like WebStorm.
 *
 * @constructor Creates an instance of [VentoLanguage] with the specified language ID.
 */
object VentoLanguage : Language(HTMLLanguage.INSTANCE, "Vento"), TemplateLanguage, InjectableLanguage {
    private fun readResolve(): Any = VentoLanguage

    override fun isCaseSensitive(): Boolean = false

    override fun getDisplayName(): String = "Vento"

    override fun getID(): String = "Vento"

    fun getDefaultTemplateLang(): LanguageFileType = HtmlFileType.INSTANCE
}
