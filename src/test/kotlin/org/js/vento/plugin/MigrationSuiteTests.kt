/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

import org.js.vento.plugin.lexer.*
import org.js.vento.plugin.parser.CommentParsingTestCase
import org.js.vento.plugin.parser.CornerCaseTestCase
import org.junit.runners.Suite.SuiteClasses
import org.js.vento.plugin.parser.ExportTestCase as ExportParserTestCase
import org.js.vento.plugin.parser.ForTestCase as ForParserTestCase
import org.js.vento.plugin.parser.ImportTestCase as ImportParserTestCase
import org.js.vento.plugin.parser.IncludeTestCase as IncludeParserTestCase
import org.js.vento.plugin.parser.LayoutTestCase as LayoutParserTestCase
import org.js.vento.plugin.parser.SetTestCase as SetParserTestCase
import org.js.vento.plugin.parser.VariableTestCase as VariableParserTestCase

// @RunWith(Suite::class)
@SuiteClasses(
    ArrayVariableTestCase::class,
    CommentParsingTestCase::class,
    CommentTestCase::class,
    CornerCaseTestCase::class,
    ErrorTestCase::class,
    ErrorTestCase::class,
    ExportParserTestCase::class,
    ExportTestCase::class,
    ForParserTestCase::class,
    ForTestCase::class,
    ImportParserTestCase::class,
    ImportTestCase::class,
    IncludeParserTestCase::class,
    IncludeTestCase::class,
    LayoutParserTestCase::class,
    LayoutTestCase::class,
    ObjectVariableTestCase::class,
    SetParserTestCase::class,
    SetTestCase::class,
    VariableParserTestCase::class,
    VariableTestCase::class,
)
class MigrationSuiteTests
