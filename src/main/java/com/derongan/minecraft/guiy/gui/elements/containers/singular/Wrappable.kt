package com.derongan.minecraft.guiy.gui.elements.containers.singular

import com.derongan.minecraft.guiy.gui.Element
import com.derongan.minecraft.guiy.gui.Size

abstract class Wrappable(protected open val wrapped: Element) : Element by wrapped {
    override val dims: Size get() = wrapped.dims
}