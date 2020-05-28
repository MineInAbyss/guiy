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
        var clickAction: (ClickEvent) -> (Unit) = { it.isCancelled = true }
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
     * Only visible to java.
     *
     * @param clickAction the action to take when clicked.
     */
    @Suppress("NEWER_VERSION_IN_SINCE_KOTLIN")
    @kotlin.SinceKotlin(version = "99999.0") //hack to prevent viewing in Kotlin
    fun setClickAction(clickAction: Consumer<ClickEvent>) {
        this.clickAction = clickAction::accept
    }
}