package com.derongan.minecraft.guiy.gui.elements.lists

import com.derongan.minecraft.guiy.gui.Element
import com.derongan.minecraft.guiy.gui.elements.containers.Containable
import com.derongan.minecraft.guiy.kotlin_dsl.wrappedList

/**
 * An element which holds a mutable list of items with useful functionality like pagination from [ListContainable].
 * Can hold any type of element, but has a [special rule][convertBy] to convert items added to the list into elements
 * that show up in the GUI. This is useful when you wish to wrap a list of items of any type, and display them in a GUI,
 * without having to deal with updating both elements from the gui and items in the list. This handles both for you.
 *
 * @param list A list of items that this list will wrap. Changes in either will affect the other.
 *
 * @property convertBy Defines how items should be converted into elements that show up in the GUI.
 * Will convert all items in list initially, then convert items using this rule as they are added. These get stored
 * in a cache and do not get converted ever again after the first time.
 * @property filterBy A filter to be applied on the list every render.
 * No caching done, runs every time [draw] is called.
 * @property elements Takes this list of elements, filters it with [filterBy], applies a transformation to elements
 * via [convertBy], and uses this list for rendering.
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