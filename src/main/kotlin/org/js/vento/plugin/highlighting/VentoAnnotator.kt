package org.js.vento.plugin.highlighting

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.elementType
import org.js.vento.plugin.lexer.VentoLexerTypes

class VentoAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        when (element.elementType) {
            VentoLexerTypes.OPEN_TRIM_COMMENT_CLAUSE, VentoLexerTypes.OPEN_COMMENT_CLAUSE -> {
                if (!hasMatchingEnd(element, arrayOf(VentoLexerTypes.CLOSE_COMMENT_CLAUSE, VentoLexerTypes.CLOSE_TRIM_COMMENT_CLAUSE))) {
                    holder
                        .newAnnotation(HighlightSeverity.ERROR, "Unclosed comment block")
                        .range(element.textRange)
                        .textAttributes(VentoSyntaxHighlighter.SYNTAX_ERROR)
                        .create()
                }
            }
        }
    }

    private fun hasMatchingEnd(startElement: PsiElement, expectedEndTypes: Array<IElementType>): Boolean {
        var depth = 1
        expectedEndTypes.forEach {
            var current = startElement.nextSibling
            while (current != null && depth > 0) {
                when (current.elementType) {
                    startElement.elementType -> depth++
                    it -> depth--
                }
                current = current.nextSibling
            }
            if (depth == 0) return true
        }

        return false
    }
}
