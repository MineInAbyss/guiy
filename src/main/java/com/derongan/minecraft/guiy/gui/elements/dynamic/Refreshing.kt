package com.derongan.minecraft.guiy.gui.elements.dynamic

import com.derongan.minecraft.guiy.gui.Element
import com.derongan.minecraft.guiy.gui.GuiRenderer
import com.derongan.minecraft.guiy.gui.Size
import com.derongan.minecraft.guiy.gui.layouts.GridLayout
import kotlin.reflect.KProperty

class Refreshing<T : Element>(private val create: GridLayout.() -> T) : GridLayout() {
    private var currentElement: T = create()
    override val dims: Size get() = currentElement.dims

    override fun draw(guiRenderer: GuiRenderer) {
        clear()
        currentElement = create()
        currentElement.draw(guiRenderer)
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = currentElement
}