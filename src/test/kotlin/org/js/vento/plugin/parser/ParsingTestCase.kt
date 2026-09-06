/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.testFramework.ParsingTestCase
import org.js.vento.plugin.VentoParserDefinition
import org.js.vento.plugin.settings.Settings

abstract class ParsingTestCase(val path: String = "") : ParsingTestCase(path, "vto", VentoParserDefinition()) {
    override fun setUp() {
        super.setUp()
        project.registerService(Settings::class.java, Settings(project))
    }
}
