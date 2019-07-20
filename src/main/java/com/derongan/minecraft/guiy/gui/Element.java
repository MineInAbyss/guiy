package com.derongan.minecraft.guiy.gui;

/**
 * Interface shared by all GUI elements.
 */
public interface Element {
    /**
     * Get the size of the element.
     */
    Size getSize();

    /**
     * Draws the element to the provided {@link GuiRenderer}.
     */
    void draw(GuiRenderer guiRenderer);

    /**
     * Runs when an element is clicked. This generally includes any slot that the element covers with its Size.
     */
    default void onClick(ClickEvent clickEvent) {
        clickEvent.setCancelled(true);
    }
}
