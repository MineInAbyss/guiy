package com.derongan.minecraft.guiy.gui;

import com.google.auto.value.AutoValue;

/**
 * Simple immutable Pair class that holds two elements.
 *
 * @param <E> The type of element to hold.
 */
@AutoValue
public abstract class Pair<E> {
    public abstract E getFirst();

    public abstract E getSecond();

    public static <E> Pair<E> create(E newFirst, E newSecond) {
        return new AutoValue_Pair<>(newFirst, newSecond);
    }
}
