/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.file

import com.intellij.lang.Language
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.FileViewProviderFactory
import com.intellij.psi.PsiManager
import org.js.vento.plugin.VentoLanguage

class ViewProviderFactory : FileViewProviderFactory {
    override fun createFileViewProvider(
        virtualFile: VirtualFile,
        language: Language,
        psiManager: PsiManager,
        eventSystemEnabled: Boolean,
    ): FileViewProvider {
        assert(language.isKindOf(VentoLanguage))
        return FileViewProvider(psiManager, virtualFile, eventSystemEnabled, language)
    }
}
