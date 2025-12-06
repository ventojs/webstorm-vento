/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */

package org.js.vento.plugin.file

import com.intellij.lang.Language
import com.intellij.lang.LanguageParserDefinitions
import com.intellij.lang.ParserDefinition
import com.intellij.openapi.util.text.StringUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.LanguageSubstitutors
import com.intellij.psi.MultiplePsiFilesPerDocumentFileViewProvider
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.impl.source.PsiFileImpl
import com.intellij.psi.templateLanguages.ConfigurableTemplateLanguageFileViewProvider
import com.intellij.psi.templateLanguages.TemplateDataElementType
import com.intellij.psi.templateLanguages.TemplateDataLanguageMappings
import com.intellij.psi.templateLanguages.TemplateDataModifications
import com.intellij.psi.tree.IElementType
import org.js.vento.plugin.lexer.LexerTokens.HTML
import org.js.vento.plugin.lexer.LexerTokens.VENTO_OUTER
import java.util.Set
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class FileViewProvider : MultiplePsiFilesPerDocumentFileViewProvider, ConfigurableTemplateLanguageFileViewProvider {
    private val myBaseLanguage: Language
    private val myTemplateLanguage: Language

    @JvmOverloads
    constructor(
        manager: PsiManager,
        file: VirtualFile,
        physical: Boolean,
        myBaseLanguage: Language,
        myTemplateLanguage: Language = getTemplateDataLanguage(manager, file),
    ) : super(manager, file, physical) {
        this.myBaseLanguage = myBaseLanguage
        this.myTemplateLanguage = myTemplateLanguage
    }

    override fun supportsIncrementalReparse(rootLanguage: Language): Boolean = false

    override fun getBaseLanguage(): Language = myBaseLanguage

    override fun getTemplateDataLanguage(): Language = myTemplateLanguage

    override fun getLanguages(): MutableSet<Language?> = Set.of<Language?>(myBaseLanguage, getTemplateDataLanguage())

    override fun cloneInner(virtualFile: VirtualFile): MultiplePsiFilesPerDocumentFileViewProvider =
        FileViewProvider(
            getManager(),
            virtualFile,
            false,
            myBaseLanguage,
            myTemplateLanguage,
        )

    override fun createFile(lang: Language): PsiFile? {
        val parserDefinition = getDefinition(lang)
        if (parserDefinition == null) {
            return null
        }

        if (lang.`is`(getTemplateDataLanguage())) {
            val file = parserDefinition.createFile(this)
            val type = getContentElementType(lang)
            if (type != null) {
                (file as PsiFileImpl).setContentElementType(type)
            }
            return file
        } else if (lang.isKindOf(getBaseLanguage())) {
            return parserDefinition.createFile(this)
        }
        return null
    }

    override fun getContentElementType(language: Language): IElementType? {
        if (language.`is`(getTemplateDataLanguage())) {
            return getTemplateDataElementType(getBaseLanguage())
        }
        return null
    }

    private fun getDefinition(lang: Language): ParserDefinition? {
        val parserDefinition: ParserDefinition?
        if (lang.isKindOf(getBaseLanguage())) {
            parserDefinition =
                LanguageParserDefinitions.INSTANCE.forLanguage(if (lang.`is`(getBaseLanguage())) lang else getBaseLanguage())
        } else {
            parserDefinition = LanguageParserDefinitions.INSTANCE.forLanguage(lang)
        }
        return parserDefinition
    }

    companion object {
        private val TEMPLATE_DATA_TO_LANG: ConcurrentMap<String?, TemplateDataElementType?> =
            ConcurrentHashMap<String, TemplateDataElementType>()

        private fun getTemplateDataElementType(lang: Language): TemplateDataElementType {
            val result = TEMPLATE_DATA_TO_LANG.get(lang.getID())

            if (result != null) return result
            val created: TemplateDataElementType =
                object : TemplateDataElementType("VENTO_TEMPLATE_DATA", lang, HTML, VENTO_OUTER) {
                    override fun appendCurrentTemplateToken(
                        tokenEndOffset: Int,
                        tokenText: CharSequence,
                    ): TemplateDataModifications {
                        if (StringUtil.endsWithChar(tokenText, '=')) {
                            // insert fake ="" for attributes inside html tags
                            return TemplateDataModifications.fromRangeToRemove(tokenEndOffset, "\"\"")
                        }
                        return super.appendCurrentTemplateToken(tokenEndOffset, tokenText)
                    }
                }
            val prevValue = TEMPLATE_DATA_TO_LANG.putIfAbsent(lang.id, created)

            return if (prevValue == null) created else prevValue
        }

        private fun getTemplateDataLanguage(manager: PsiManager, file: VirtualFile): Language {
            val mappings = TemplateDataLanguageMappings.getInstance(manager.project)
            var dataLang = mappings?.getMapping(file)
            if (dataLang == null) {
                dataLang = guessTemplateLanguage(file)
            }

            val substituteLang =
                LanguageSubstitutors.getInstance().substituteLanguage(dataLang, file, manager.project)

            // only use a substituted language if it's templateable
            if (TemplateDataLanguageMappings.getTemplateableLanguages().contains(substituteLang)) {
                dataLang = substituteLang
            }
            return dataLang
        }
    }
}
