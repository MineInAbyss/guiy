package com.derongan.minecraft.guiy.gui

interface GridContainable : ListContainable {
    fun <T : Element> setElement(x: Int, y: Int, element: T): T
    fun removeElement(x: Int, y: Int)

    //TODO not sure if this is a good thing to do, it require opt-in adn generates extra bytecode. We don't need it if
    // all subclasses are Kotlin
    @JvmDefault
    override fun <T : Element> addElement(element: T): T = element

    @JvmDefault
    operator fun IntRange.plus(element: Element) {
        setElement(first, last, element)
    }

    @JvmDefault
    infix fun <T : Element> Pair<Int, Int>.add(element: T): T {
        return setElement(first, second, element)
    }

    @JvmDefault
    infix fun <T : Element> T.at(x: Int): ElementPositioner<T> {
        return ElementPositioner(this, x)
    }

    @JvmDefault
    infix fun <T : Element> ElementPositioner<T>.to(y: Int): T {
        return setElement(x, y, element)
    }
}

class ElementPositioner<T : Element>(val element: T, val x: Int)

fun GridContainable.at(x: Int, y: Int) = x to y