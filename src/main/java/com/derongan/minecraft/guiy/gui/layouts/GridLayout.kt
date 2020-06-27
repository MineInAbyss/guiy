package com.derongan.minecraft.guiy.gui.layouts

import com.derongan.minecraft.guiy.gui.Element
import com.derongan.minecraft.guiy.gui.containers.GridContainable
import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap

/**
 * An layout which contains other elements at specific locations. It forwards any clicks to
 * *any* element that claims to be in an inventory slot, based on its position and size.
 */
open class GridLayout : Layout(), BiMap<Pair<Int, Int>, Element> by HashBiMap.create(), GridContainable {
    override val children: List<Element> get() = this.values.toList()
    override fun calculateDrawLocations() = this

    override fun <T : Element> setElement(x: Int, y: Int, element: T): T {
        this[x to y] = element
        return element
    }

    fun getElement(x: Int, y: Int) = this[x to y]

    operator fun get(x: Int, y: Int) {
        getElement(x, y)
    }

    operator fun set(x: Int, y: Int, element: Element) {
        setElement(x, y, element)
    }

    override fun removeElement(x: Int, y: Int) {
        this -= x to y
    }

    override fun removeElement(element: Element?) {
        inverse().remove(element)
    }

    /**
     * Checks if there is an element at a specific position.
     *
     * @param x The x coordinate within this element.
     * @param y The y coordinate within this element.
     * @return true if there is an element at this coordinate.
     */
    fun hasElement(x: Int, y: Int) = contains(x to y)

    override fun toString() = "Layout(${dims.width} ${dims.height})"
}