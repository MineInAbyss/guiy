package com.derongan.minecraft.guiy.gui.elements

import com.derongan.minecraft.guiy.gui.*
import org.bukkit.inventory.ItemStack

/**
 * An element which holds a mutable list of items that fit into a rectangle of size [width] and [height]. Can hold any
 * type of element, but has a [special rule][toElement] to convert items added to the list into elements that show up in
 * the GUI.
 *
 * @param list A list of items that this should initially be [converted][toElement]. Will not be mutated.
 * @param toElement Defines how items should be converted into elements that show up in the GUI. Will convert all items in list
 * initially, then convert items using this rule as they are added.
 */
//TODO would be nice to make this an actual list delegated by `list`, but there's a clash between Element and List's `size`
class ListElement<T>(val width: Int, val height: Int, list: List<T>, val toElement: ListElement<T>.(T) -> Element) : Element, ListContainable {
    private val elements = mutableListOf<Element>()

    init {
        list.forEach { toElement(it) }
    }

    override val size: Size = Size(width, height)
    override fun draw(guiRenderer: GuiRenderer) {
        elements.forEachIndexed { i, element ->
            element.draw(GuiRenderer { x1: Int, y1: Int, mat: ItemStack? -> guiRenderer.renderAt(x1 + i % size.width, y1 + i / size.width, mat) })
        }
    }

    fun add(item: T) {
        toElement(item)
    }

    override fun <R : Element> addElement(element: R): R {
        elements += element
        return element
    }

    override fun removeElement(element: Element?) {
        elements.remove(element)
    }

    /**
     * Calls contained elements onclick at the appropriate location.
     */
    override fun onClick(clickEvent: ClickEvent) {
        elements.getOrNull(gridToIndex(clickEvent.x, clickEvent.y))?.onClick(ClickEvent.offsetClickEvent(clickEvent, clickEvent.x, clickEvent.y))
    }

    private fun gridToIndex(x: Int, y: Int) = (y * width) + x
}