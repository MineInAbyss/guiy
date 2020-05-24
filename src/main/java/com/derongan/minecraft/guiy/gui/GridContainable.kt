package com.derongan.minecraft.guiy.gui

interface GridContainable : ListContainable {
    fun <T : Element> setElement(x: Int, y: Int, element: T): T
    fun removeElement(x: Int, y: Int)

    //TODO not sure if this is a good thing to do, it require opt-in adn generates extra bytecode. We don't need it if
    // all subclasses are Kotlin
    @JvmDefault
    override fun <T : Element> addElement(element: T): T = element

    @JvmDefault
    fun <T : Element> T.at(x: Int, y: Int): T = setElement(x, y, this)
}