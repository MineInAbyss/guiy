package com.derongan.minecraft.guiy.gui.layouts;

import com.derongan.minecraft.guiy.gui.*;
import de.erethon.headlib.HeadLib;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class HistoryGuiHolder extends GuiHolder {
    protected Player player;
    private List<Layout> history = new ArrayList<>();
    private ClickableElement back;

    public HistoryGuiHolder(int numRows, String title, Plugin plugin) {
        super(numRows, title, new Layout(), plugin);

        //create back button
        Element cell = Cell.forItemStack(HeadLib.RED_X.toItemStack("Back"));
        back = new ClickableElement(cell);
        back.setClickAction(clickEvent -> backInHistory());
    }

    public ClickableElement getBackButton() {
        return back;
    }

    public void addBackButton(Layout layout) {
        history.add(layout);
        layout.addElement(8, 5, back);
    }

    public void backInHistory() {
        if (history.size() <= 1 && player != null) {
            player.closeInventory();
            return;
        }
        Layout previous = history.get(history.size() - 2);
        history.remove(history.size() - 1);

        setElement(previous);
    }
}
