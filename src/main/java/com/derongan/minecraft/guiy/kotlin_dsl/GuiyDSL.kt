package com.derongan.minecraft.guiy.kotlin_dsl

import com.derongan.minecraft.guiy.gui.*
import com.derongan.minecraft.guiy.gui.layouts.HistoryGuiHolder

@DslMarker
annotation class GuiyMarker

inline fun guiyLayout(init: Layout.() -> Unit): Layout {
    return initTag(Layout(), init)
}

inline fun <T : Element> initTag(element: T, init: T.() -> Unit, addTo: MutableList<T>? = null): T {
    element.init()
    addTo?.add(element)
    return element
}

fun ListContainable.button(wrapped: Element, init: (ClickEvent) -> (Unit)) =
        addElement(ClickableElement(wrapped, init))

fun GridContainable.button(x: Int, y: Int, wrapped: Element, init: (ClickEvent) -> (Unit)) =
        setElement(x, y, ClickableElement(wrapped, init))

fun Layout.backButtonTo(historyHolder: HistoryGuiHolder) = historyHolder.addBackButton(this)

fun <T : Element> ListContainable.addElement(element: T, init: T.() -> Unit) = addElement(initTag(element, init))

fun <T : Element> GridContainable.setElement(x: Int, y: Int, element: T, init: T.() -> Unit) = setElement(x, y, initTag(element, init))
