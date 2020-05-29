package com.derongan.minecraft.guiy.gui.elements.containers.lists

import com.derongan.minecraft.guiy.gui.Element
import com.derongan.minecraft.guiy.gui.elements.containers.Containable
import com.derongan.minecraft.guiy.gui.elements.lists.ListContainable

/**
 * A [MutableList] of elements that handles rendering and pagination. See [ListContainable] for more useful properties.
 *
 * @param list The original list of elements to start with. Changes in the initial list will be reflected in the gui
 * and vice versa.
 */
open class ElementList(
        override val width: Int,
        override val height: Int,
        list: MutableList<Element> = mutableListOf()
) : ListContainable<Element>(list) {
    override val elements = list

    override fun <T : Element> addElement(element: T): T {
        this += element
        return element
    }

    override fun removeElement(element: Element?) {
        element?.let { remove(element) }
    }

    override val innerList: Containable.(Int, Int) -> ListContainable<Element> = { width, height ->
        ElementList(width, height, elements)
    }
}