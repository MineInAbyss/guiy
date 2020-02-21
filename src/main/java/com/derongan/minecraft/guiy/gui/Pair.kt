package com.derongan.minecraft.guiy.gui

/**
 * Simple immutable Pair class that holds two elements.
 *
 * @param <E> The type of element to hold.*/
class Pair<E>(val first: E, val second: E) {
    companion object {
        @JvmStatic
        fun <E> create(newFirst: E, newSecond: E) = Pair(newFirst, newSecond)
    }
}