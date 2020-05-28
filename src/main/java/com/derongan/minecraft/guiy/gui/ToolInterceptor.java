package com.derongan.minecraft.guiy.gui;

import com.derongan.minecraft.guiy.gui.elements.containers.ContainerElement;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static com.derongan.minecraft.guiy.GuiyKeys.TYPE_KEY;

public class ToolInterceptor implements Element {
    public static final String TOOL_TYPE_KEY = "tool_type";
    public static final String TOOL_VALUE = "tool";

    private final ContainerElement wrapped;
    private final Plugin plugin;
    private final Map<String, BiConsumer<ClickEvent, Element>> handlerMap = new HashMap<>();

    public ToolInterceptor(ContainerElement wrapped, Plugin plugin) {
        this.wrapped = wrapped;
        this.plugin = plugin;
    }

    @NotNull
    @Override
    public Size getDims() {
        return wrapped.getDims();
    }

    @Override
    public void draw(@NotNull GuiRenderer guiRenderer) {
        wrapped.draw(guiRenderer);
    }

    public void registerToolAction(String tool, BiConsumer<ClickEvent, Element> clickedElementConsumer) {
        handlerMap.put(tool, clickedElementConsumer);
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        ItemStack itemOnCursor = clickEvent.getItemOnCursor();
        if (itemOnCursor.hasItemMeta()) {
            if(itemOnCursor.getItemMeta() == null) return;

            PersistentDataContainer customTagContainer = itemOnCursor.getItemMeta().getPersistentDataContainer();
            if (customTagContainer.has(new NamespacedKey(plugin, TYPE_KEY), PersistentDataType.STRING)) {
                if (customTagContainer.get(new NamespacedKey(plugin, TYPE_KEY), PersistentDataType.STRING).equals(TOOL_VALUE)) {

                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        clickEvent.getRawEvent().getWhoClicked().setItemOnCursor(null);
                    }, 1);

                    Element element = wrapped.getElement(clickEvent.getX(), clickEvent.getY());

                    String toolType = customTagContainer.get(new NamespacedKey(plugin, TOOL_TYPE_KEY), PersistentDataType.STRING);

                    handlerMap.getOrDefault(toolType, (click, elem) -> wrapped.onClick(click))
                            .accept(clickEvent, element);

                    return;
                }
            }
        }

        wrapped.onClick(clickEvent);
    }
}
