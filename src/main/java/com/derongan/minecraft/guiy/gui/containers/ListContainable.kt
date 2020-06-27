package com.derongan.minecraft.guiy.gui.elements.lists

import com.derongan.minecraft.guiy.gui.ClickEvent
import com.derongan.minecraft.guiy.gui.Element
import com.derongan.minecraft.guiy.gui.GuiRenderer
import com.derongan.minecraft.guiy.gui.Size
import com.derongan.minecraft.guiy.gui.containers.Containable
import com.derongan.minecraft.guiy.gui.elements.containers.grids.FillableElement
import com.derongan.minecraft.guiy.gui.elements.scrolling.ScrollBar
import com.derongan.minecraft.guiy.gui.properties.scroll.ScrollAlignment.INLINE
import com.derongan.minecraft.guiy.gui.properties.scroll.ScrollType.*
import com.derongan.minecraft.guiy.helpers.offset
import org.bukkit.inventory.ItemStack
import kotlin.math.ceil
import kotlin.properties.Delegates.vetoable


/**
 * An abstract class that provides a lot of useful functionality for lists, such as pagination. It delegates the
 * implementation of the [MutableList] to an initial list of items of type T. Implementations will choose how to use
 * this list to generate a list of [elements] that actually get displayed in the gui.
 *
 * @param list The initial list to have [MutableList]'s functions delegated to. Changes in the initial list will be
 * reflected in the gui and vice versa.
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

    //TODO add a page count option

    val scrollBar by lazy {
        ScrollBar(
                width, height,
                previousButtonAction = {
                    with(this@ListContainable) {
                        page = wrapPage(page - 1)
                    }
                },
                nextButtonAction = {
                    with(this@ListContainable) {
                        page = wrapPage(page + 1)
                    }
                }
        )
    }

    //TODO make a system that could calculate this from children + what's defined here
    private fun updatePadding() {
        if (scrollBar.scrollType != NONE && scrollBar.alignment == INLINE) {
            startPadding = 1
            endPadding = 1
        } else {
            startPadding = 0
            endPadding = 0
        }
    }

    //TODO is padding the right term here?
    private var startPadding by vetoable(0) { _, _, newValue -> newValue > 0 }
    private var endPadding by vetoable(0) { _, _, newValue -> newValue > 0 }

    private var visibleOnPage: List<Element?> = listOf()

    private var page = 0
    private val pageSize get() = width * height - startPadding - endPadding
    private val pages get() = ceil(elements.size.toDouble() / pageSize).toInt().coerceAtLeast(1)

    fun wrapPage(page: Int): Int = if (page < 0) pages - 1 else page % pages

    val viewableItems = FillableElement(width, height)

    override fun draw(guiRenderer: GuiRenderer) {
        updatePadding()
        if (scrollBar.scrollType != NONE)
            scrollBar.draw(guiRenderer)
        if (scrollBar.alignment == INLINE) {

            visibleOnPage = elements.drop(page * pageSize).take(pageSize)

            for (x in 0 until width) {
                for (y in 0 until height) {
                    get(x, y)?.draw(GuiRenderer { x1: Int, y1: Int, mat: ItemStack? ->
                        guiRenderer.renderAt(x1 + x, y1 + y, mat)
                    })
                }
            }
        }
    }

    /**
     * Calls contained elements onclick at the appropriate location.
     */
    override fun onClick(clickEvent: ClickEvent) {
        val index = gridToIndex(clickEvent.x, clickEvent.y)
        if (scrollBar.scrollType != NONE)
            if (index == 0 || index == width * height - 1 || scrollBar.alignment != INLINE) {
                scrollBar.onClick(clickEvent)
                return
            }
        this[clickEvent.x, clickEvent.y]?.onClick(clickEvent.offset())
    }

    /**
     * Attempts to get an element at x and y coordinates based on what's [currently visible on the page][visibleOnPage].
     */
    operator fun get(x: Int, y: Int): Element? {
        var index = gridToIndex(x, y)
        if (scrollBar.alignment == INLINE)
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