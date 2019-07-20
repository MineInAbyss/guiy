package com.derongan.minecraft.guiy.gui;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

/**
 * Listener that listens for inventory click events and delegates to the {@link GUIHolder} if appropriate.
 */
public class GUIListener implements Listener {
    public GUIListener() {
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {
        InventoryHolder holder = inventoryClickEvent.getClickedInventory().getHolder();
        if (holder instanceof GUIHolder) {
            inventoryClickEvent.setCancelled(true);
            ((GUIHolder) holder).onClick(ClickEvent.createClickEvent(inventoryClickEvent));
        }
    }
}
