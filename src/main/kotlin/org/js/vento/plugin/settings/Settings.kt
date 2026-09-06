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
import com.intellij.util.messages.Topic
import com.intellij.util.xmlb.XmlSerializerUtil

/**
 * Service that holds a persistent state for Vento plugin settings.
 */
@State(
    name = "Settings",
    storages = [Storage("vento.xml")],
)
@Service(Service.Level.PROJECT)
class Settings(private val project: Project) : PersistentStateComponent<Settings.State> {
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
            val changed = state.enableFrontmatterHighlighting != value
            state.enableFrontmatterHighlighting = value
            if (changed) {
                project.messageBus.syncPublisher(SETTINGS_TOPIC).settingsChanged()
            }
        }

    /**
     * Get whether HTML highlighting is enabled.
     */
    var isHtmlHighlightingEnabled: Boolean
        get() = state.enableHtmlHighlighting
        set(value) {
            val changed = state.enableHtmlHighlighting != value
            state.enableHtmlHighlighting = value
            if (changed) {
                project.messageBus.syncPublisher(SETTINGS_TOPIC).settingsChanged()
            }
        }

    /**
     * State class to hold settings values.
     */
    class State {
        var enableFrontmatterHighlighting: Boolean = true
        var enableHtmlHighlighting: Boolean = true
    }

    /**
     * Listener interface for settings changes.
     */
    interface SettingsListener {
        fun settingsChanged()
    }

    companion object {
        /**
         * Topic for settings change notifications.
         */
        val SETTINGS_TOPIC = Topic.create("Vento Settings Changed", SettingsListener::class.java)

        /**
         * Get the Settings service instance for a project.
         */
        fun getInstance(project: Project): Settings =
            project.getService(Settings::class.java)
    }
}
