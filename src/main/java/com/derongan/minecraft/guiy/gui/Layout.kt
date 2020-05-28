package com.derongan.minecraft.guiy.gui

import com.derongan.minecraft.guiy.gui.elements.containers.GridContainableBiMap
import org.bukkit.inventory.ItemStack

/**
 * An element thats purpose is to contain other elements at specific locations. It forwards any clicks to
 * *any* element that claims to be in an inventory slot, based on its position and size.
 */
//TODO rewrite in kotlin to make use of delegation to a list
open class Layout : Element, GridContainableBiMap() {
    override val dims: Size
        get() = Size(
                maxBy { (coords, element) -> coords.first + element.dims.width }!!.value.dims.width,
                maxBy { (coords, element) -> coords.second + element.dims.height }!!.value.dims.height
        )

    override fun onClick(clickEvent: ClickEvent) =
            findAffectedElements(clickEvent.x, clickEvent.y).forEach { (coords, child) ->
                child.onClick(ClickEvent.offsetClickEvent(clickEvent, coords.first, coords.second))
            }

    private fun findAffectedElements(x: Int, y: Int): Map<kotlin.Pair<Int, Int>, Element> =
            filter { (coords, child) ->
                val size: Size = child.dims
                val xMin = coords.first
                val xMax = coords.first + size.width - 1
                val yMin = coords.second
                val yMax = coords.second + size.height - 1
                x in xMin..xMax && y in yMin..yMax
            }

    override fun draw(guiRenderer: GuiRenderer) =
            forEach { (coords, element) ->
                element.draw(GuiRenderer { x: Int, y: Int, inv: ItemStack? ->
                    guiRenderer.renderAt(x + coords.first, y + coords.second, inv)
                })
            }

    override fun toString() = "Layout(${dims.width} ${dims.height})"
}