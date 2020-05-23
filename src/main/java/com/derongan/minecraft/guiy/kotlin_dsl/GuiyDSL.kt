package com.derongan.minecraft.guiy.kotlin_dsl

import com.derongan.minecraft.guiy.gui.*
import com.derongan.minecraft.guiy.gui.elements.ListElement
import javafx.scene.Parent
import tornadofx.View
import tornadofx.fieldset
import tornadofx.form

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

fun <T : Element> ListContainable.initAndAdd(element: T, init: (T.() -> Unit)?): T {
    init?.invoke(element)
    addElement(element)
    return element
}

infix fun <T : Element> T.with(init: T.() -> Unit): T = init().let { this }

fun ListContainable.button(wrapped: Element, init: (ClickEvent) -> (Unit)) =
        addElement(ClickableElement(wrapped, init))

fun ListContainable.scrollingPallet(width: Int, init: (ScrollingPallet.() -> Unit)? = null) =
        initAndAdd(ScrollingPallet(width), init)

fun <T> ListContainable.list(
        width: Int,
        height: Int,
        list: List<T>,
        toElement: ListElement<T>.(T) -> Element,
        init: (ListElement<T>.() -> Unit)? = null
) = initAndAdd(ListElement(width, height, list, toElement), init)

fun <T : Element> ListContainable.addElement(element: T, init: T.() -> Unit) =
        addElement(initTag(element, init))

fun <T : Element> GridContainable.setElement(x: Int, y: Int, element: T, init: T.() -> Unit): T =
        setElement(x, y, initTag(element, init)).let { element }

//TODO remove
private class Test : View() {
    override val root: Parent = form {
        fieldset {

        }
    }
}