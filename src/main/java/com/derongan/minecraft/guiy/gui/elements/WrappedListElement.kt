package com.derongan.minecraft.guiy.gui.elements

import com.derongan.minecraft.guiy.gui.*
import com.derongan.minecraft.guiy.helpers.offset
import com.derongan.minecraft.guiy.helpers.toCell
import com.derongan.minecraft.guiy.kotlin_dsl.button
import com.derongan.minecraft.guiy.kotlin_dsl.guiyLayout
import de.erethon.headlib.HeadLib
import org.bukkit.inventory.ItemStack

/**
 * An element which holds a mutable list of items that fit into a rectangle of size [width] and [height]. Can hold any
 * type of element, but has a [special rule][convertBy] to convert items added to the list into elements that show up in
 * the GUI.
 *
 * @param list A list of items that this should initially be [converted][convertBy]. Will not be mutated.
 * @param convertBy Defines how items should be converted into elements that show up in the GUI. Will convert all items in list
 * initially, then convert items using this rule as they are added.
 */
//TODO would be nice to make this an actual list delegated by `list`, but there's a clash between Element and List's `size`
class WrappedListElement<T>(
        val width: Int,
        val height: Int,
        private val values: MutableList<T> = mutableListOf(),
        val filter: MutableList<T>.() -> List<T> = { this },
        val convertBy: WrappedListElement<T>.(T) -> Element
) : Element, ListContainable, MutableList<T> by values {
    private val elements: MutableMap<T, Element> = mutableMapOf()
    override val dims: Size = Size(width, height)

    var scrollType = ScrollType.NONE
    var origin = 0

    val innerLayout by lazy { //TODO create layout for vertical scroll too
        guiyLayout {
            button(HeadLib.WOODEN_ARROW_LEFT.toCell("Left")) {
                with(this@WrappedListElement) {
                    origin = Math.floorMod(origin + 1, (width - 2).coerceAtLeast(size))
                }
            }.at(0, 0)

            button(HeadLib.WOODEN_ARROW_RIGHT.toCell("Right")) {
                with(this@WrappedListElement) {
                    origin = Math.floorMod(origin - 1, (width - 2).coerceAtLeast(size))
                }
            }.at(this@WrappedListElement.width - 1, this@WrappedListElement.height - 1)
        }
    }

    override fun draw(guiRenderer: GuiRenderer) {
        Cell.forItemStack(HeadLib.WOODEN_ARROW_RIGHT.toItemStack("Right"))
        //TODO offset similar to scrolling pallet
        values.filter().take(width * height).map { it.toElement() }.forEachIndexed { i, element ->
            element.draw(GuiRenderer { x1: Int, y1: Int, mat: ItemStack? -> guiRenderer.renderAt(x1 + i % dims.width, y1 + i / dims.width, mat) })
        }
        if (scrollType == ScrollType.HORIZONTAL)
            innerLayout.draw(guiRenderer)
    }

    //TODO make work for this
    /**
     * Gets the tool currently at the specific x location.
     *
     * @param x The x location of the tool.
     * @return The [Element] containing the tool if there is one at the location, an empty optional otherwise.
     */
    /*private fun getToolAt(x: Int): Element? {
        val locationInTools = Math.floorMod(origin + x, Math.max(width - 2, tools.size))
        return if (locationInTools >= tools.size) {
            Optional.empty()
        } else {
            Optional.of(tools.get(locationInTools))
        }
    }*/

    //TODO mutating list should auto update
    override fun <R : Element> addElement(element: R): R {
        return element
    }

    override fun removeElement(element: Element?) {
        elements.remove(elements.entries.first { (key, value) -> value == element }.key)
    }

    /**
     * Calls contained elements onclick at the appropriate location.
     */
    override fun onClick(clickEvent: ClickEvent) {
        values[gridToIndex(clickEvent.x, clickEvent.y)]?.toElement()?.onClick(clickEvent.offset(clickEvent.x, clickEvent.y))
    }

    private fun gridToIndex(x: Int, y: Int) = (y * width) + x

    private fun T.toElement() = elements.getOrPut(this, { convertBy(this) })
}

enum class ScrollType {
    NONE, HORIZONTAL, VERTICAL
}