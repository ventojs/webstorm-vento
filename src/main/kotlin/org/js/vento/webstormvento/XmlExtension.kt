package org.js.vento.webstormvento

import com.intellij.lang.html.HTMLLanguage
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile
import com.intellij.psi.html.HtmlTag
import com.intellij.psi.impl.source.xml.SchemaPrefix
import com.intellij.psi.xml.XmlTag
import com.intellij.xml.HtmlXmlExtension

class XmlExtension : HtmlXmlExtension() {
    override fun isAvailable(file: PsiFile?): Boolean {
        if (file != null) {
            return HTMLLanguage.INSTANCE in file.viewProvider.languages
        }
        return false
    }

    override fun getPrefixDeclaration(context: XmlTag, namespacePrefix: String?): SchemaPrefix? {
        if (null != namespacePrefix && context is HtmlTag && hasVentoPrefix(namespacePrefix)) {
            findAttributeSchema(context, namespacePrefix)
                ?.let { return it }
        }
        return super.getPrefixDeclaration(context, namespacePrefix)
    }

    private fun hasVentoPrefix(namespacePrefix: String): Boolean = AttributeUtil.isXmlPrefix(namespacePrefix)

    private fun findAttributeSchema(context: XmlTag, namespacePrefix: String): SchemaPrefix? {
        return context.attributes
            .find { it.name.startsWith(namespacePrefix) }
            ?.let { SchemaPrefix(it, TextRange.create(0, namespacePrefix.length), "Vento.js") }
    }
}
