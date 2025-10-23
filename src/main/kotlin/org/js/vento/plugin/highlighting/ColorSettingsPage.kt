/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.highlighting

import com.intellij.openapi.editor.colors.TextAttributesKey
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
class ColorSettingsPage : ColorSettingsPage {
    object Util {
        val DESCRIPTORS: Array<AttributesDescriptor> =
            arrayOf(
                AttributesDescriptor("Args", SyntaxHighlighter.ARGS),
                AttributesDescriptor("Block / Comment", SyntaxHighlighter.COMMENT),
                AttributesDescriptor("Block / JavaScript", SyntaxHighlighter.JAVASCRIPT),
                AttributesDescriptor("Block / Variable", SyntaxHighlighter.BLOCK),
                AttributesDescriptor("Comment content", SyntaxHighlighter.COMMENTED_CONTENT),
                AttributesDescriptor("Expression", SyntaxHighlighter.VALUES),
                AttributesDescriptor("Keyword", SyntaxHighlighter.KEY_WORD),
                AttributesDescriptor("String", SyntaxHighlighter.STRING),
                AttributesDescriptor("Variable", SyntaxHighlighter.VARIABLE),
            )
    }

    override fun getIcon(): Icon = Vento.ICON

    override fun getHighlighter(): SyntaxHighlighter = SyntaxHighlighter()

    override fun getDemoText(): String =
        """
        comments:
          {{# This is a sample web page #}}
          {{#- trimmed comment -#}}

        variable:
          {{ username || "unknown" }}
          {{ "Hello World!" |> toUpperCase }}

        for:
          {{ for value of collection |> toUpperCase}}

        import:
          {{ import { component } from "path/to/file.vto" }}

        export:
          {{ export message = "Hello World!" }}

          {{ export message }}
          <h1>Hello World!</h1>
          {{ /export }}

          {{ export function sayHello(name) }}
          <h1>Hello {{ name }}!</h1>
          {{ /export }}

        Set:
          {{ set message = "Hello World!" }}

        layout:
          {{ layout "section.vto" {department: "Marketing"} }}
            {{ slot header |> toUpperCase }}
              <h1>Section title</h1>
            {{ /slot }}
            <p>Content of the section</p>
          {{ /layout }}

        include:
            {{ include "/my-file.vto" {salute: "Good bye"} |> toUpperCase }}

        Javascript:
          {{> console.log('Hello World') }}
        """.trimIndent()

    override fun getAdditionalHighlightingTagToDescriptorMap(): MutableMap<String, TextAttributesKey>? = null

    override fun getAttributeDescriptors(): Array<AttributesDescriptor> = Util.DESCRIPTORS

    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY

    override fun getDisplayName(): String = "Vento"
}
