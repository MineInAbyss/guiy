package com.derongan.minecraft.guiy.gui.inputs

import com.derongan.minecraft.guiy.gui.ClickEvent
import com.derongan.minecraft.guiy.gui.Element
import com.derongan.minecraft.guiy.gui.elements.containers.singular.Wrappable
import com.derongan.minecraft.guiy.helpers.toCell
import de.erethon.headlib.HeadLib


/**
 * Wrapper [Element] with a click callback.
 *
 * @param wrapped Element to delegate all clicks to.
 * @param clickAction the action to take when clicked.
 */
open class ToggleElement : Wrappable {
    private val default: Element
    private val toggled: Element

    constructor(default: Element, toggled: Element = default) : super() {
        this.default = default
        this.toggled = toggled
    }

    constructor(name: String, enabledMessage: String = "On", disabledMessage: String = "Off") {
        default = HeadLib.CHECKMARK.toCell("$name: $enabledMessage")
        toggled = HeadLib.RED_X.toCell("$name: $disabledMessage")
    }

    var enabled = false
    override val wrapped get() = if (enabled) toggled else default

    private var onEnable: ClickEvent.() -> (Unit) = {}
    private var onDisable: ClickEvent.() -> (Unit) = {}
    private var onClick: ClickEvent.(toggled: Boolean) -> Unit = {}

    fun onEnable(action: ClickEvent.() -> (Unit)) {
        onEnable = action
    }

    fun onDisable(action: ClickEvent.() -> (Unit)) {
        onDisable = action
    }

    fun onClick(action: ClickEvent.(toggled: Boolean) -> Unit) {
        onClick = action
    }

    override fun onClick(clickEvent: ClickEvent) {
        enabled = !enabled
        if (enabled) onEnable(clickEvent)
        else onDisable(clickEvent)
        onClick(clickEvent, enabled)
    }
}