package com.derongan.minecraft.guiy.gui.elements.containers.singular

import com.derongan.minecraft.guiy.gui.ClickEvent
import com.derongan.minecraft.guiy.gui.Element
import com.derongan.minecraft.guiy.gui.GuiRenderer
import com.derongan.minecraft.guiy.gui.Size

abstract class Wrappable : Element {
    protected abstract val wrapped: Element

    override val dims: Size get() = wrapped.dims

    override fun draw(guiRenderer: GuiRenderer) = wrapped.draw(guiRenderer)

    override fun onClick(clickEvent: ClickEvent) = wrapped.onClick(clickEvent)
}