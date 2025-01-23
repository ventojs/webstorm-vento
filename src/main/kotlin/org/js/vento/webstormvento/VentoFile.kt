/*
 * Copyright (c) 2023 Óscar Otero
 * All rights reserved.
 */

package org.js.vento.webstormvento


import com.intellij.extapi.psi.PsiFileBase
import com.intellij.psi.FileViewProvider

/**
 * Represents a Vento file in the IDE.
 * This class provides the basic functionality for handling PSI (Program Structure Interface)
 * within WebStorm for Vento files.
 */
class VentoFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, VentoLanguage) {
    override fun getFileType() = VentoFileType
    override fun toString(): String = "Vento File"
}