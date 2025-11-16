/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin

import com.intellij.lang.DependentLanguage
import com.intellij.lang.Language
import com.intellij.lang.html.HTMLLanguage

/**
 * Represents the Vento programming language within the IntelliJ Platform.
 *
 * The [VentoLanguage] object extends the [Language] class, specifying "Vento" as its unique identifier.
 * This object is essential for integrating the Vento language support into JetBrains IDEs like WebStorm.
 *
 * @constructor Creates an instance of [VentoLanguage] with the specified language ID.
 */
object VentoLanguage : Language(HTMLLanguage.INSTANCE, "Vento"), DependentLanguage {
    private fun readResolve(): Any = VentoLanguage
}
