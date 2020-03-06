package com.derongan.minecraft.guiy.gui;

import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * An {@link InventoryHolder} with extra functionality for rendering Guiy Elements and Layouts
 */
public class GuiHolder implements InventoryHolder {
    private final Inventory inventory;
    private final int numRows;

    private Element initial;
    private final Plugin plugin;

    public GuiHolder(int numRows, String title, Element initial, Plugin plugin) {
        this.numRows = numRows;
        this.initial = initial;
        this.plugin = plugin;

        inventory = Bukkit.createInventory(this, 9 * numRows, title);
    }

    public GuiHolder(int numRows, String title, Plugin plugin) {
        this.numRows = numRows;
        this.initial = new Layout();
        this.plugin = plugin;

        inventory = Bukkit.createInventory(this, 9 * numRows, title);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }


    public void render() {
        inventory.clear();
        initial.draw((x, y, itemStack) -> {
            if (x + 9 * y < numRows * 9) {
                if (itemStack == null) {
                    inventory.clear(x + 9 * y);
                } else if (x + 9 * y < inventory.getSize() && x + 9 * y >= 0) {
                    inventory.setItem(x + 9 * y, itemStack);
                }
            }
        });
    }

    private void renderFuture() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, this::render);
    }

    public void onClick(ClickEvent clickEvent) {
        processEvent(clickEvent, initial::onClick);
    }

    private void processEvent(ClickEvent clickEvent, Consumer<ClickEvent> clickEventConsumer) {
        clickEventConsumer.accept(clickEvent);
        renderFuture();
    }

    public void show(Player player) {
        render();
        player.openInventory(inventory);
    }

    public void setElement(Element element) {
        this.initial = element;
    }

    public Plugin getPlugin() {
        return plugin;
    }
}
