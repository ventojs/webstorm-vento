/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.parser.injectors

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import org.js.vento.plugin.DefaultElement
import org.js.vento.plugin.ForElement
import org.js.vento.plugin.ImportElement
import org.js.vento.plugin.SetElement
import org.js.vento.plugin.lexer.LexerTokens
import org.js.vento.plugin.parser.ParserElements

/**
 * Extracts the real variable names bound by `for`/`set`/`default`/`import` blocks in a Vento
 * file, so [ContextualJavaScriptInjector] can declare them in the shared JS context instead of
 * a generic placeholder list.
 *
 * There is no generated/accessor PSI for these constructs (`PsiElements.kt`'s wrapper classes
 * are plain `ASTWrapperPsiElement`s with no domain API), so extraction works by walking raw
 * child leaf tokens by `IElementType`. Filtering must go through `node.elementType`, never
 * `PsiTreeUtil.findChildrenOfType(file, DefaultElement::class.java)` - `PsiElementFactory`'s
 * `createElement()` only maps a handful of `ParserElements` to their own named PSI class; every
 * unmapped element type (including `IF_ELEMENT`, `ECHO_ELEMENT`, `FRAGMENT_ELEMENT`,
 * `LAYOUT_SLOT_ELEMENT`, and every `*_CLOSE_ELEMENT`) falls through to the same generic
 * `DefaultElement` catch-all, so a `DefaultElement`-typed search would spuriously match nearly
 * every other block type too.
 */
object VentoVariableExtractor {
    fun collectAllVariableNames(file: PsiFile): List<String> {
        val names = mutableListOf<String>()

        PsiTreeUtil.findChildrenOfType(file, ForElement::class.java).forEach {
            // ForElement is also used for FOR_CLOSE_ELEMENT ('/for') - only extract from the
            // opening tag.
            if (it.node.elementType == ParserElements.FOR_ELEMENT) {
                names.addAll(extractForBindings(it))
            }
        }
        PsiTreeUtil.findChildrenOfType(file, SetElement::class.java).forEach {
            names.addAll(extractSetOrDefaultBindings(it))
        }
        PsiTreeUtil.findChildrenOfType(file, DefaultElement::class.java).forEach {
            if (it.node.elementType == ParserElements.DEFAULT_ELEMENT) {
                names.addAll(extractSetOrDefaultBindings(it))
            }
        }
        PsiTreeUtil.findChildrenOfType(file, ImportElement::class.java).forEach {
            names.addAll(extractImportBindings(it))
        }

        return names.distinct()
    }

    /**
     * `parseFor()` (`parse-for.kt`) consumes the value/key binding(s) as raw, unmarked child
     * tokens/subtrees up to the `FOR_OF` leaf - a plain `SYMBOL`, a `key, value` pair, or an
     * array-destructured `[a, b]` form nested through `ARRAY_ELEMENT`/`EXPRESSION_ELEMENT`
     * (arbitrarily deep for `[[n]]`-style nesting), but always still bottoming out in raw
     * `SYMBOL` leaves. Everything after `FOR_OF` is the collection expression, safely excluded
     * since `parseJavaScriptExpression` always wraps it in its own child element.
     */
    fun extractForBindings(forElement: PsiElement): List<String> {
        val names = mutableListOf<String>()
        for (child in forElement.node.getChildren(null)) {
            if (child.elementType == LexerTokens.FOR_OF) break
            collectSymbolLeaves(child, names)
        }
        return names
    }

    /**
     * `set`/`default` share one element type for both their inline self-closing form
     * (`{{ set x = 1 }}`) and their block form (`{{ set x }}`) - both `parsSet`/`parseDefault`
     * call `optional(builder, EQUAL, ...)` unconditionally right after the variable name, so an
     * `EQUAL` leaf (or, for the arrow-function-with-braces-body case, a `FUNCTION_BODY_ELEMENT`
     * child) marks the start of the RHS. Collect every `SYMBOL`/`DESTRUCTURE_KEY` leaf before
     * that boundary - including property-name tokens in a `{ a: b }` rename that aren't
     * actually the bound name - rather than replicating the colon-rename/`...expand` logic
     * precisely; harmless here since extra unused `var`s don't break the synthetic scope.
     */
    fun extractSetOrDefaultBindings(element: PsiElement): List<String> {
        val names = mutableListOf<String>()
        for (child in element.node.getChildren(null)) {
            if (child.elementType == LexerTokens.EQUAL ||
                child.elementType == ParserElements.JAVASCRIPT_EXPRESSION_ELEMENT ||
                child.elementType == ParserElements.FUNCTION_ELEMENT ||
                child.elementType == ParserElements.FUNCTION_BODY_ELEMENT
            ) {
                break
            }
            if (child.elementType == LexerTokens.SYMBOL || child.elementType == LexerTokens.DESTRUCTURE_KEY) {
                names.add(child.text.trim())
            }
        }
        return names
    }

    /**
     * `IMPORT_VALUES` (`parse-import.kt`) is consumed via `expect(..., expectMultipleTokens =
     * true)`, so it can be one or more consecutive leaf tokens - e.g. a single `{ a, b }` match,
     * or separate `{`/`a`/`,`/`b`/`}`-shaped pieces depending on how `keywords-import.flex`
     * happened to tokenize it. Concatenate them in document order first so either shape yields
     * the same raw text, then strip the braces, split on `,`, and for each entry take the text
     * after `" as "` (the aliased local name) when present, else the whole entry.
     */
    fun extractImportBindings(importElement: PsiElement): List<String> {
        val raw =
            importElement.node
                .getChildren(null)
                .filter { it.elementType == LexerTokens.IMPORT_VALUES }
                .joinToString("") { it.text }

        return raw
            .trim()
            .removePrefix("{")
            .removeSuffix("}")
            .split(",")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .map { entry ->
                val asIndex = entry.indexOf(" as ")
                if (asIndex >= 0) entry.substring(asIndex + 4).trim() else entry
            }
    }

    private fun collectSymbolLeaves(node: ASTNode, out: MutableList<String>) {
        if (node.elementType == LexerTokens.SYMBOL) {
            out.add(node.text.trim())
            return
        }
        for (child in node.getChildren(null)) {
            collectSymbolLeaves(child, out)
        }
    }
}
