package org.js.vento.webstormvento

import com.intellij.ui.components.JBCheckBox
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel

class VentoSettingsComponent {
    val panel: JPanel

    private val myShowGutterIconsStatus = JBCheckBox("Show Vento gutter icons? ")

    val preferredFocusedComponent: JComponent
        get() = myShowGutterIconsStatus

    var showGutterIconsStatus: Boolean
        get() = myShowGutterIconsStatus.isSelected
        set(newStatus) {
            myShowGutterIconsStatus.isSelected = newStatus
        }

    init {
        panel = FormBuilder.createFormBuilder()
            .addComponent(myShowGutterIconsStatus, 1)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }
}
