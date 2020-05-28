package com.derongan.minecraft.guiy.gui.elements.containables

import com.derongan.minecraft.guiy.gui.Element
import com.derongan.minecraft.guiy.gui.GridContainable
import com.uchuhimo.collections.MutableBiMap
import com.uchuhimo.collections.mutableBiMapOf

abstract class GridContainableBiMap : GridContainable, MutableBiMap<Pair<Int, Int>, Element> by mutableBiMapOf() {
    override fun <T : Element> setElement(x: Int, y: Int, element: T): T {
        this[x to y] = element
        return element
    }

    override fun removeElement(x: Int, y: Int) {
        this -= x to y
    }

    override fun removeElement(element: Element?) {
        inverse.remove(element)
    }
}