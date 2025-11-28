// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package org.js.vento.plugin.structure

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import org.js.vento.plugin.VentoLanguage
import org.js.vento.plugin.filetype.VentoFileType

class VentoPsiFile
    @JvmOverloads
    constructor(viewProvider: FileViewProvider, lang: Language = VentoLanguage) :
    PsiFileBase(viewProvider, lang) {
        override fun getFileType(): FileType = VentoFileType

        override fun toString(): String = "VentoFile:" + getName()
    }
