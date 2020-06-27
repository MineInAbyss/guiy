package com.derongan.minecraft.guiy.gui.elements.containers.grids

import com.derongan.minecraft.guiy.gui.Element
import com.derongan.minecraft.guiy.gui.Size
import com.derongan.minecraft.guiy.gui.Size.Companion.create
import com.derongan.minecraft.guiy.gui.layouts.GridLayout

/**
 * Element that supports having Elements added to it dynamically.
 */
open class FillableElement(private val height: Int, private val width: Int) : GridLayout() {
    override val dims: Size get() = create(width, height)

    /**
     * Adds an element at the next available position.
     *
     * @return the element added
     */
    override fun <T : Element> addElement(element: T): T {
        for (y in 0 until height)
            for (x in 0 until width)
                if (!hasElement(x, y))
                    return element.at(x, y)
        return element
    }
}