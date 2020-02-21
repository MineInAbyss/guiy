package com.derongan.minecraft.guiy.gui

import com.derongan.minecraft.guiy.kotlin_dls.GuiyMarker

/**
 * Interface shared by all GUI elements.
 */
@GuiyMarker
interface Element {
    /**
     * Get the size of the element.
     */
    val size: Size

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