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
                AttributesDescriptor("Comment content", VentoSyntaxHighlighter.COMMENTED_CONTENT),
                AttributesDescriptor("Comment block", VentoSyntaxHighlighter.COMMENT),
                AttributesDescriptor("JavaScript block", VentoSyntaxHighlighter.JAVASCRIPT),
                AttributesDescriptor("Variable", VentoSyntaxHighlighter.VARIABLE_ELEMENT),
                AttributesDescriptor("Variable block", VentoSyntaxHighlighter.VARIABLE),
                AttributesDescriptor("Variable symbol", VentoSyntaxHighlighter.VARIABLE_PIPES),
                AttributesDescriptor("Text", VentoSyntaxHighlighter.TEXT),
                AttributesDescriptor("HTML", VentoSyntaxHighlighter.HTML),
            )
    }

    override fun getIcon(): Icon = Vento.ICON

    override fun getHighlighter(): SyntaxHighlighter = VentoSyntaxHighlighter()

    override fun getDemoText(): String =
        """
        <!DOCTYPE html>
        <html>
            <head>
                {{# This is a sample web page #}}
                {{#- trimmed comment -#}}

                {{> console.log('Hello World') }}
            </head>
            <body>
                <h1>My Blog</h1>
                <h2>Hello {{ username || "unknown" }}! </h2>
                <p>There are many cool things to read here</p>
            </body>
        </html>
        """.trimIndent()

    override fun getAdditionalHighlightingTagToDescriptorMap(): MutableMap<String, TextAttributesKey>? = null

    override fun getAttributeDescriptors(): Array<AttributesDescriptor> = Util.DESCRIPTORS

    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY

    override fun getDisplayName(): String = "Vento"
}
