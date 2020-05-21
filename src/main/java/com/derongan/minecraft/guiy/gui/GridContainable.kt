package com.derongan.minecraft.guiy.gui

interface GridContainable {
    fun <T : Element> setElement(x: Int, y: Int, element: T): T
    fun removeElement(x: Int, y: Int)
}