package com.github.illyrius666.webstormvento

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.psi.FileViewProvider

class VentoFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, VentoLanguage.INSTANCE) {
    override fun getFileType() = VentoFileType

    override fun toString() = "Vento File"
}