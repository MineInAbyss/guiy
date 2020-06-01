package com.derongan.minecraft.guiy.gui.elements.dynamic

import com.derongan.minecraft.guiy.gui.Element
import com.derongan.minecraft.guiy.gui.GuiRenderer
import com.derongan.minecraft.guiy.gui.elements.containers.singular.Wrappable
import kotlin.reflect.KProperty

class Mutating<T : Element>(override val wrapped: T, private val mutate: T.() -> Unit) : Wrappable() {
    override fun draw(guiRenderer: GuiRenderer) {
        wrapped.mutate()
        super.draw(guiRenderer)
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = wrapped
}