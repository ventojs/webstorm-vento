/*
 * Copyright (c) 2023 Óscar Otero
 * All rights reserved.
 */

package org.js.vento.webstormvento

import com.intellij.openapi.options.Configurable
import org.jetbrains.annotations.NonNls
import javax.swing.JComponent

class VentoSettingsConfigurable : Configurable {
    private var mySettingsComponent: VentoSettingsComponent? = null

    @NonNls
    override fun getDisplayName(): String = "Vento.js"

    override fun getPreferredFocusedComponent(): JComponent? = mySettingsComponent?.preferredFocusedComponent

    override fun createComponent(): JComponent? =
        VentoSettingsComponent().also { mySettingsComponent = it }.panel

    override fun isModified(): Boolean =
        mySettingsComponent?.showGutterIconsStatus != VentoSettingsState.instance.showGutterIcons

    override fun apply() {
        VentoSettingsState.Companion.instance.showGutterIcons = mySettingsComponent?.showGutterIconsStatus != false
    }

    override fun reset() {
        mySettingsComponent?.showGutterIconsStatus = VentoSettingsState.Companion.instance.showGutterIcons
    }

    override fun disposeUIResources() {
        mySettingsComponent = null
    }
}
