/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.structure

import com.intellij.ide.structureView.StructureViewBuilder
import com.intellij.ide.structureView.impl.TemplateLanguageStructureViewBuilder
import com.intellij.lang.PsiStructureViewFactory
import com.intellij.psi.PsiFile
import com.intellij.util.PairFunction

class VentoStructureViewFactory : PsiStructureViewFactory {
    override fun getStructureViewBuilder(psiFile: PsiFile): StructureViewBuilder =
        TemplateLanguageStructureViewBuilder
            .create(
                psiFile,
                PairFunction { file, editor ->
                    VentoStructureViewModel(file, editor)
                },
            )
}
