package com.derongan.minecraft.guiy.gui.elements.lists

import com.derongan.minecraft.guiy.gui.ClickEvent
import com.derongan.minecraft.guiy.gui.Element
import com.derongan.minecraft.guiy.gui.GuiRenderer
import com.derongan.minecraft.guiy.gui.Size
import com.derongan.minecraft.guiy.gui.elements.containers.Containable
import com.derongan.minecraft.guiy.gui.properties.scroll.ScrollAlignment.*
import com.derongan.minecraft.guiy.gui.properties.scroll.ScrollType.*
import com.derongan.minecraft.guiy.helpers.offset
import com.derongan.minecraft.guiy.helpers.toCell
import com.derongan.minecraft.guiy.kotlin_dsl.button
import com.derongan.minecraft.guiy.kotlin_dsl.guiyLayout
import de.erethon.headlib.HeadLib
import org.bukkit.inventory.ItemStack
import kotlin.math.ceil
import kotlin.properties.Delegates

/**
 * An element which holds a mutable list of items that fit into a rectangle of size [width] and [height]. Can hold any
 * type of element, but has a [special rule][convertBy] to convert items added to the list into elements that show up in
 * the GUI.
 *
 * @param list A list of items that this should initially be [converted][convertBy]. Will not be mutated.
 *
 * @property width The width of the element
 * @property height The height of the element
 * @property elements A list of elements that will be rendered. This list is not necessarily immutable, but after a
 * render has begun, it will be cached as [visibleOnPage].
 * @property innerList Logic for creating a child list of the same type, which will be used when [scrollBarAlignment] isn't
 * [INLINE]
 * @property prevButtonName The name of the button that takes you backwards one page
 * @property nextButtonName The name of the button that takes you forwards one page
 * @property scrollBarAlignment Where the scrollbar should be aligned
 * @property scrollType [HORIZONTAL] and [VERTICAL] will change the arrow icons, while [NONE] will show no arrows and
 * ignore the [scrollBarAlignment]
 * @property startPadding How many spaces to leave empty from the beginning (top left). Must be greater than zero.
 * @property endPadding How many spaces to leave empty at the end (bottom right). Must be greater than zero.
 * @property visibleOnPage The [elements] that were drawn on the current [page] when [draw] was called.
 * @property page The current page this list is on.
 * @property pageSize The amount of pages needed to fit all [elements] in this list.
 * @property innerLayout A layout that rendering buttons and inner elements will be delegated to.
 * Unused if [scrollBarAlignment] is [INLINE].
 */
abstract class ListContainable<T>(
        list: MutableList<T> = mutableListOf()
) : Element, Containable, MutableList<T> by list {
    abstract val width: Int //TODO this might be better to just put into element
    abstract val height: Int
    override val dims: Size get() = Size(width, height)
    abstract val elements: List<Element?>

    //TODO the inner list probably shouldn't ever be more than just a static list of what's visible on the page. We can
    // delegate the rendering to it, and dynamically update the items viewable in it to be visibleOnPage, then
    // subclasses don't need to re-implement this and it fixes the issue of inconsistencies (i.e. wrong page number in
    // parent vs child)
    abstract val innerList: Containable.(width: Int, height: Int) -> ListContainable<T>

    //TODO add a page count option
    var prevButtonName = "Previous"
    var nextButtonName = "Next"

    private fun updatePadding() {
        if (scrollType != NONE && scrollBarAlignment == INLINE) {
            startPadding = 1
            endPadding = 1
        } else {
            startPadding = 0
            endPadding = 0
        }
    }

    //TODO move into interface
    var scrollBarAlignment = INLINE
        set(value) {
            field = value
            updatePadding()
        }

    //TODO move into interface
    var scrollType = NONE
        set(value) {
            field = value
            updatePadding()
        }

    //TODO is padding the right term here?
    private var startPadding by Delegates.vetoable(0) { _, _, newValue -> newValue > 0 }
    private var endPadding by Delegates.vetoable(0) { _, _, newValue -> newValue > 0 }

    private var visibleOnPage: List<Element?> = listOf()

    private var page = 0
    private val pageSize get() = width * height - startPadding - endPadding
    private val pages get() = ceil(elements.size.toDouble() / pageSize).toInt()
    private val innerLayout by lazy {
        guiyLayout {
            //TODO make this update when changing scroll type
            val (firstArrow, secondArrow) =
                    if (this@ListContainable.scrollType == HORIZONTAL) HeadLib.WOODEN_ARROW_LEFT to HeadLib.WOODEN_ARROW_RIGHT
                    else HeadLib.WOODEN_ARROW_UP to HeadLib.WOODEN_ARROW_DOWN
            var editOnClick = this@ListContainable
            fun Int.wrapPage(): Int = if (this < 0) editOnClick.pages - 1 else this % editOnClick.pages

            if (this@ListContainable.scrollBarAlignment != INLINE) {
                val (innerX, innerY, width, height) = with(this@ListContainable) {
                    when (scrollBarAlignment) {
                        LEFT -> listOf(1, 0, width - 1, height)
                        RIGHT -> listOf(0, 0, width - 1, height)
                        TOP -> listOf(0, 1, width, height - 1)
                        BOTTOM -> listOf(0, 0, width, height - 1)
                        else -> error("Cannot set alignment for inline list")
                    }
                }
                editOnClick = this@ListContainable.innerList(this, width, height).at(innerX, innerY)
            }

            val (posX, posY, posX2, posY2) = with(this@ListContainable) {
                when (scrollBarAlignment) {
                    LEFT -> listOf(0, 0, 0, height - 1)
                    RIGHT -> listOf(width - 1, 0, width - 1, height - 1)
                    TOP -> listOf(0, 0, width - 1, 0)
                    BOTTOM -> listOf(0, height - 1, width - 1, height - 1)
                    INLINE -> listOf(0, 0, width - 1, height - 1)
                }
            }

            button(firstArrow.toCell(this@ListContainable.prevButtonName)) {
                with(editOnClick) {
                    page = (page - 1).wrapPage()
                }
            }.at(posX, posY)

            button(secondArrow.toCell(this@ListContainable.nextButtonName)) {
                with(editOnClick) {
                    page = (page + 1).wrapPage()
                }
            }.at(posX2, posY2)

            this@ListContainable.scrollBarAlignment = LEFT
        }
    }

    override fun draw(guiRenderer: GuiRenderer) {
        if (scrollType != NONE)
            innerLayout.draw(guiRenderer)
        if (scrollBarAlignment == INLINE) {
            visibleOnPage = elements.drop(page * pageSize).take(pageSize)
            for (x in 0 until width)
                for (y in 0 until height) {
                    getElementAt(x, y)
                            ?.draw(GuiRenderer { x1: Int, y1: Int, mat: ItemStack? ->
                                guiRenderer.renderAt(x1 + x, y1 + y, mat)
                            })
                }
        }
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

    /**
     * Attempts to get an element at x and y coordinates based on what's [currently visible on the page][visibleOnPage].
     */
    fun getElementAt(x: Int, y: Int): Element? {
        var index = gridToIndex(x, y)
        if (scrollType == HORIZONTAL)
            index -= startPadding
        if (index > pageSize) return null
        return visibleOnPage.getOrNull(index)
    }

    /**
     * Converts x and y coordinates to their index, starting from zero, when going top to bottom, left to right.
     * This is not indicative of the actual index of an element in the [elements] list, e.x. the previous page button
     * at (0, 0) will return 0.
     */
    private fun gridToIndex(x: Int, y: Int) = (y * width) + x
}