package com.derongan.minecraft.guiy.gui;

import de.erethon.headlib.HeadLib;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * A pallet that can hold a number of elements (referred to as tools). It allows cyclic scrolling left and right. It is
 * always one slot high. Tools are normal Elements, and define their own behaviour.
 */
public class ScrollingPallet implements Element, ListContainable {
    private final int width;
    private final Layout innerLayout;
    private final List<Element> tools = new ArrayList<>();

    private int origin = 0;

    /**
     * Constructor
     *
     * @param width width of the scrolling pallet. Must be at least 3 in order to show a tool and the scroll buttons.
     */
    public ScrollingPallet(int width) {
        checkArgument(width >= 3, "Width must be over 2 in order to include controls and tools");
        this.width = width;
        this.innerLayout = new Layout();

        innerLayout.setElement(0, 0, Cell.forItemStack(HeadLib.WOODEN_ARROW_LEFT.toItemStack("Left")));
        innerLayout.setElement(width - 1, 0, Cell.forItemStack(HeadLib.WOODEN_ARROW_RIGHT.toItemStack("Right")));
    }

    @Override
    public Size getSize() {
        return Size.create(width, 1);
    }

    @Override
    public void draw(GuiRenderer guiRenderer) {
        innerLayout.draw(guiRenderer);

        for (int i = 0; i < width - 2; i++) {
            int finalI = i;

            getToolAt(i).ifPresent(element -> element.draw((x, y, itemStack) -> guiRenderer.renderAt(1 + finalI, 0, itemStack)));
        }
    }

    /**
     * Adds a cell too the pallet.
     *
     * @param cell The cell to add.
     * @return the cell that was added
     */
    @Override
    public <T extends Element> @NotNull T addElement(@NotNull T cell) {
        tools.add(cell);
        return cell;
    }

    /**
     * Removes a cell from the pallet.
     *
     * @param cell The cell to remove.
     */
    @Override
    public void removeElement(Element cell) {
        tools.remove(cell);
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        if (clickEvent.getX() == 0) {
            origin = Math.floorMod((origin + 1), Math.max(width - 2, tools.size()));
            return;
        }

        if (clickEvent.getX() == width - 1) {
            origin = Math.floorMod((origin - 1), Math.max(width - 2, tools.size()));
            return;
        }


        getToolAt(clickEvent.getX() - 1).ifPresent(clicked -> {
            clicked.onClick(ClickEvent.offsetClickEvent(clickEvent, clickEvent.getX(), clickEvent.getY()));
        });
    }

    /**
     * Gets the tool currently at the specific x location.
     *
     * @param x The x location of the tool.
     * @return {@link Optional} containing the tool if there is one at the location, an empty optional otherwise.
     */
    private Optional<Element> getToolAt(int x) {
        int locationInTools = Math.floorMod(origin + x, Math.max(width - 2, tools.size()));

        if (locationInTools >= tools.size()) {
            return Optional.empty();
        } else {
            return Optional.of(tools.get(locationInTools));
        }
    }

    /**
     * Remove all elements from this element.
     */
    public void clear() {
        tools.clear();
    }
}
