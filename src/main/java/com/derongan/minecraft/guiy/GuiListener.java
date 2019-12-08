package com.derongan.minecraft.guiy;

import com.derongan.minecraft.guiy.gui.ClickEvent;
import com.derongan.minecraft.guiy.gui.GuiHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;

import javax.inject.Inject;

/**
 * Listener that listens for inventory click events and delegates to the {@link GuiHolder} if appropriate.
 * <p>
 * This can either be registered by your plugin, or you can simply do the same check in your own listener.
 */
public class GuiListener implements Listener {
    private final Plugin plugin;

    @Inject
    public GuiListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {
        if (inventoryClickEvent.getClickedInventory() == null) {
            return;
        }
        InventoryHolder holder = inventoryClickEvent.getInventory().getHolder();
        if (isGuiy(holder)) {
            inventoryClickEvent.setCancelled(true);
            if (isGuiy(inventoryClickEvent.getClickedInventory().getHolder()))
                ((GuiHolder) holder).onClick(ClickEvent.createClickEvent(inventoryClickEvent));
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent inventoryDragEvent) {
        InventoryHolder holder = inventoryDragEvent.getInventory().getHolder();
        if (isGuiy(holder)) inventoryDragEvent.setCancelled(true);
    }

    private boolean isGuiy(InventoryHolder holder) { //FIXME holder isn't an instance of GuiHolder after a plugin reload so no clicks happen
        return holder instanceof GuiHolder && ((GuiHolder) holder).getPlugin() == plugin;
    }
}
