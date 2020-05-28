package com.derongan.minecraft.guiy.gui;

import static com.derongan.minecraft.guiy.GuiyKeys.TYPE_KEY;

import java.util.Set;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.ItemTagType;
import org.bukkit.plugin.Plugin;


/**
 * Element that acts like a normal inventory, but only accepts items with a certain TYPE_KEY NBT value.
 */
public class ContainerElement extends FillableElement {
    private final Set<String> allowedKeys;
    private final Plugin plugin;

    /**
     * Constructs a {@link ContainerElement}. The {@code allowedKeys} set contains a set of string values. Items will
     * only be able to be placed in this container if they have a custom tag set with a {@link NamespacedKey} containing
     * the provided plugin and the value "type".
     *
     * @param height      Height of the element
     * @param width       Width of the element
     * @param allowedKeys Set of strings allowed
     * @param plugin      Plugin the tags belong to
     */
    public ContainerElement(int height, int width, Set<String> allowedKeys, Plugin plugin) {
        super(height, width);
        this.allowedKeys = allowedKeys;
        this.plugin = plugin;
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        if (allowedKeys == null) {
            clickEvent.setCancelled(false);
            setElement(clickEvent.getX(), clickEvent.getY(), Cell.forItemStack(clickEvent.getItemOnCursor().clone()));
            return;
        }

        clickEvent.setCancelled(false);
        if (clickEvent.getItemOnCursor().getType() == Material.AIR) {
            removeElement(clickEvent.getX(), clickEvent.getY());
            return;
        }

        ItemStack itemOnCursor = clickEvent.getItemOnCursor();

        if (!itemOnCursor.hasItemMeta()) {
            clickEvent.setCancelled(true);
            return;
        }

        if (!itemOnCursor.getItemMeta()
                .getCustomTagContainer()
                .hasCustomTag(new NamespacedKey(plugin, TYPE_KEY), ItemTagType.STRING)) {
            clickEvent.setCancelled(true);
            return;
        }

        ItemMeta meta = itemOnCursor.getItemMeta();

        String itemType = meta.getCustomTagContainer()
                .getCustomTag(new NamespacedKey(plugin, TYPE_KEY), ItemTagType.STRING);


        boolean allowed = allowedKeys.contains(itemType);


        if (allowed) {
            setElement(clickEvent.getX(), clickEvent.getY(), Cell.forItemStack(itemOnCursor.clone()));
        } else {
            clickEvent.setCancelled(true);
        }
    }

}
