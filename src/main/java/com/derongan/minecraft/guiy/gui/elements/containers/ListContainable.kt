package com.derongan.minecraft.guiy.gui.elements.containers

import com.derongan.minecraft.guiy.gui.Element

class ListContainable : Containable, MutableList<Element> by mutableListOf() {
    override fun <T : Element> addElement(element: T): T {
        this += element
        return element
    }

    override fun removeElement(element: Element?) {
        element?.let { remove(element) }
    }
}