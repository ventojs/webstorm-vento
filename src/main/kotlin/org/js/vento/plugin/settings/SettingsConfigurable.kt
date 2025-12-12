/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.settings

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBCheckBox
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel

/**
 * Provides the UI for Vento plugin settings in the IDE Settings dialog.
 */
class SettingsConfigurable(private val project: Project) : Configurable {
    private var enableFrontmatterCheckBox: JBCheckBox? = null
    private val settings = VentoSettings.getInstance(project)

    override fun getDisplayName(): String = "Vento"

    override fun createComponent(): JComponent {
        enableFrontmatterCheckBox = JBCheckBox("Enable frontmatter highlighting")

        return FormBuilder
            .createFormBuilder()
            .addComponent(enableFrontmatterCheckBox!!)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

    override fun isModified(): Boolean = enableFrontmatterCheckBox?.isSelected != settings.isFrontmatterHighlightingEnabled

    override fun apply() {
        enableFrontmatterCheckBox?.let {
            settings.isFrontmatterHighlightingEnabled = it.isSelected
        }
    }

    override fun reset() {
        enableFrontmatterCheckBox?.isSelected = settings.isFrontmatterHighlightingEnabled
    }

    override fun disposeUIResources() {
        enableFrontmatterCheckBox = null
    }
}
