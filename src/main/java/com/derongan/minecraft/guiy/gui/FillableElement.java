package com.derongan.minecraft.guiy.gui;

import org.jetbrains.annotations.NotNull;

/**
 * Element that supports having Elements added to it dynamically.
 */
public class FillableElement implements Element, ListContainable, GridContainable {
    private final int height;
    private final int width;

    private final Element[][] elements;

    // TODO make this work with fixed size
    public FillableElement(int height, int width) {
        this.height = height;
        this.width = width;

        elements = new Element[width][height];
    }

    @NotNull
    @Override
    public Size getDims() {
        return Size.create(width, height);
    }

    @Override
    public void draw(@NotNull GuiRenderer guiRenderer) {
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
     *
     * @return the element added
     */
    @Override
    public <T extends Element> @NotNull T addElement(@NotNull T element) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (elements[x][y] == null) {
                    elements[x][y] = element;
                    return element;
                }
            }
        }
        return element;
    }

    /**
     * Adds an element at a specific position.
     *
     * @param x       The x coordinate within this element.
     * @param y       The y coordinate within this element.
     * @param element The element to add to this element.
     * @return the element set
     */
    @Override
    public <T extends Element> @NotNull T setElement(int x, int y, @NotNull T element) {
        elements[x][y] = element;
        return element;
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
     * Removes all references to a specific element from the contents of the elements grid.
     *
     * @param element The element to be removed.
     */
    @Override
    public void removeElement(Element element) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (elements[x][y].equals(element)) {
                    elements[x][y] = null;
                }
            }
        }
    }

    //TODO maybe a method to remove spacing between elements? (i.e. [element1, null, element2] becomes [element1, element2, null])

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
