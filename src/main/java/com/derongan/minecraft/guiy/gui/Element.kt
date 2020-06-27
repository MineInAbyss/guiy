package com.derongan.minecraft.guiy.gui

import com.derongan.minecraft.guiy.kotlin_dsl.GuiyMarker

/**
 * Interface shared by all GUI elements.
 *
 * @property dims The [Size] of the element.
 */
@GuiyMarker
interface Element {
    val dims: Size
//    val constraints: Constraints get() = Constraints()

    /**
     * Draws the element to the provided [GuiRenderer].
     */
    fun draw(guiRenderer: GuiRenderer)

    /**
     * Runs when an element is clicked. This generally includes any slot that the element covers with its Size.
     */
    fun onClick(clickEvent: ClickEvent) {
        clickEvent.isCancelled = true
    }
}