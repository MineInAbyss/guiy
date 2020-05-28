package com.derongan.minecraft.guiy.gui.elements.containers

import com.derongan.minecraft.guiy.gui.Element
import com.derongan.minecraft.guiy.gui.GridContainable
import com.uchuhimo.collections.MutableBiMap
import com.uchuhimo.collections.mutableBiMapOf

abstract class GridContainableBiMap : GridContainable, MutableBiMap<Pair<Int, Int>, Element> by mutableBiMapOf() {
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
        inverse.remove(element)
    }

    /**
     * Checks if there is an element at a specific position.
     *
     * @param x The x coordinate within this element.
     * @param y The y coordinate within this element.
     * @return true if there is an element at this coordinate.
     */
    fun hasElement(x: Int, y: Int) = contains(x to y)
}