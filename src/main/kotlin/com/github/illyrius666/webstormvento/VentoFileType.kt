package com.github.illyrius666.webstormvento

import com.intellij.openapi.fileTypes.LanguageFileType
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import javax.swing.Icon

object VentoFileType : LanguageFileType(VentoLanguage.INSTANCE) {
    @NotNull
    override fun getName(): String = "Vento File"

    @NotNull
    override fun getDescription(): String = "Vento template file"

    @NotNull
    override fun getDefaultExtension(): String = "vento"

    @Nullable
    override fun getIcon(): Icon? = VentoIcons.FILE
}