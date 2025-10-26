/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

import org.js.vento.plugin.lexer.ErrorTestCase
import org.js.vento.plugin.lexer.ExportTestCase
import org.js.vento.plugin.lexer.ForTestCase
import org.js.vento.plugin.lexer.ImportTestCase
import org.js.vento.plugin.lexer.SetTestCase
import org.js.vento.plugin.parser.CornerCaseTestCase
import org.junit.runner.RunWith
import org.junit.runners.Suite
import org.junit.runners.Suite.SuiteClasses
import org.js.vento.plugin.parser.ExportTestCase as ExportParserTestCase
import org.js.vento.plugin.parser.ForTestCase as ForParserTestCase
import org.js.vento.plugin.parser.ImportTestCase as ImportParserTestCase
import org.js.vento.plugin.parser.SetTestCase as SetParserTestCase

@RunWith(Suite::class)
@SuiteClasses(
    ErrorTestCase::class,
    ExportTestCase::class,
    ExportParserTestCase::class,
    ErrorTestCase::class,
    ImportTestCase::class,
    ImportParserTestCase::class,
    SetTestCase::class,
    SetParserTestCase::class,
    ForTestCase::class,
    ForParserTestCase::class,
    CornerCaseTestCase::class,
)
class MigrationSuiteTests
