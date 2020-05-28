package com.derongan.minecraft.guiy.gui.elements.dynamic

import com.derongan.minecraft.guiy.gui.Element
import com.derongan.minecraft.guiy.gui.GuiRenderer
import com.derongan.minecraft.guiy.gui.Layout
import com.derongan.minecraft.guiy.gui.Size
import kotlin.reflect.KProperty

class Refreshing<T : Element>(private val create: Layout.() -> T) : Layout() {
    private var currentElement: T = create()
    override val dims: Size get() = currentElement.dims

    override fun draw(guiRenderer: GuiRenderer) {
        clear()
        currentElement = create()
        currentElement.draw(guiRenderer)
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = currentElement
}

class Mutating<T : Element>(val element: T, private val mutate: T.() -> Unit) : Element by element {
    override fun draw(guiRenderer: GuiRenderer) {
        element.mutate()
        element.draw(guiRenderer)
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = element
}