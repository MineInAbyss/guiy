package com.derongan.minecraft.guiy.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Basic {@link Element} of the UI. Simply renders a specific {@link ItemStack}.
 */
public class Cell implements Element {
    public final ItemStack itemStack;
    private boolean dirty = true;

    private Cell(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    /**
     * Create a {@link Cell} with an {@link ItemStack} of type {@code material}.
     *
     * @param material Material to create an ItemStack of.
     * @return Cell that displays as a {@code material}.
     */
    public static Element forMaterial(Material material) {
        return forMaterial(material, "");
    }

    /**
     * Create a {@link Cell} with an {@link ItemStack} of type {@code material} with a specified name.
     *
     * @param material Material to create an ItemStack of.
     * @param name     Name this cell displays when hovered over.
     * @return Cell that displays as a {@code material}.
     */
    public static Element forMaterial(Material material, String name) {
        ItemStack itemStack = new ItemStack(material);

        ItemMeta meta = Bukkit.getItemFactory().getItemMeta(material);

        meta.setDisplayName(name);

        itemStack.setItemMeta(meta);

        return new Cell(itemStack);
    }

    /**
     * Create a {@link Cell} that displays {@code itemStack}.
     *
     * @param itemStack {@link ItemStack} to display.
     * @return Cell that displays {@code itemStack}.
     */
    public static Element forItemStack(ItemStack itemStack) {
        return new Cell(itemStack);
    }

    /**
     * Create a {@link Cell} that displays {@code itemStack} with a specified name.
     *
     * @param itemStack {@link ItemStack} to display
     * @param name      Name this cell displays when hovered over..
     * @return Cell that displays {@code itemStack}.
     */
    public static Element forItemStack(ItemStack itemStack, String name) {
        ItemMeta meta = itemStack.getItemMeta();

        meta.setDisplayName(name);

        itemStack.setItemMeta(meta);

        return new Cell(itemStack);
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        clickEvent.setCancelled(true);
    }

    @Override
    public Size getDims() {
        return Size.create(1, 1);
    }

    @Override
    public void draw(GuiRenderer guiRenderer) {
        guiRenderer.renderAt(0, 0, itemStack);
        dirty = false;
    }
}
