package com.derongan.minecraft.guiy.gui.elements.dynamic

import com.derongan.minecraft.guiy.gui.Element
import com.derongan.minecraft.guiy.gui.GuiRenderer
import kotlin.reflect.KProperty

class Mutating<T : Element>(val element: T, private val mutate: T.() -> Unit) : Element by element {
    override fun draw(guiRenderer: GuiRenderer) {
        element.mutate()
        element.draw(guiRenderer)
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = element
}