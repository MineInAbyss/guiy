package com.derongan.minecraft.guiy.gui;

import com.google.auto.value.AutoValue;

/**
 * Simple immutable Size class that holds a width and height.
 */
@AutoValue
public abstract class Size {
    public abstract int getWidth();
    public abstract int getHeight();

    public static Size create(int newWidth, int newHeight) {
        return new AutoValue_Size(newWidth, newHeight);
    }
}
