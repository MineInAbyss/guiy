package com.derongan.minecraft.guiy.gui.containers

import com.derongan.minecraft.guiy.gui.Element

interface GridContainable : Containable {
    /**
     * Adds an element.
     *
     *
     * Adding an element to the location of an existing element will overwrite the existing element.
     *
     * @param x       X coordinate to add the element at.
     * @param y       Y coordinate to add the element at.
     * @param element The element to add.
     * @return the element added
     */
    fun <T : Element> setElement(x: Int, y: Int, element: T): T

    /**
     * Removes an element.
     *
     * @param x X coordinate to remove the element from.
     * @param y Y coordinate to remove the element from.
     */
    fun removeElement(x: Int, y: Int)

    //TODO not sure if this is a good thing to do, it require opt-in adn generates extra bytecode. We don't need it if
    // all subclasses are Kotlin
    @JvmDefault
    override fun <T : Element> addElement(element: T): T = element

    @JvmDefault
    fun <T : Element> T.at(x: Int, y: Int): T = setElement(x, y, this)

    @JvmDefault
    fun <T : Element> T.moveTo(x: Int, y: Int): T = removeElement(this).let { this.at(x, y) }
}