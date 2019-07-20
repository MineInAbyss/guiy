package com.derongan.minecraft.guiy;

import com.derongan.minecraft.guiy.gui.ClickEvent;
import com.derongan.minecraft.guiy.gui.GUIHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

import javax.inject.Inject;

/**
 * Listener that listens for inventory click events and delegates to the {@link GUIHolder} if appropriate.
 * <p>
 * This can either be registered by your plugin, or you can simply do the same check in your own listener.
 */
public class GUIListener implements Listener {
    @Inject
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
