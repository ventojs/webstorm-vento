/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin

import com.intellij.openapi.util.IconLoader

/**
 * Centralizes resources and constants for the Vento language plugin.
 *
 * The [Vento] object serves as a repository for shared resources, such as icons,
 * used throughout the Vento plugin. This ensures consistency and easy access
 * to these resources from different parts of the plugin.
 *
 * @constructor Creates an instance of the [Vento] object.
 */
object Vento {
    @JvmField
    val ICON = IconLoader.getIcon("/ventoicon.svg", javaClass)
}
