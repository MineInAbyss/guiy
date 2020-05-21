package com.derongan.minecraft.guiy.gui

interface ListContainable {
    fun <T : Element> addElement(element: T): T
    fun removeElement(element: Element?)
}

/**
 * Adds a list of element with the logic of addElement.
 */
//TODO not sure if this is the best way to pull out logic,
// if all subclasses are in Kotlin, best to keep in the interface
fun ListContainable.addAll(elements: List<Element>) = elements.forEach { addElement(it) }

fun ListContainable.addElements(elements: ListContainable.() -> Unit) {
    elements()
}