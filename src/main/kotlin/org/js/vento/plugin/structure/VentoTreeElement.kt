// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package org.js.vento.plugin.structure

import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.structureView.impl.common.PsiTreeElementBase
import com.intellij.util.ReflectionUtil
import org.js.vento.plugin.BaseElementImpl
import javax.swing.Icon

internal class VentoTreeElement private constructor(psiElement: VentoPsiFile) :
    PsiTreeElementBase<VentoPsiFile?>(psiElement) {
        private val myElement: VentoPsiFile = psiElement

        override fun getChildrenBase(): MutableCollection<StructureViewTreeElement?> =
            getStructureViewTreeElements(myElement)

        override fun getPresentableText(): String = myElement.getName()

        override fun getIcon(open: Boolean): Icon? = myElement.getIcon(0)

        companion object {
            fun getStructureViewTreeElements(psiElement: VentoPsiFile): MutableList<StructureViewTreeElement?> {
                val children: MutableList<StructureViewTreeElement?> = ArrayList()
                for (childElement in psiElement.children) {
                    if (childElement !is BaseElementImpl) {
                        continue
                    }

                    if (childElement is BaseElementImpl) {
                        // Vento Statments elements transparently wrap other elements, so we don't add
                        // this element to the tree, but we add its children
                        children.addAll(VentoTreeElement(childElement as VentoPsiFile).childrenBase)
                    }

                    for (suitableClass in StructureViewModel.ourSuitableClasses) {
                        if (ReflectionUtil.isAssignable(suitableClass, childElement.javaClass)) {
                            children.add(VentoTreeElement(childElement as VentoPsiFile))
                            break
                        }
                    }
                }
                return children
            }
        }
    }
