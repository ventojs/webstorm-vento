/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser

import com.intellij.testFramework.ParsingTestCase
import org.js.vento.plugin.VentoParserDefinition

/**
 * Tests for parsing Vento import statements.
 */
class ImportTestCase : ParsingTestCase("", "vto", VentoParserDefinition()) {
    // Valid import syntax tests

    /**
     * Tests import with destructured named imports.
     * Example: {{ import { foo } from "./afile.vto" }}
     */
    fun testNamedImport() {
        val code = "{{ import { foo } from \"./afile.vto\" }}"
        doCodeTest(code)
    }

    /**
     * Tests import with multiple destructured named imports.
     * Example: {{ import { foo,bar } from "./afile.vto" }}
     */
    fun testMultipleNamedImport() {
        val code = "{{ import { foo,bar } from \"./afile.vto\" }}"
        doCodeTest(code)
    }

    /**
     * Tests import with default (bare) import.
     * Example: {{ import foo from "./afile.vto" }}
     */
    fun testDefaultImport() {
        val code = "{{ import foo from \"./afile.vto\" }}"
        doCodeTest(code)
    }

    // Invalid import syntax tests

    /**
     * Tests import with missing source path.
     * Example: {{ import foo from }}
     */
    fun testMissingSourcePath() {
        val code = "{{ import foo from }}"
        doCodeTest(code)
    }

    /**
     * Tests import with missing import specifier.
     * Example: {{ import from "./afile.vto" }}
     */
    fun testMissingImportSpecifier() {
        val code = "{{ import from \"./afile.vto\" }}"
        doCodeTest(code)
    }

    /**
     * Tests import with missing 'from' keyword.
     * Example: {{ import foo "./afile.vto" }}
     */
    fun testMissingFromKeyword() {
        val code = "{{ import foo \"./afile.vto\" }}"
        doCodeTest(code)
    }

    override fun getTestDataPath(): String = "src/test/resources/testdata/import"

    override fun includeRanges(): Boolean = true
}
