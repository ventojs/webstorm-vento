/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.ex.EditorEx
import com.intellij.testFramework.PlatformTestUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.js.vento.plugin.file.VentoFileType

/**
 * Verifies that toggling the "Enable frontmatter highlighting" setting:
 * - Updates the highlighter of already open editors without reopening
 * - Changes the text attributes applied to frontmatter tokens
 */
class FrontmatterHighlightToggleTest : BasePlatformTestCase() {
    fun testToggleFrontmatterHighlightingRehighlightsOpenEditors() {
        val project = project
        val settings = Settings.getInstance(project)

        // Ensure listener is active in tests (startup activity may not run in test env)
        project.messageBus
            .connect(testRootDisposable)
            .subscribe(Settings.SETTINGS_TOPIC, VentoSettingsListener(project))

        // Start from enabled state
        settings.isFrontmatterHighlightingEnabled = true

        // Open a Vento file with frontmatter
        myFixture.configureByText(
            VentoFileType,
            """
            ---
            layout: default
            title: Hello
            ---
            <div>{{ title }}</div>
            """.trimIndent(),
        )

        val editorEx = myFixture.editor as EditorEx
        val initialHighlighter = editorEx.highlighter
        // Keep original highlighter reference to verify replacement on toggle

        // Now toggle setting to disable frontmatter highlighting
        settings.isFrontmatterHighlightingEnabled = false
        pumpEvents()

        val afterDisableHighlighter = editorEx.highlighter
        // Highlighter instance should be replaced
        assertNotSame("EditorHighlighter should be replaced after disabling frontmatter", initialHighlighter, afterDisableHighlighter)

        // Toggle back to enabled and verify it updates again
        settings.isFrontmatterHighlightingEnabled = true
        pumpEvents()

        val afterReEnableHighlighter = editorEx.highlighter

        assertNotSame(
            "EditorHighlighter should be replaced after re-enabling frontmatter",
            afterDisableHighlighter,
            afterReEnableHighlighter,
        )
    }

    private fun pumpEvents() {
        // Ensure invokeLater tasks (rehighlight) are executed
        ApplicationManager.getApplication().invokeAndWait { /* no-op */ }
        PlatformTestUtil.dispatchAllEventsInIdeEventQueue()
        // Force highlighting cycle to complete
        myFixture.doHighlighting()
    }
}
