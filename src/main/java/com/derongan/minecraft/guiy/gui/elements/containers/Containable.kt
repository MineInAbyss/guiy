package com.derongan.minecraft.guiy.gui.elements.containers

import com.derongan.minecraft.guiy.gui.Element
import com.derongan.minecraft.guiy.gui.Layout
import com.derongan.minecraft.guiy.gui.elements.dynamic.Mutating
import com.derongan.minecraft.guiy.gui.elements.dynamic.Refreshing

interface Containable {
    fun <T : Element> addElement(element: T): T
    fun removeElement(element: Element?)

    @JvmDefault
    fun <T : Element> T.dynamic(mutate: T.() -> Unit): Mutating<T> {
        removeElement(this)
        return addElement(Mutating(this, mutate))
    }

    @JvmDefault
    fun <T : Element> refresing(create: Layout.() -> T): Refreshing<T> {
        return addElement(Refreshing(create))
    }

    @JvmDefault
    fun addAll(elements: List<Element>) = elements.forEach { addElement(it) }
}