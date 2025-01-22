/*
 * Copyright (c) 2023 Óscar Otero
 * All rights reserved.
 */

package org.js.vento.webstormvento

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@State(name = "org.js.vento.webstormvento.AppSettingsState", storages = [Storage("WebStormVento.xml")])
class VentoSettingsState : PersistentStateComponent<VentoSettingsState?> {
    var showGutterIcons = true

    override fun getState(): VentoSettingsState? = this

    override fun loadState(state: VentoSettingsState) = XmlSerializerUtil.copyBean(state, this)

    companion object {
        val instance: VentoSettingsState
            get() = ApplicationManager.getApplication().getService(VentoSettingsState::class.java)
    }
}
