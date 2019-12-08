package com.derongan.minecraft.guiy.gui.layouts;

import com.derongan.minecraft.guiy.gui.Layout;

public class HistoryLayout extends Layout {
    protected HistoryGuiHolder holder;

    public HistoryLayout(HistoryGuiHolder holder) {
        this.holder = holder;
    }

    public HistoryGuiHolder getHolder() {
        return holder;
    }

    public void setHolder(HistoryGuiHolder holder) {
        this.holder = holder;
    }

    public void addBackButton() {
        holder.addBackButton(this);
        addElement(8, 5, holder.getBackButton());
    }

    public void addBackButton(Layout otherLayout) {
        holder.addBackButton(otherLayout);
    }

    public void backInHistory() {
        holder.backInHistory();
    }
}
