package com.github.illyrius666.webstormvento

import com.intellij.openapi.options.Configurable
import javax.swing.JComponent

class VentoSettingsConfigurable : Configurable {
    private var mySettingsComponent: VentoSettingsComponent? = null

    override fun getDisplayName(): String = "Vento.js"

    override fun getPreferredFocusedComponent(): JComponent? = mySettingsComponent?.preferredFocusedComponent

    override fun createComponent(): JComponent? {
        mySettingsComponent = VentoSettingsComponent()
        return mySettingsComponent?.panel
    }

    override fun isModified(): Boolean {
        val settings: VentoSettingsState = VentoSettingsState.Companion.instance
        return mySettingsComponent?.showGutterIconsStatus != settings.showGutterIcons
    }

    override fun apply() {
        val settings: VentoSettingsState = VentoSettingsState.Companion.instance
        settings.showGutterIcons = mySettingsComponent?.showGutterIconsStatus != false
    }

    override fun reset() {
        val settings: VentoSettingsState = VentoSettingsState.Companion.instance
        mySettingsComponent?.showGutterIconsStatus = settings.showGutterIcons
    }

    override fun disposeUIResources() {
        mySettingsComponent = null
    }
}
