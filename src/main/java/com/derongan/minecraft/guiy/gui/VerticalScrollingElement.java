package com.derongan.minecraft.guiy.gui;

import kotlin.Deprecated;
import org.bukkit.inventory.ItemStack;

/**
 * Decorator that adds a vertical scroll bar to an item. The scrollbar is added to the right of the element, and
 * increases its width by 1.
 * <p>
 * Note that currently the fixed height of this element only determines the clickable region, and where the scroll
 * buttons are shown. Extra elements will spill over to fill up the chest to the bottom.
 */
@Deprecated(message = "ListContainable")
public class VerticalScrollingElement implements Element {
    private final int height;
    private final Element wrapped;
    private int scrollFactor = 0;
    private final ItemStack scrollUpItemStack;
    private final ItemStack scrollDownItemStack;

    public VerticalScrollingElement(int height,
                                    Element wrapped,
                                    ItemStack scrollUpItemStack,
                                    ItemStack scrollDownItemStack) {
        this.height = height;
        this.wrapped = wrapped;
        this.scrollDownItemStack = scrollDownItemStack;
        this.scrollUpItemStack = scrollUpItemStack;
    }

    @Override
    public Size getDims() {
        return Size.create(wrapped.getDims().getWidth() + 1, height);
    }

    @Override
    public void draw(GuiRenderer guiRenderer) {
        wrapped.draw((x, y, itemStack) -> guiRenderer.renderAt(x, y - scrollFactor, itemStack));

        guiRenderer.renderAt(getDims().getWidth() - 1, 0, scrollUpItemStack);
        guiRenderer.renderAt(getDims().getWidth() - 1, height - 1, scrollDownItemStack);
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        if (clickEvent.getX() == getDims().getWidth() - 1 && clickEvent.getY() == height - 1 && scrollFactor + height - 1 < wrapped
                .getDims().getHeight()) {
            scrollFactor++;
        } else if (clickEvent.getX() == getDims().getWidth() - 1 && clickEvent.getY() == 0 && scrollFactor != 0) {
            scrollFactor--;
        } else if (clickEvent.getX() != getDims().getWidth() - 1 && clickEvent.getY() != getDims().getHeight() - 1) {
            wrapped.onClick(ClickEvent.offsetClickEvent(clickEvent, 0, -scrollFactor));
        }
    }
}
