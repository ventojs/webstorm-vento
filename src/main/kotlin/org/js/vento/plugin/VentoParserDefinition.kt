/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import org.js.vento.plugin.filetype.VentoFile
import org.js.vento.plugin.lexer.LexerAdapter
import org.js.vento.plugin.lexer.LexerTokens
import org.js.vento.plugin.parser.PareserElementFactory
import org.js.vento.plugin.parser.Parser

/**
 * Defines the parser for Vento files, integrating key components such as Lexer, Parser, and PSI elements.
 * This class is responsible for enabling IntelliJ Platform-based IDEs to understand and process Vento files
 * by providing the necessary infrastructure for tokenization, parsing, and PSI tree construction.
 *
 * Responsibilities:
 * - Provides the lexer implementation to tokenize Vento code.
 * - Supplies the parser for generating the syntax tree from tokenized input.
 * - Defines the file type and high-level PSI structure for Vento files.
 * - Specifies token sets for comments and string literals in Vento syntax.
 * - Maps syntax tree nodes to PSI elements for further IDE analysis and operations.
 *
 * Key methods:
 * - `createLexer`: Returns the lexer adapter for tokenizing Vento files.
 * - `createParser`: Returns the parser responsible for constructing the syntax tree.
 * - `getFileNodeType`: Specifies the file element type for the root of the syntax tree.
 * - `getCommentTokens`: Provides the token types considered as comments in Vento.
 * - `getStringLiteralElements`: Provides the token types considered as string literals in Vento.
 * - `createElement`: Converts AST nodes into specific PSI elements.
 * - `createFile`: Creates the file-level PSI element for Vento files.
 *
 * See also:
 * - `VentoLexerAdapter` for handling tokenization.
 * - `VentoParser` for parsing logic and syntax tree generation.
 * - `PsiFile`, `PsiElement`, `TokenSet` for IntelliJ PSI structure.
 */
class VentoParserDefinition : ParserDefinition {
    override fun createLexer(project: Project?): Lexer = LexerAdapter()

    override fun createParser(project: Project?): PsiParser = Parser()

    override fun getFileNodeType(): IFileElementType = IFileElementType(VentoLanguage)

    override fun getCommentTokens(): TokenSet = TokenSet.create(LexerTokens.COMMENT)

    override fun getStringLiteralElements(): TokenSet = TokenSet.create(LexerTokens.STRING)

    override fun createElement(node: ASTNode): PsiElement = PareserElementFactory.createElement(node)

    override fun createFile(viewProvider: FileViewProvider): PsiFile = VentoFile(viewProvider)
}
