package com.derongan.minecraft.guiy.gui.elements.containers.lists

import com.derongan.minecraft.guiy.gui.ClickEvent
import com.derongan.minecraft.guiy.gui.Element
import com.derongan.minecraft.guiy.gui.GuiRenderer
import com.derongan.minecraft.guiy.gui.Layout
import com.derongan.minecraft.guiy.helpers.offset
import com.derongan.minecraft.guiy.helpers.toCell
import com.derongan.minecraft.guiy.kotlin_dsl.guiyLayout
import com.google.common.base.Preconditions
import de.erethon.headlib.HeadLib
import java.util.*

/**
 * A pallet that can hold a number of elements (referred to as tools). It allows cyclic scrolling left and right. It is
 * always one slot high. Tools are normal Elements, and define their own behaviour.
 *
 * @param width width of the scrolling pallet. Must be at least 3 in order to show a tool and the scroll buttons.
 */
class ScrollingPallet(override val width: Int) : ElementList(width, 1) {
    init {
        Preconditions.checkArgument(width >= 3, "Width must be over 2 in order to include controls and tools")
    }

    private val innerLayout: Layout = guiyLayout {
        HeadLib.WOODEN_ARROW_LEFT.toCell("Left").at(0, 0)
        HeadLib.WOODEN_ARROW_RIGHT.toCell("Right").at(this@ScrollingPallet.width - 1, 0)
    }

    private var origin = 0

    override fun draw(guiRenderer: GuiRenderer) {
        innerLayout.draw(guiRenderer)
        for (i in 0 until width - 2)
            getToolAt(i)?.draw(GuiRenderer { x, y, itemStack -> guiRenderer.renderAt(1 + i, 0, itemStack) })
    }

    override fun onClick(clickEvent: ClickEvent) {
        if (clickEvent.x == 0) {
            origin = Math.floorMod(origin + 1, (width - 2).coerceAtLeast(size))
            return
        }
        if (clickEvent.x == width - 1) {
            origin = Math.floorMod(origin - 1, (width - 2).coerceAtLeast(size))
            return
        }
        getToolAt(clickEvent.x - 1)?.onClick(clickEvent.offset())
    }

    /**
     * Gets the tool currently at the specific x location.
     *
     * @param x The x location of the tool.
     * @return [Optional] containing the tool if there is one at the location, an empty optional otherwise.
     */
    private fun getToolAt(x: Int): Element? {
        val locationInTools = Math.floorMod(origin + x, (width - 2).coerceAtLeast(size))
        return getOrNull(locationInTools)
    }
}