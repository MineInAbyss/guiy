package com.derongan.minecraft.guiy.gui.elements.lists

import com.derongan.minecraft.guiy.gui.*
import com.derongan.minecraft.guiy.gui.elements.lists.ScrollAlignment.*
import com.derongan.minecraft.guiy.gui.elements.lists.ScrollType.HORIZONTAL
import com.derongan.minecraft.guiy.gui.elements.lists.ScrollType.NONE
import com.derongan.minecraft.guiy.helpers.offset
import com.derongan.minecraft.guiy.helpers.toCell
import com.derongan.minecraft.guiy.kotlin_dsl.button
import com.derongan.minecraft.guiy.kotlin_dsl.guiyLayout
import com.derongan.minecraft.guiy.kotlin_dsl.wrappedList
import de.erethon.headlib.HeadLib
import org.bukkit.inventory.ItemStack
import kotlin.math.ceil

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
        private val list: MutableList<T> = mutableListOf(),
        val filter: MutableList<T>.() -> List<T> = { this },
        val convertBy: WrappedListElement<T>.(T) -> Element
) : Element, Containable, MutableList<T> by list {
    private val elements: MutableMap<T, Element> = mutableMapOf()
    override val dims: Size = Size(width, height)

    private fun updatePadding() {
        if (scrollType != NONE && scrollBarAlignment == INLINE) {
            startPadding = 1
            endPadding = 1
        } else {
            startPadding = 0
            endPadding = 0
        }
    }

    var prevButtonName = "Previous"
    var nextButtonName = "Next"

    var scrollBarAlignment = INLINE
        set(value) {
            field = value
            updatePadding()
        }
    var scrollType = NONE
        set(value) {
            field = value
            updatePadding()
        }
    private var startPadding = 0
    private var endPadding = 0

    private var page = 0
    private val pageSize get() = width * height - startPadding - endPadding
    private val pages get() = ceil(filter().size.toDouble() / pageSize).toInt()

    private val innerLayout by lazy {
        guiyLayout {
            //TODO make this update when changing scroll type
            val (firstArrow, secondArrow) =
                    if (this@WrappedListElement.scrollType == HORIZONTAL) HeadLib.WOODEN_ARROW_LEFT to HeadLib.WOODEN_ARROW_RIGHT
                    else HeadLib.WOODEN_ARROW_UP to HeadLib.WOODEN_ARROW_DOWN

            var editOnClick = this@WrappedListElement
            fun Int.wrapPage(): Int = if (this < 0) editOnClick.pages - 1 else this % editOnClick.pages

            if (this@WrappedListElement.scrollBarAlignment != INLINE) {
                val (innerX, innerY, width, height) = with(this@WrappedListElement) {
                    when (scrollBarAlignment) {
                        LEFT -> listOf(1, 0, width - 1, height)
                        RIGHT -> listOf(0, 0, width - 1, height)
                        TOP -> listOf(0, 1, width, height - 1)
                        BOTTOM -> listOf(0, 0, width, height - 1)
                        else -> error("Cannot set alignment for inline list")
                    }
                }
                editOnClick = wrappedList(
                        width,
                        height,
                        this@WrappedListElement.list,
                        this@WrappedListElement.filter,
                        this@WrappedListElement.convertBy
                ).at(innerX, innerY)
            }

            val (posX, posY, posX2, posY2) = with(this@WrappedListElement) {
                when (scrollBarAlignment) {
                    LEFT -> listOf(0, 0, 0, height - 1)
                    RIGHT -> listOf(width - 1, 0, width - 1, height - 1)
                    TOP -> listOf(0, 0, width - 1, 0)
                    BOTTOM -> listOf(0, height - 1, width - 1, height - 1)
                    INLINE -> listOf(0, 0, width - 1, height - 1)
                }
            }

            button(firstArrow.toCell(this@WrappedListElement.prevButtonName)) {
                with(editOnClick) {
                    page = (page - 1).wrapPage()
                }
            }.at(posX, posY)

            button(secondArrow.toCell(this@WrappedListElement.nextButtonName)) {
                with(editOnClick) {
                    page = (page + 1).wrapPage()
                }
            }.at(posX2, posY2)
        }
    }
    private val visibleOnPage get() = filter().drop(page * pageSize).take(pageSize)

    override fun draw(guiRenderer: GuiRenderer) {
        if (scrollType != NONE)
            innerLayout.draw(guiRenderer)
        if (scrollBarAlignment == INLINE)
            for (x in 0 until width)
                for (y in 0 until height) {
                    getElementAt(x, y)
                            ?.draw(GuiRenderer { x1: Int, y1: Int, mat: ItemStack? ->
                                guiRenderer.renderAt(x1 + x, y1 + y, mat)
                            })
                }
    }

    //TODO mutating list should auto update
    override fun <R : Element> addElement(element: R): R {
        return element
    }

    override fun removeElement(element: Element?) {
        elements.remove(elements.entries.first { (_, value) -> value == element }.key)
    }

    /**
     * Calls contained elements onclick at the appropriate location.
     */
    override fun onClick(clickEvent: ClickEvent) {
        val index = gridToIndex(clickEvent.x, clickEvent.y)
        if (scrollType != NONE)
            if (index == 0 || index == width * height - 1 || scrollBarAlignment != INLINE) {
                innerLayout.onClick(clickEvent)
                return
            }
        getElementAt(clickEvent.x, clickEvent.y)?.onClick(clickEvent.offset())
    }

    private fun getElementAt(x: Int, y: Int): Element? {
        var index = gridToIndex(x, y)
        if (scrollType == HORIZONTAL)
            index -= startPadding
        if (index > pageSize) return null
        return visibleOnPage.getOrNull(index)?.toElement()
    }

    private fun gridToIndex(x: Int, y: Int) = (y * width) + x

    private fun T.toElement() = elements.getOrPut(this, { convertBy(this) })
}

enum class ScrollAlignment {
    LEFT, RIGHT, TOP, BOTTOM, INLINE
}

enum class ScrollType {
    NONE, HORIZONTAL, VERTICAL
}