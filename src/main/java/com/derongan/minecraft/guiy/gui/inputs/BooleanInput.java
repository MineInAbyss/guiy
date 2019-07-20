package com.derongan.minecraft.guiy.gui.inputs;

import com.derongan.minecraft.guiy.gui.ClickEvent;
import com.derongan.minecraft.guiy.gui.Element;
import com.derongan.minecraft.guiy.gui.GuiRenderer;
import com.derongan.minecraft.guiy.gui.Size;
import de.erethon.headlib.HeadLib;

import java.util.function.Consumer;

/**
 * Input that produces a boolean result. This input is always a 2x1 element.
 */
public class BooleanInput implements Element, Input<Boolean> {
    private Boolean selected = null;
    private Consumer<Boolean> consumer;


    public BooleanInput(Boolean initial) {
        this.selected = initial;
    }

    @Override
    public Size getSize() {
        return Size.create(2, 1);
    }

    @Override
    public void draw(GuiRenderer guiRenderer) {
        guiRenderer.renderAt(0, 0, HeadLib.CHECKMARK.toItemStack("True"));
        guiRenderer.renderAt(1, 0, HeadLib.RED_X.toItemStack("False"));
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        if (clickEvent.getX() == 0 && clickEvent.getY() == 0) {
            selected = true;
        } else if (clickEvent.getX() == 1 && clickEvent.getY() == 0) {
            selected = false;
        }
    }

    @Override
    public Boolean getResult() {
        return selected;
    }

    @Override
    public void setSubmitAction(Consumer<Boolean> consumer) {
        this.consumer = consumer;
    }

    @Override
    public Consumer<Boolean> getSubmitAction() {
        return consumer;
    }
}
