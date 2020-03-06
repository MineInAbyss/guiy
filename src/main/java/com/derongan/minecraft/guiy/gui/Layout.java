package com.derongan.minecraft.guiy.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

/**
 * An element thats purpose is to contain other elements at specific locations. It forwards any clicks to
 * <em>any</em> element that claims to be in an inventory slot, based on its position and size.
 */
public class Layout implements Element, GridContainable {
    private final Map<Pair<Integer>, Element> elements;

    public Layout() {
        elements = new HashMap<>();
    }

    /**
     * Adds an element.
     * <p>
     * Adding an element to the location of an existing element will overwrite the existing element.
     *
     * @param x X coordinate to add the element at.
     * @param y Y coordinate to add the element at.
     * @param e The element ot add.
     */
    public void setElement(int x, int y, Element e) {
        elements.put(Pair.create(x, y), e);
    }

    /**
     * Removes an element.
     *
     * @param x X coordinate to remove the element from.
     * @param y Y coordinate to remove the element from.
     */
    public void removeElement(int x, int y) {
        elements.remove(Pair.create(x, y));
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        findAffectedElements(clickEvent.getX(), clickEvent.getY())
                .forEach(child -> child.getValue()
                        .onClick(ClickEvent.offsetClickEvent(clickEvent, child.getKey().getFirst(), child.getKey()
                                .getSecond())));
    }

    @NotNull
    private Stream<Map.Entry<Pair<Integer>, Element>> findAffectedElements(int x, int y) {
        return elements.entrySet()
                .stream()
                .filter(child -> {
                    Pair<Integer> coords = child.getKey();
                    Size size = child.getValue().getSize();

                    int xMin = coords.getFirst();
                    int xMax = coords.getFirst() + size.getWidth() - 1;
                    int yMin = coords.getSecond();
                    int yMax = coords.getSecond() + size.getHeight() - 1;

                    return x <= xMax && x >= xMin && y <= yMax && y >= yMin;
                });
    }

    @Override
    public void draw(GuiRenderer guiRenderer) {
        elements.forEach((coord, element) -> {
            element.draw((x, y, inv) -> guiRenderer.renderAt(x + coord.getFirst(), y + coord.getSecond(), inv));
        });
    }

    @Override
    public Size getSize() {
        int width = 0;
        int height = 0;
        Set<Map.Entry<Pair<Integer>, Element>> entries = elements.entrySet();
        for (Map.Entry<Pair<Integer>, Element> entry : entries) {
            Pair<Integer> loc = entry.getKey();
            Size size = entry.getValue().getSize();

            width = Math.max(width, loc.getFirst() + size.getWidth());
            height = Math.max(height, loc.getSecond() + size.getWidth());
        }

        return Size.create(width, height);
    }


    @Override
    public String toString() {
        return String.format("Layout(%dx%d)", getSize().getWidth(), getSize().getWidth());
    }
}
