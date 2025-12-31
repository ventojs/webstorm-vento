/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.actions

import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.CreateFileFromTemplateDialog
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import org.js.vento.plugin.Vento

/**
 * Action to create a new Vento file from the "New" menu.
 */
class CreateVentoFileAction : CreateFileFromTemplateAction(
    "Vento File",
    "Create a new Vento template file",
    Vento.ICON,
) {
    override fun buildDialog(project: Project, directory: PsiDirectory, builder: CreateFileFromTemplateDialog.Builder) {
        builder
            .setTitle("New Vento File")
            .addKind("Vento file", Vento.ICON, "Vento File")
    }

    override fun getActionName(directory: PsiDirectory?, newName: String, templateName: String?): String = "Create Vento File: $newName"
}
