/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.highlighting

import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

/**
 * Factory class for creating an instance of `VentoSyntaxHighlighter`.
 *
 * This class is responsible for providing the syntax highlighter specific to the
 * Vento language in the IntelliJ Platform. It is invoked to supply syntax highlighting
 * functionality based on the associated project and virtual file context.
 *
 * This implementation is part of the IntelliJ syntax highlighting framework and works
 * in tandem with `VentoSyntaxHighlighter` to provide colorized editor support
 * for Vento language files.
 */
class SyntaxHighlighterFactory : SyntaxHighlighterFactory() {
    override fun getSyntaxHighlighter(project: Project?, virtualFile: VirtualFile?): SyntaxHighlighter = SyntaxHighlighter()
}
