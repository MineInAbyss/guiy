package com.derongan.minecraft.guiy.gui;

/**
 * Element that supports having Elements added to it dynamically.
 */
public class FillableElement implements Element {
    private final int height;
    private final int width;

    private Element[][] elements;

    // TODO make this work with fixed size
    public FillableElement(int height, int width) {
        this.height = height;
        this.width = width;

        elements = new Element[width][height];
    }

    @Override
    public Size getSize() {
        return Size.create(width, height);
    }

    @Override
    public void draw(GuiRenderer guiRenderer) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                final int xc = x;
                final int yc = y;

                if (elements[x][y] != null) {
                    elements[x][y].draw((x1, y1, mat) -> guiRenderer.renderAt(x1 + xc, y1 + yc, mat));
                }
            }
        }
    }

    /**
     * Adds an element at the next available position.
     */
    public void addElement(Element element) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (elements[x][y] == null) {
                    elements[x][y] = element;
                    return;
                }
            }
        }
    }

    /**
     * Adds an element at a specific position.
     *
     * @param x       The x coordinate within this element.
     * @param y       The y coordinate within this element.
     * @param element The element to add to this element.
     */
    public void setElement(int x, int y, Element element) {
        elements[x][y] = element;
    }

    /**
     * Removes an element at a specific position.
     * If there is no element this method is a noop.
     *
     * @param x The x coordinate within this element.
     * @param y The y coordinate within this element.
     */
    public void removeElement(int x, int y) {
        elements[x][y] = null;
    }

    /**
     * Checks if there is an element at a specific position.
     *
     * @param x The x coordinate within this element.
     * @param y The y coordinate within this element.
     * @return true if there is an element at this coordinate.
     */
    public boolean hasElement(int x, int y) {
        return elements[x][y] != null;
    }


    /**
     * Gets the element at a specific position, returning null if there is no element.
     *
     * @param x The x coordinate within this element.
     * @param y The y coordinate within this element.
     * @return the element at this position or null.
     */
    public Element getElement(int x, int y) {
        return elements[x][y];
    }

    /**
     * Remove all elements from this element.
     */
    public void clear() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                elements[x][y] = null;
            }
        }
    }


    /**
     * Calls contained elements onclick at the appropriate location.
     */
    @Override
    public void onClick(ClickEvent clickEvent) {
        Element element = elements[clickEvent.getX()][clickEvent.getY()];
        if (element != null) {
            element.onClick(ClickEvent.offsetClickEvent(clickEvent, clickEvent.getX(), clickEvent.getY()));
        }
    }
}
