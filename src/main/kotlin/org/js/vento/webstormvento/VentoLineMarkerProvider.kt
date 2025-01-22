package org.js.vento.webstormvento

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.xml.XmlTokenImpl
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlAttribute
import javax.swing.Icon

class VentoLineMarkerProvider : RelatedItemLineMarkerProvider() {
    override fun getIcon(): Icon = Vento.ICON

    override fun collectNavigationMarkers(
        element: PsiElement,
        result: MutableCollection<in RelatedItemLineMarkerInfo<*>?>
    ) {
        if (!VentoSettingsState.Companion.instance.showGutterIcons) return

        if (element is XmlAttribute && element.descriptor is VentoAttributeDescriptor) {

            val token = PsiTreeUtil.getChildOfType(element, XmlTokenImpl::class.java) ?: return

            val builder = NavigationGutterIconBuilder.create(Vento.ICON)
                .setTarget(token)
                .setTooltipText("Vento.js directive")

            result.add(builder.createLineMarkerInfo(token))
        }
    }
}
