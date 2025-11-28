// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package org.js.vento.plugin.structure

import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.structureView.impl.common.PsiTreeElementBase

internal class VentoTreeElementFile(psiFile: VentoPsiFile) : PsiTreeElementBase<VentoPsiFile?>(psiFile) {
    private val myFile: VentoPsiFile = psiFile

    override fun getChildrenBase(): MutableCollection<StructureViewTreeElement> =
        VentoTreeElement.getStructureViewTreeElements(
            myFile,
        ) as MutableCollection<StructureViewTreeElement>

    override fun getPresentableText(): String = myFile.name
}
