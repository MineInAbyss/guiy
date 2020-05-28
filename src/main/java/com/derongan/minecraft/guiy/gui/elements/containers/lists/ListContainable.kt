package com.derongan.minecraft.guiy.gui.elements.containers.lists

import com.derongan.minecraft.guiy.gui.Element
import com.derongan.minecraft.guiy.gui.elements.containers.Containable

abstract class ListContainable : Element, Containable, MutableList<Element> by mutableListOf() {
    override fun <T : Element> addElement(element: T): T {
        this += element
        return element
    }

    override fun removeElement(element: Element?) {
        element?.let { remove(element) }
    }
}