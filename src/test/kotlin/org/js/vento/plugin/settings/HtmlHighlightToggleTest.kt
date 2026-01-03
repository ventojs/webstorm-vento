/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.settings

import com.intellij.lang.html.HTMLLanguage
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileTypes.PlainTextLanguage
import com.intellij.testFramework.PlatformTestUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.js.vento.plugin.file.FileViewProvider
import org.js.vento.plugin.file.VentoFileType

/**
 * Verifies that toggling the "Enable HTML highlighting" setting
 * changes the template data language of Vento files.
 */
class HtmlHighlightToggleTest : BasePlatformTestCase() {
    fun testToggleHtmlHighlightingChangesTemplateDataLanguage() {
        val project = project
        val settings = Settings.getInstance(project)

        // Ensure listener is active in tests
        project.messageBus
            .connect(testRootDisposable)
            .subscribe(Settings.SETTINGS_TOPIC, VentoSettingsListener(project))

        // Start from enabled state
        settings.isHtmlHighlightingEnabled = true

        // Open a Vento file
        myFixture.configureByText(
            VentoFileType,
            "<div>{{ title }}</div>"
        )

        val psiFile = myFixture.file
        val viewProvider = psiFile.viewProvider
        assertTrue("ViewProvider should be Vento FileViewProvider", viewProvider is FileViewProvider)
        val fileViewProvider = viewProvider as FileViewProvider
        
        assertEquals("Default template data language should be HTML", HTMLLanguage.INSTANCE, fileViewProvider.templateDataLanguage)

        // Now toggle setting to disable HTML highlighting
        settings.isHtmlHighlightingEnabled = false
        pumpEvents()

        // After reparse, we should have a new FileViewProvider or it should be updated
        val newPsiFile = myFixture.file
        val newViewProvider = newPsiFile.viewProvider
        assertTrue("New ViewProvider should be Vento FileViewProvider", newViewProvider is FileViewProvider)
        val newFileViewProvider = newViewProvider as FileViewProvider
        
        assertEquals("Template data language should be Plain Text after disabling HTML highlighting", PlainTextLanguage.INSTANCE, newFileViewProvider.templateDataLanguage)

        // Toggle back to enabled
        settings.isHtmlHighlightingEnabled = true
        pumpEvents()

        val reEnabledPsiFile = myFixture.file
        val reEnabledViewProvider = reEnabledPsiFile.viewProvider
        val reEnabledFileViewProvider = reEnabledViewProvider as FileViewProvider
        
        assertEquals("Template data language should be HTML again after re-enabling", HTMLLanguage.INSTANCE, reEnabledFileViewProvider.templateDataLanguage)
    }

    fun testAssociatedFileTypeRespectsSettings() {
        val project = project
        val settings = Settings.getInstance(project)
        val virtualFile = myFixture.configureByText("test.vto", "<div></div>").virtualFile

        // Case 1: Enabled (default)
        settings.isHtmlHighlightingEnabled = true
        val associatedFileType = VentoFileType.getAssociatedFileType(virtualFile, project)
        assertEquals("com.intellij.ide.highlighter.HtmlFileType", associatedFileType?.javaClass?.name)

        // Case 2: Disabled
        settings.isHtmlHighlightingEnabled = false
        val associatedFileTypeDisabled = VentoFileType.getAssociatedFileType(virtualFile, project)
        assertEquals("com.intellij.openapi.fileTypes.PlainTextFileType", associatedFileTypeDisabled?.javaClass?.name)
    }

    fun testRespectsExplicitMappingsEvenIfHtmlDisabled() {
        val project = project
        val settings = Settings.getInstance(project)
        val virtualFile = myFixture.configureByText("test.vto", "console.log('hello')").virtualFile

        // Set explicit mapping to JavaScript
        val jsLanguage = com.intellij.lang.Language.findLanguageByID("JavaScript")
        if (jsLanguage != null) {
            com.intellij.psi.templateLanguages.TemplateDataLanguageMappings.getInstance(project).setMapping(virtualFile, jsLanguage)
            
            settings.isHtmlHighlightingEnabled = false
            pumpEvents()

            val psiFile = myFixture.file
            val viewProvider = psiFile.viewProvider as FileViewProvider
            assertEquals("Explicit mapping should be respected even if HTML is disabled", jsLanguage, viewProvider.templateDataLanguage)
        }
    }

    private fun pumpEvents() {
        ApplicationManager.getApplication().invokeAndWait { /* no-op */ }
        PlatformTestUtil.dispatchAllEventsInIdeEventQueue()
        myFixture.doHighlighting()
    }
}
