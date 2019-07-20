package com.derongan.minecraft.guiy.gui;

import org.bukkit.event.Cancellable;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Wrapper for a {@link InventoryClickEvent} that exposes more useful coordinates.
 */
public interface ClickEvent extends Cancellable {
    int getX();

    int getY();

    ItemStack getItemOnCursor();

    InventoryClickEvent getRawEvent();

    static ClickEvent createClickEvent(InventoryClickEvent inventoryClickEvent) {
        return new ClickEvent() {
            @Override
            public int getX() {
                return inventoryClickEvent.getSlot() % 9;
            }

            @Override
            public int getY() {
                return inventoryClickEvent.getSlot() / 9;
            }

            @Override
            public ItemStack getItemOnCursor() {
                return inventoryClickEvent.getCursor();
            }

            @Override
            public boolean isCancelled() {
                return inventoryClickEvent.isCancelled();
            }

            @Override
            public void setCancelled(boolean cancel) {
                inventoryClickEvent.setCancelled(cancel);
            }

            @Override
            public InventoryClickEvent getRawEvent() {
                return inventoryClickEvent;
            }
        };
    }

    static ClickEvent offsetClickEvent(ClickEvent clickEvent, int x, int y) {
        return new ClickEvent() {
            @Override
            public int getX() {
                return clickEvent.getX() - x;
            }

            @Override
            public int getY() {
                return clickEvent.getY() - y;
            }

            @Override
            public ItemStack getItemOnCursor() {
                return clickEvent.getItemOnCursor();
            }

            @Override
            public boolean isCancelled() {
                return clickEvent.isCancelled();
            }

            @Override
            public void setCancelled(boolean cancel) {
                clickEvent.setCancelled(cancel);
            }

            @Override
            public InventoryClickEvent getRawEvent() {
                return clickEvent.getRawEvent();
            }
        };
    }
}
