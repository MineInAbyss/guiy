package com.derongan.minecraft.guiy.gui

interface ListContainable {
    fun addElement(element: Element)
    fun addAll(elements: List<Element>)
    fun removeElement(element: Element?)
}