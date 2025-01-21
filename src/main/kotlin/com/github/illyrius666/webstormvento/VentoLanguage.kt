package com.github.illyrius666.webstormvento

import com.intellij.lang.Language

object VentoLanguage : Language("Vento") {
    private fun readResolve(): Any = VentoLanguage
    val INSTANCE = VentoLanguage
}