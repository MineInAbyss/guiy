package com.derongan.minecraft.guiy.gui.inputs;

import com.derongan.minecraft.guiy.gui.ClickEvent;
import com.derongan.minecraft.guiy.gui.Element;
import com.derongan.minecraft.guiy.gui.GuiRenderer;
import com.derongan.minecraft.guiy.gui.Size;
import com.google.common.collect.Iterators;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * An input that lets one of a fixed set of strings be chosen.
 * This Element has a fixed width, but its height grows to the size needed to display all of the strings.
 * <p>
 * {@link com.derongan.minecraft.guiy.gui.VerticalScrollingElement} is a good choice for displaying FixedInputs
 * that are too large to fit in the GUI.
 */
public class FixedStringInput implements Element, Input<String> {
    private final int width;
    private final List<String> strings;
    private Integer selectedIndex = null;
    private Consumer<String> consumer;

    public FixedStringInput(int width, List<String> strings) {
        this.width = width;
        this.strings = strings;
    }

    @Override
    public Size getDims() {
        return Size.create(width, strings.size() / width + 1);
    }

    @Override
    public void draw(GuiRenderer guiRenderer) {
        Iterator<Material> materialIterator = getWoolIterator();

        for (int i = 0; i < strings.size(); i++) {
            String name = strings.get(i);

            Material material = materialIterator.next();

            if (selectedIndex != null && i == selectedIndex) {
                material = Material.ELYTRA;
            }
            ItemStack itemStack = new ItemStack(material);

            ItemMeta meta = Bukkit.getItemFactory().getItemMeta(material);
            meta.setDisplayName(name);

            itemStack.setItemMeta(meta);

            guiRenderer.renderAt(i % width, i / width, itemStack);
        }
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        this.selectedIndex = clickEvent.getX() + clickEvent.getY() * width;
    }

    private Iterator<Material> getWoolIterator() {
        List<Material> wools = Arrays.stream(Material.values())
                .filter(mat -> mat.name().endsWith("_WOOL"))
                .collect(toImmutableList());

        return Iterators.cycle(wools);
    }

    @Override
    public String getResult() {
        if (selectedIndex == null) {
            return null;
        }
        return strings.get(selectedIndex);
    }

    @Override
    public Consumer<String> getSubmitAction() {
        return consumer;
    }

    @Override
    public void setSubmitAction(Consumer<String> consumer) {

        this.consumer = consumer;
    }
}
