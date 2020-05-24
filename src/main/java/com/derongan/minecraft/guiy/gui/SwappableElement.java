package com.derongan.minecraft.guiy.gui;

/**
 * Wrapper element that supports swapping out its wrapped element.
 */
public class SwappableElement implements Element {
    private Element wrapped;

    public SwappableElement(Element wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public Size getDims() {
        return wrapped.getDims();
    }

    @Override
    public void draw(GuiRenderer guiRenderer) {
        wrapped.draw(guiRenderer);
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        wrapped.onClick(clickEvent);
    }

    /**
     * Swap the contained element to the provided element.
     * @param element The element to swap to.
     */
    public void swap(Element element) {
        this.wrapped = element;
    }
}
