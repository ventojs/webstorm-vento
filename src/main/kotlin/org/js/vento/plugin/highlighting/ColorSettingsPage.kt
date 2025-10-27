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
                AttributesDescriptor("Bad characters", SyntaxHighlighter.UNKNOWN_CONTENT),
                AttributesDescriptor("Block / Comment", SyntaxHighlighter.CBLOCK),
                AttributesDescriptor("Block / JavaScript", SyntaxHighlighter.JSBLOCK),
                AttributesDescriptor("Block / Variable", SyntaxHighlighter.VBLOCK),
                AttributesDescriptor("Args", SyntaxHighlighter.ARGS),
                AttributesDescriptor("Braces", SyntaxHighlighter.BRACES),
                AttributesDescriptor("Brackets", SyntaxHighlighter.BRACKETS),
                AttributesDescriptor("Commas", SyntaxHighlighter.COMMAS),
                AttributesDescriptor("Comment content", SyntaxHighlighter.COMMENTED_CONTENT),
                AttributesDescriptor("Dots", SyntaxHighlighter.DOTS),
                AttributesDescriptor("Keyword", SyntaxHighlighter.KEYWORDS),
                AttributesDescriptor("Numbers", SyntaxHighlighter.NUMBERS),
                AttributesDescriptor("Operations", SyntaxHighlighter.OPERATIONS),
                AttributesDescriptor("String", SyntaxHighlighter.STRING),
                AttributesDescriptor("Symbol", SyntaxHighlighter.SYMBOLS),
                AttributesDescriptor("Values", SyntaxHighlighter.VALUES),
                AttributesDescriptor("Vento keywords", SyntaxHighlighter.VENTO_KEYWORDS),
            )
    }

    override fun getIcon(): Icon = Vento.ICON

    override fun getHighlighter(): SyntaxHighlighter = SyntaxHighlighter()

    override fun getDemoText(): String =
        """
        {{# This is a sample web page #}}
        {{#- trimmed comment -#}}

        {{ username || "unknown" }}
        {{ "Hello World!" |> toUpperCase }}

        {{ for value of collection |> toUpperCase}}
          {{ value }}
        {{ /for}}
        {{ import { component } from "path/to/file.vto" }}

        {{ export message = "Hello World!" }}

        {{ export message }}
            <h1>Hello World!</h1>
        {{ /export }}

        {{ export function sayHello(name) }}
            <h1>Hello {{ name }}!</h1>
        {{ /export }}

        {{ set message = "Hello World!" }}

        {{ layout "section.vto" {department: "Marketing", active: true} }}
          {{ slot header |> toUpperCase }}
            <h1>Section title</h1>
          {{ /slot }}
          <p>Content of the section</p>
        {{ /layout }}


        {{ include "/my-file.vto" { salutation: "Good bye", sizes: [10, 20, 30] } |> toUpperCase }}

        {{> console.log('Hello World') }}

        {{ bad characters }}
        """.trimIndent()

    override fun getAdditionalHighlightingTagToDescriptorMap(): MutableMap<String, TextAttributesKey>? = null

    override fun getAttributeDescriptors(): Array<AttributesDescriptor> = Util.DESCRIPTORS

    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY

    override fun getDisplayName(): String = "Vento"
}
