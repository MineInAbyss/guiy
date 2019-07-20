package com.derongan.minecraft.guiy.gui;

import java.util.function.Consumer;

/**
 * Wrapper {@link Element} with a click callback.
 */
public class ClickableElement implements Element {
    private Element wrapped;
    private Consumer<ClickEvent> pickupAction;

    /**
     * @param wrapped Element to delegate all clicks to.
     */
    public ClickableElement(Element wrapped) {
        this.wrapped = wrapped;
        this.pickupAction = a -> a.setCancelled(true);
    }

    @Override
    public Size getSize() {
        return wrapped.getSize();
    }

    @Override
    public void draw(GuiRenderer guiRenderer) {
        wrapped.draw(guiRenderer);
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        pickupAction.accept(clickEvent);
    }

    /**
     * Sets the onClick action.
     *
     * @param clickAction the action to take when clicked.
     */
    public void setClickAction(Consumer<ClickEvent> clickAction) {
        this.pickupAction = clickAction;
    }
}
