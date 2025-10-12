/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.build

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.File

class PrepareFlexFilesTaskTest {
    private fun readCleanedFlex(): String {
        val file = File("build/tmp/jflex-cleaned/VentoLexer.flex")
        assertTrue(file.exists(), "Expected cleaned VentoLexer.flex to exist at ${file.path}. Make sure prepareFlexFiles ran before tests.")
        return file.readText()
    }

    private fun betweenPercents(content: String): String {
        val parts = content.split("%%")
        assertTrue(parts.size >= 3, "Unexpected JFlex structure: expected at least three sections separated by '%%'")
        return parts[1]
    }

    @Test
    fun `no duplicate macro names or states in cleaned VentoLexer flex`() {
        val content = readCleanedFlex()
        val middle = betweenPercents(content)

        val macroRegex = Regex("^([A-Z_][A-Z0-9_]*)\\s*=.*$")
        val stateRegex = Regex("^%state\\s+(.+)$")

        val macroNames = mutableListOf<String>()
        val stateNames = mutableListOf<String>()

        middle.lines().map { it.trim() }.forEach { line ->
            if (line.isEmpty() || line.startsWith("//")) return@forEach
            val m = macroRegex.matchEntire(line)
            if (m != null) {
                macroNames.add(m.groupValues[1])
            }
            val s = stateRegex.matchEntire(line)
            if (s != null) {
                stateNames.addAll(
                    s.groupValues[1]
                        .trim()
                        .split(Regex("\\s+"))
                        .filter { it.isNotBlank() },
                )
            }
        }

        val macroDupes =
            macroNames
                .groupingBy { it }
                .eachCount()
                .filter { it.value > 1 }
                .keys
        val stateDupes =
            stateNames
                .groupingBy { it }
                .eachCount()
                .filter { it.value > 1 }
                .keys

        assertTrue(macroDupes.isEmpty(), "Duplicate macro names found in cleaned VentoLexer.flex: $macroDupes")
        assertTrue(stateDupes.isEmpty(), "Duplicate %state names found in cleaned VentoLexer.flex: $stateDupes")

        // Guardrail: commonly duplicated macros should appear at most once
        val mustBeUnique = setOf("EXPORT", "FOR_KEY", "FROM", "IMPORT", "OWS", "PIPE", "IDENT", "REGEX", "STRING")
        val nameCounts = macroNames.groupingBy { it }.eachCount()
        val offenders = mustBeUnique.filter { (nameCounts[it] ?: 0) > 1 }
        assertTrue(offenders.isEmpty(), "Some common macros are duplicated: $offenders")
    }
}
