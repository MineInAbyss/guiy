package com.derongan.minecraft.guiy.kotlin_dsl

import com.derongan.minecraft.guiy.gui.ClickEvent
import com.derongan.minecraft.guiy.gui.Element
import com.derongan.minecraft.guiy.gui.GridContainable
import com.derongan.minecraft.guiy.gui.Layout
import com.derongan.minecraft.guiy.gui.elements.ClickableElement
import com.derongan.minecraft.guiy.gui.elements.containers.Containable
import com.derongan.minecraft.guiy.gui.elements.containers.lists.ScrollingPallet
import com.derongan.minecraft.guiy.gui.elements.dynamic.Refreshing
import com.derongan.minecraft.guiy.gui.elements.lists.WrappedListElement

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
        filter: MutableList<T>.() -> List<T> = { this },
        toElement: WrappedListElement<T>.(T) -> Element,
        init: (WrappedListElement<T>.() -> Unit)? = null
) = initAndAdd(WrappedListElement(width, height, list, filter, toElement), init)

fun <T : Element> Containable.addElement(element: T, init: T.() -> Unit) =
        addElement(initTag(element, init))

fun <T : Element> Layout.dynamic(create: Layout.() -> T): Refreshing<T> {
    return addElement(Refreshing(create))
}

@Deprecated("", ReplaceWith("element{} at x to y"))
fun <T : Element> GridContainable.setElement(x: Int, y: Int, element: T, init: T.() -> Unit): T =
        setElement(x, y, initTag(element, init)).let { element }