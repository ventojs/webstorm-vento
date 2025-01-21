package com.github.illyrius666.webstormvento

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

//FIX: this.
@State(name = "com.github.inxilpro.intellijalpine.AppSettingsState", storages = [Storage("IntellijAlpine.xml")])
class VentoSettingsState : PersistentStateComponent<VentoSettingsState?> {
    var showGutterIcons = true

    override fun getState(): VentoSettingsState? {
        return this
    }

    override fun loadState(state: VentoSettingsState) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        val instance: VentoSettingsState
            get() = ApplicationManager.getApplication().getService(VentoSettingsState::class.java)
    }
}
