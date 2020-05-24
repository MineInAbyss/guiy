package com.derongan.minecraft.guiy.gui

import java.util.function.Consumer


/**
 * Wrapper [Element] with a click callback.
 *
 * @param wrapped Element to delegate all clicks to.
 * @param clickAction the action to take when clicked.
 */
open class ClickableElement @JvmOverloads constructor(
        private val wrapped: Element,
        private var clickAction: (ClickEvent) -> (Unit) = { it.isCancelled = true }
) : Element {
    override val dims: Size
        get() = wrapped.dims

    override fun draw(guiRenderer: GuiRenderer) {
        wrapped.draw(guiRenderer)
    }

    override fun onClick(clickEvent: ClickEvent) {
        clickAction(clickEvent)
    }

    /**
     * Sets the onClick action.
     *
     * @param clickAction the action to take when clicked.
     */
    fun setClickAction(clickAction: Consumer<ClickEvent>) {
        this.clickAction = clickAction::accept
    }


    /**
     * A similar function to [setClickAction] but for Kotlin. Calling it setClickAction
     * causes an ambiguous call, but since the pickupAction is expected to be passed
     * on class creation in Kotlin, this isn't a big deal.
     *
     * @param clickAction the action to take when clicked.
     */
    fun setClickActionKt(clickAction: (ClickEvent) -> (Unit)) {
        this.clickAction = clickAction
    }
}