package org.js.vento.plugin.highlighting

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import org.js.vento.plugin.Vento
import javax.swing.Icon

class VentoColorSettingsPage : ColorSettingsPage {
    object Util {
        val DESCRIPTORS: Array<AttributesDescriptor> = arrayOf<AttributesDescriptor>(
            AttributesDescriptor("Comment content", VentoSyntaxHighlighter.COMMENTED_CONTENT),
            AttributesDescriptor("Comment block", VentoSyntaxHighlighter.COMMENT),
            AttributesDescriptor("JavaScript block", VentoSyntaxHighlighter.JAVASCRIPT),
        )
    }

    override fun getIcon(): Icon {
        return Vento.ICON
    }

    override fun getHighlighter(): SyntaxHighlighter {
        return VentoSyntaxHighlighter()
    }

    override fun getDemoText(): String {
        return """
        {{# comment #}}
        {{#- trimmed comment -#}}
        {{ console.log('Hello World') }}
        """.trimIndent()
    }


    override fun getAdditionalHighlightingTagToDescriptorMap(): MutableMap<String, TextAttributesKey>? {
        return null
    }

    override fun getAttributeDescriptors(): Array<AttributesDescriptor> {
        return Util.DESCRIPTORS
    }

    override fun getColorDescriptors(): Array<ColorDescriptor> {
        return ColorDescriptor.EMPTY_ARRAY
    }

    override fun getDisplayName(): String {
        return "Vento"
    }
}
