/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.actions

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.js.vento.plugin.file.VentoFileType

class ToggleCommentActionTest : BasePlatformTestCase() {
    fun testToggleComment() {
        myFixture.configureByText(VentoFileType, "{{ if true }}<caret>")
        myFixture.performEditorAction("VentoToggleCommentAction")
        myFixture.checkResult("{{# if true #}}")

        myFixture.performEditorAction("VentoToggleCommentAction")
        myFixture.checkResult("{{ if true }}")
    }

    fun testToggleCommentWithSelection() {
        myFixture.configureByText(VentoFileType, "<selection>{{ if true }}</selection>")
        myFixture.performEditorAction("VentoToggleCommentAction")
        myFixture.checkResult("{{# if true #}}")

        myFixture.performEditorAction("VentoToggleCommentAction")
        myFixture.checkResult("{{ if true }}")
    }

    fun testToggleCommentWithTrimmingMarkers() {
        myFixture.configureByText(VentoFileType, "{{- if true -}}<caret>")
        myFixture.performEditorAction("VentoToggleCommentAction")
        myFixture.checkResult("{{#- if true -#}}")

        myFixture.performEditorAction("VentoToggleCommentAction")
        myFixture.checkResult("{{- if true -}}")
    }

    fun testToggleCommentNested() {
        // This tests that if it's already a wrapped block, we don't double-wrap on uncomment
        myFixture.configureByText(VentoFileType, "{{# {{ if true }} #}}<caret>")
        myFixture.performEditorAction("VentoToggleCommentAction")
        myFixture.checkResult("{{ if true }}")
    }
}
