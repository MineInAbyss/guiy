package com.derongan.minecraft.guiy.gui

/**
 * Simple immutable Size class that holds a width and height.
 */
data class Size(val width: Int, val height: Int) {
    companion object {
        @JvmStatic
        fun create(newWidth: Int, newHeight: Int) = Size(newWidth, newHeight)
    }
}