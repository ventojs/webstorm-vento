/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.testFramework.TestActionEvent
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.js.vento.plugin.file.VentoFileType

class ToggleCommentActionEnablementTest : BasePlatformTestCase() {
    fun testActionIsDisabledInHtmlBlock() {
        myFixture.configureByText(VentoFileType, "<div><caret></div>")
        val action = ToggleCommentAction()
        val event = createEvent(action)

        action.update(event)

        assertFalse("Action should be disabled in HTML block to allow fallback", event.presentation.isEnabled)
    }

    fun testActionIsDisabledInJsBlock() {
        myFixture.configureByText(VentoFileType, "{{> console.log('<caret>') }}")
        val action = ToggleCommentAction()
        val event = createEvent(action)

        action.update(event)

        assertFalse("Action should be disabled in JS block to allow fallback", event.presentation.isEnabled)
    }

    fun testActionIsEnabledOnVentoMarker() {
        myFixture.configureByText(VentoFileType, "{{<caret> if true }}")
        val action = ToggleCommentAction()
        val event = createEvent(action)

        action.update(event)

        assertTrue("Action should be enabled on Vento marker", event.presentation.isEnabled)
    }

    fun testActionIsEnabledInVentoBlock() {
        myFixture.configureByText(VentoFileType, "{{ i<caret>f true }}")
        val action = ToggleCommentAction()
        val event = createEvent(action)

        action.update(event)

        assertTrue("Action should be enabled in Vento block (keyword)", event.presentation.isEnabled)
    }

    private fun createEvent(action: ToggleCommentAction): AnActionEvent =
        TestActionEvent.createTestEvent(action) { dataId ->
            when (dataId) {
                CommonDataKeys.EDITOR.name -> myFixture.editor
                CommonDataKeys.PROJECT.name -> myFixture.project
                CommonDataKeys.PSI_FILE.name -> myFixture.file.findElementAt(myFixture.caretOffset)?.containingFile ?: myFixture.file
                else -> null
            }
        }
}
