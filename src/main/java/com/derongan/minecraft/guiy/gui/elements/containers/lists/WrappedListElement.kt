package com.derongan.minecraft.guiy.gui.elements.lists

import com.derongan.minecraft.guiy.gui.Element
import com.derongan.minecraft.guiy.gui.elements.containers.Containable
import com.derongan.minecraft.guiy.kotlin_dsl.wrappedList

/**
 * An element which holds a mutable list of items that fit into a rectangle of size [width] and [height]. Can hold any
 * type of element, but has a [special rule][convertBy] to convert items added to the list into elements that show up in
 * the GUI.
 *
 * @param list A list of items that this should initially be [converted][convertBy]. Will not be mutated.
 *
 * @property convertBy Defines how items should be converted into elements that show up in the GUI. Will convert all items in list
 * initially, then convert items using this rule as they are added.
 */
class WrappedListElement<T>(
        override val width: Int,
        override val height: Int,
        private val list: MutableList<T> = mutableListOf()
) : ListContainable<T>(list) {
    private val elementCache: MutableMap<T, Element?> = mutableMapOf()

    var convertBy: WrappedListElement<T>.(T) -> Element? = { null }
    var filterBy: (MutableList<T>.() -> List<T>) = { this }

    override val elements get() = filterBy().map { it.toElement() }

    //TODO mutating list should auto update
    override fun <R : Element> addElement(element: R): R {
        return element
    }

    override fun removeElement(element: Element?) {
        elementCache.remove(elementCache.entries.first { (_, value) -> value == element }.key)
    }

    private fun T.toElement() = elementCache.getOrPut(this, { convertBy(this) })

    override val innerList: Containable.(Int, Int) -> ListContainable<T> = { width, height ->
        wrappedList(width, height, this@WrappedListElement.list) {
            convertBy = (this@WrappedListElement.convertBy)
            filterBy = (this@WrappedListElement.filterBy)
        }
    }
}