// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package org.js.vento.plugin.structure

import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.structureView.TextEditorBasedStructureViewModel
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile
import org.js.vento.plugin.BaseElementImpl

internal class StructureViewModel(psiFile: PsiFile, editor: Editor?) :
    TextEditorBasedStructureViewModel(editor, psiFile) {
    companion object {
        val ourSuitableClasses = arrayOf(BaseElementImpl::class.java)
    }

    override fun getRoot(): StructureViewTreeElement = VentoTreeElementFile(psiFile as VentoPsiFile)
}
