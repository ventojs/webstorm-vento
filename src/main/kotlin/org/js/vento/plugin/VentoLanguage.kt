/*
 * Copyright (c) 2023 Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin

import com.intellij.lang.Language

/**
 * Represents the Vento programming language within the IntelliJ Platform.
 *
 * The [VentoLanguage] object extends the [Language] class, specifying "Vento" as its unique identifier.
 * This object is essential for integrating the Vento language support into JetBrains IDEs like WebStorm.
 *
 * @constructor Creates an instance of [VentoLanguage] with the specified language ID.
 */
object VentoLanguage : Language("Vento") {
    private fun readResolve(): Any = VentoLanguage
}