package com.derongan.minecraft.guiy.gui.elements.containers.singular

import com.derongan.minecraft.guiy.gui.Element


/**
 * Wrapper element that supports swapping out its wrapped element.
 */
class SwappableElement(override var wrapped: Element) : Wrappable() {
    /**
     * Swap the contained element to the provided element.
     * @param element The element to swap to.
     */
    fun swap(element: Element) {
        wrapped = element
    }
}