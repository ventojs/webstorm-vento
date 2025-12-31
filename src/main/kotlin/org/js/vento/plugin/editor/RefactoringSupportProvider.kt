/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.editor

import com.intellij.lang.refactoring.RefactoringSupportProvider

/**
 * Provides refactoring support for Vento files, particularly for JavaScript code within script tags.
 *
 * This provider ensures that variable extraction and other refactoring operations work correctly
 * in .vto files, similar to how they work in .html files.
 */
class RefactoringSupportProvider : RefactoringSupportProvider()
