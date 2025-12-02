/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.editor

import com.intellij.codeInsight.lookup.CharFilter
import com.intellij.codeInsight.lookup.Lookup
import org.js.vento.plugin.file.VentoFileType

class VentoCharFilter : CharFilter() {
    override fun acceptChar(c: Char, prefixLength: Int, lookup: Lookup): Result? {
        val file = lookup.psiFile ?: return null
        if (file.fileType != VentoFileType) return null

        // Allow '/' to be typed and continue filtering
        if (c == '/') {
            return Result.ADD_TO_PREFIX
        }

        return null // Let other filters handle it
    }
}
