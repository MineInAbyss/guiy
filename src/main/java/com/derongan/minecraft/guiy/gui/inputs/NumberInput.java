package com.derongan.minecraft.guiy.gui.inputs;

import com.derongan.minecraft.guiy.gui.ClickEvent;
import com.derongan.minecraft.guiy.gui.Element;
import com.derongan.minecraft.guiy.gui.GuiRenderer;
import com.derongan.minecraft.guiy.gui.Size;
import de.erethon.headlib.HeadLib;
import java.util.ArrayList;
import java.util.function.Consumer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Input that produces a numeric result. This input is always a 9x5 element.
 * The result is always a Double.
 */
public class NumberInput implements Element, Input<Double> {
    private final ArrayList<String> inputs;
    private Consumer<Double> consumer;

    public NumberInput() {
        inputs = new ArrayList<>(9);
    }

    @Override
    public Size getSize() {
        return Size.create(9, 5);
    }

    @Override
    public void draw(GuiRenderer guiRenderer) {
        for (int y = 1; y < 6; y++) {
            guiRenderer.renderAt(0, y, new ItemStack(Material.IRON_BARS));
            guiRenderer.renderAt(1, y, new ItemStack(Material.IRON_BARS));
            guiRenderer.renderAt(2, y, new ItemStack(Material.IRON_BARS));
            guiRenderer.renderAt(6, y, new ItemStack(Material.IRON_BARS));
            guiRenderer.renderAt(7, y, new ItemStack(Material.IRON_BARS));
            guiRenderer.renderAt(8, y, new ItemStack(Material.IRON_BARS));
        }

        ItemStack decimal = HeadLib.WOODEN_DOT.toItemStack(".");

        for (int i = 0; i < 9; i++) {
            guiRenderer.renderAt(3 + (i % 3), 1 + (i / 3), HeadLib.valueOf(String.format("WOODEN_%d", i + 1))
                    .toItemStack(String.valueOf(i + 1)));
        }

        guiRenderer.renderAt(3, 4, decimal);
        guiRenderer.renderAt(4, 4, HeadLib.WOODEN_0.toItemStack("0"));
        guiRenderer.renderAt(5, 4, HeadLib.WOODEN_ARROW_LEFT.toItemStack("Delete"));

        for (int i = 0; i < inputs.size(); i++) {
            guiRenderer.renderAt((9 - inputs.size()) + i, 0, HeadLib.valueOf(String.format("WOODEN_%s", inputs.get(i)))
                    .toItemStack(inputs.get(i)));
        }
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        if (clickEvent.getX() > 2 && clickEvent.getX() < 6) {
            if (clickEvent.getY() < 4 && clickEvent.getY() > 0) {
                int x = clickEvent.getX() - 3;
                int y = clickEvent.getY() - 1;

                int num = y * 3 + x + 1;

                inputs.add(String.valueOf(num));
            }
        }

        if (clickEvent.getX() == 5 && clickEvent.getY() == 4) {
            inputs.remove(inputs.size() - 1);
        }

        if (clickEvent.getX() == 3 && clickEvent.getY() == 4) {
            inputs.add("DOT");
        }

        if (clickEvent.getX() == 4 && clickEvent.getY() == 4) {
            inputs.add("0");
        }
    }

    @Override
    public Double getResult() {
        return Double.valueOf(String.join("", inputs).replaceAll("DOT", "."));
    }

    @Override
    public void setSubmitAction(Consumer<Double> consumer) {
        this.consumer = consumer;
    }

    @Override
    public Consumer<Double> getSubmitAction() {
        return consumer;
    }
}
