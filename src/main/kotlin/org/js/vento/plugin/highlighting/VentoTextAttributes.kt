package org.js.vento.plugin.highlighting

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey

class VentoTextAttributes {
    companion object {
        val VENTO_COMMENT: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "VENTO_COMMENT",
            DefaultLanguageHighlighterColors.DOC_COMMENT_MARKUP
        )
    }
}