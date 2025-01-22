package org.js.vento.webstormvento

import com.intellij.psi.impl.source.html.dtd.HtmlElementDescriptorImpl
import com.intellij.psi.xml.XmlTag
import com.intellij.xml.XmlAttributeDescriptor
import com.intellij.xml.XmlAttributeDescriptorsProvider

class AttributesProvider : XmlAttributeDescriptorsProvider {
    override fun getAttributeDescriptors(xmlTag: XmlTag): Array<XmlAttributeDescriptor> = emptyArray()

    @Suppress("ReturnCount")
    override fun getAttributeDescriptor(name: String, xmlTag: XmlTag): XmlAttributeDescriptor? {
        if (xmlTag.descriptor !is HtmlElementDescriptorImpl) return null
        val info = AttributeInfo(name)

        if (info.isVento()) return VentoAttributeDescriptor(name, xmlTag)
        return null
    }
}
