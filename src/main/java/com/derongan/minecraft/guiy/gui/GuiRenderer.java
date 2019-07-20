package com.derongan.minecraft.guiy.gui;

import org.bukkit.inventory.ItemStack;

@FunctionalInterface
public interface GuiRenderer {
    /**
     * Renders an {@link ItemStack} at a position. The general usecase for this would be rendering to an Inventory, but
     * you could customise this and have something that draws ItemStack data to a book or something.
     *
     * @param x         x location to draw to.
     * @param y         y location to draw to.
     * @param itemStack the ItemStack to draw.
     */
    void renderAt(int x, int y, ItemStack itemStack);
}
