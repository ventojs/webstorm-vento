/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil

/**
 * Service that holds persistent state for Vento plugin settings.
 */
@State(
    name = "VentoSettings",
    storages = [Storage("vento.xml")],
)
@Service(Service.Level.PROJECT)
class VentoSettings : PersistentStateComponent<VentoSettings.State> {
    private var state = State()

    override fun getState(): State = state

    override fun loadState(state: State) {
        XmlSerializerUtil.copyBean(state, this.state)
    }

    /**
     * Get whether frontmatter highlighting is enabled.
     */
    var isFrontmatterHighlightingEnabled: Boolean
        get() = state.enableFrontmatterHighlighting
        set(value) {
            state.enableFrontmatterHighlighting = value
        }

    /**
     * State class to hold settings values.
     */
    class State {
        var enableFrontmatterHighlighting: Boolean = true
    }

    companion object {
        /**
         * Get the VentoSettings service instance for a project.
         */
        fun getInstance(project: Project): VentoSettings =
            project.getService(VentoSettings::class.java)
    }
}
