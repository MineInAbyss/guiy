package com.derongan.minecraft.guiy.gui.layouts

import com.derongan.minecraft.guiy.gui.ClickEvent
import com.derongan.minecraft.guiy.gui.Element
import com.derongan.minecraft.guiy.gui.GuiRenderer
import com.derongan.minecraft.guiy.gui.Size
import org.bukkit.inventory.ItemStack

/**
 * Layouts exist as collections of [Child Elements][children], which are arranged by them in a certain way.
 * These arrangements are decided by [calculateDrawLocations], which creates a grid of positions for each element.
 * This information is used for rendering and passing through click events to children.
 *
 * Currently, these positions must be recalculated every time a request to draw is sent, so it is up to the
 * implementations to optimize and cache what they can.
 */
abstract class Layout : Element {
    abstract val children: List<Element>
    private var drawLocations: Map<Pair<Int, Int>, Element> = mapOf()
    override val dims: Size
        get() = Size(
                drawLocations.maxBy { (coords, element) -> coords.first + element.dims.width }?.value?.dims?.width ?: 0,
                drawLocations.maxBy { (coords, element) -> coords.second + element.dims.height }?.value?.dims?.height
                        ?: 0
        )

    abstract fun calculateDrawLocations(): Map<Pair<Int, Int>, Element>

    fun updateDrawLocations() {
        children.filterIsInstance<Layout>().forEach { it.updateDrawLocations() }
        drawLocations = calculateDrawLocations()
    }

    override fun onClick(clickEvent: ClickEvent) =
            findAffectedElements(clickEvent.x, clickEvent.y).forEach { (coords, child) ->
                child.onClick(ClickEvent.offsetClickEvent(clickEvent, coords.first, coords.second))
            }

    private fun findAffectedElements(x: Int, y: Int): Map<Pair<Int, Int>, Element> =
            drawLocations.filter { (coords, child) ->
                val size: Size = child.dims
                val xMin = coords.first
                val xMax = coords.first + size.width - 1
                val yMin = coords.second
                val yMax = coords.second + size.height - 1
                x in xMin..xMax && y in yMin..yMax
            }

    override fun draw(guiRenderer: GuiRenderer) {
        updateDrawLocations()
        drawLocations.forEach { (coords, element) ->
            element.draw(GuiRenderer { x: Int, y: Int, inv: ItemStack? ->
                guiRenderer.renderAt(x + coords.first, y + coords.second, inv)
            })
        }
    }
}