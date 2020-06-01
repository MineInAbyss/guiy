package com.derongan.minecraft.guiy.kotlin_dsl

import com.derongan.minecraft.guiy.gui.ClickEvent
import com.derongan.minecraft.guiy.gui.Element
import com.derongan.minecraft.guiy.gui.Layout
import com.derongan.minecraft.guiy.gui.elements.ClickableElement
import com.derongan.minecraft.guiy.gui.elements.containers.Containable
import com.derongan.minecraft.guiy.gui.elements.containers.GridContainable
import com.derongan.minecraft.guiy.gui.elements.containers.lists.ScrollingPallet
import com.derongan.minecraft.guiy.gui.elements.lists.WrappedListElement
import com.derongan.minecraft.guiy.gui.inputs.ToggleElement

@DslMarker
annotation class GuiyMarker

fun guiyLayout(init: Layout.() -> Unit): Layout {
    return initTag(Layout(), init)
}

fun <T : Element> initTag(element: T, init: T.() -> Unit, addTo: MutableList<T>? = null): T {
    element.init()
    addTo?.add(element)
    return element
}

fun <T : Element> Containable.initAndAdd(element: T, init: (T.() -> Unit)?): T {
    init?.invoke(element)
    addElement(element)
    return element
}

infix fun <T : Element> T.with(init: T.() -> Unit): T = init().let { this }

fun Containable.button(wrapped: Element, init: (ClickEvent) -> (Unit)) =
        addElement(ClickableElement(wrapped, init))

fun Containable.scrollingPallet(width: Int, init: (ScrollingPallet.() -> Unit)? = null) =
        initAndAdd(ScrollingPallet(width), init)

fun <T> Containable.wrappedList(
        width: Int,
        height: Int,
        list: MutableList<T>,
        init: (WrappedListElement<T>.() -> Unit)? = null
) = initAndAdd(WrappedListElement(width, height, list), init)

fun Containable.toggle(default: Element, toggled: Element = default, init: ToggleElement.() -> (Unit)) =
        initAndAdd(ToggleElement(default, toggled), init)

fun Containable.toggle(name: String, enabledMessage: String = "On", disabledMessage: String = "Off", init: ToggleElement.() -> (Unit)) =
        initAndAdd(ToggleElement(name, enabledMessage, disabledMessage), init)

fun <T : Element> Containable.addElement(element: T, init: T.() -> Unit) =
        addElement(initTag(element, init))

@Deprecated("", ReplaceWith("element{} at x to y"))
fun <T : Element> GridContainable.setElement(x: Int, y: Int, element: T, init: T.() -> Unit): T =
        setElement(x, y, initTag(element, init)).let { element }