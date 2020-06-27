package com.derongan.minecraft.guiy.gui.layouts

import com.derongan.minecraft.guiy.gui.Element
import com.derongan.minecraft.guiy.gui.containers.Containable

abstract class ListLayout : Layout(), MutableList<Element> by mutableListOf(), Containable {
    override val children: List<Element> get() = this

    override fun <T : Element> addElement(element: T): T {
        this += element
        return element
    }

    override fun removeElement(element: Element?) {
        element?.let { remove(element) }
    }
}