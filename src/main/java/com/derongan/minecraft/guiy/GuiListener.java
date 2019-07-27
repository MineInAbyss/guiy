package com.derongan.minecraft.guiy;

import com.derongan.minecraft.guiy.gui.ClickEvent;
import com.derongan.minecraft.guiy.gui.GuiHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

import javax.inject.Inject;

/**
 * Listener that listens for inventory click events and delegates to the {@link GuiHolder} if appropriate.
 * <p>
 * This can either be registered by your plugin, or you can simply do the same check in your own listener.
 */
public class GuiListener implements Listener {
    @Inject
    public GuiListener() {
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {
        if (inventoryClickEvent.getClickedInventory() == null) {
            return;
        }
        InventoryHolder holder = inventoryClickEvent.getClickedInventory().getHolder();
        if (holder instanceof GuiHolder) {
            inventoryClickEvent.setCancelled(true);
            ((GuiHolder) holder).onClick(ClickEvent.createClickEvent(inventoryClickEvent));
        }
    }
}