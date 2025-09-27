/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.inspections

import com.intellij.codeInspection.InspectionSuppressor
import com.intellij.codeInspection.SuppressQuickFix
import com.intellij.psi.PsiElement

class VentoInspectionSuppressor : InspectionSuppressor {
    override fun isSuppressedFor(element: PsiElement, toolId: String): Boolean {
        // Suppress unused variable warnings and highlighting for JavaScript in Vento templates
        val suppressedInspections =
            setOf(
                // JavaScript unused symbols
                "JSUnusedLocalSymbols",
                "JSUnusedGlobalSymbols",
                "JSUnusedAssignment",
                "JSUnreachableCode",
                "JSUnresolvedVariable",
                "JSUnresolvedFunction",
                "JSValidateTypes",
                "JSCheckFunctionSignatures",
                // General unused declarations
                "UnusedVariable",
                "UnusedDeclaration",
                "UnusedLabel",
                "UNUSED_SYMBOL",
                // TypeScript unused symbols (in case TS is involved)
                "TypeScriptUnusedLocalSymbol",
                "TypeScriptUnresolvedVariable",
                // Other potential inspection IDs
                "unused",
                "UnusedProperty",
                "UnusedParameter",
            )

        if (toolId in suppressedInspections) {
            val ventoFile = element.containingFile?.name?.endsWith(".vto") == true

            if (ventoFile) {
                return true
            }
        }
        return false
    }

    override fun getSuppressActions(element: PsiElement?, toolId: String): Array<SuppressQuickFix> = emptyArray()
}
