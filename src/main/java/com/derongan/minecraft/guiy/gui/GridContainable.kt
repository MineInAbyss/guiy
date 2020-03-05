package com.derongan.minecraft.guiy.gui

interface GridContainable {
    fun setElement(x: Int, y: Int, element: Element)
    fun removeElement(x: Int, y: Int)
}