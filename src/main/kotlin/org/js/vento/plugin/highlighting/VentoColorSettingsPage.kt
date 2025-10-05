/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.highlighting

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import org.js.vento.plugin.Vento
import javax.swing.Icon

/**
 * Represents the color settings page for the Vento language plugin. This class integrates with the IntelliJ
 * IDEA's `ColorSettingsPage` interface to define syntax highlighting and customizable attributes for the Vento language.
 *
 * The class provides configuration options for highlighting different language constructs such as comments,
 * JavaScript blocks, and variables. It defines display attributes and their respective association with Vento syntax elements.
 */
class VentoColorSettingsPage : ColorSettingsPage {
    object Util {
        val DESCRIPTORS: Array<AttributesDescriptor> =
            arrayOf<AttributesDescriptor>(
                AttributesDescriptor("Comment block", VentoSyntaxHighlighter.COMMENT),
                AttributesDescriptor("Comment content", VentoSyntaxHighlighter.COMMENTED_CONTENT),
                AttributesDescriptor("JavaScript block", VentoSyntaxHighlighter.JAVASCRIPT),
                AttributesDescriptor("Variable block", VentoSyntaxHighlighter.BLOCK),
                AttributesDescriptor("Variable", VentoSyntaxHighlighter.VARIABLE),
                AttributesDescriptor("Vento expression", VentoSyntaxHighlighter.VALUES),
                AttributesDescriptor("Vento keyword", VentoSyntaxHighlighter.KEY_WORD),
                AttributesDescriptor("Vento args", VentoSyntaxHighlighter.ARGS),
            )
    }

    override fun getIcon(): Icon = Vento.ICON

    override fun getHighlighter(): SyntaxHighlighter = VentoSyntaxHighlighter()

    override fun getDemoText(): String =
        """
        Comments:
          {{# This is a sample web page #}}
          {{#- trimmed comment -#}}

        Variable Block:
          {{ username || "unknown" }}
          {{ "Hello World!" |> toUpperCase }}

        Vento Blocks:
          {{ for value of collection |> toUpperCase}}

        Vento import:
          {{ import { component } from "path/to/file.vto" }}

        Vento export:
          {{ export message = "Hello World!" }}

          {{ export message }}
          <h1>Hello World!</h1>
          {{ /export }}

          {{ export function sayHello(name) }}
          <h1>Hello {{ name }}!</h1>
          {{ /export }}

        Javascript:
          {{> console.log('Hello World') }}
        """.trimIndent()

    override fun getAdditionalHighlightingTagToDescriptorMap(): MutableMap<String, TextAttributesKey>? = null

    override fun getAttributeDescriptors(): Array<AttributesDescriptor> = Util.DESCRIPTORS

    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY

    override fun getDisplayName(): String = "Vento"
}
