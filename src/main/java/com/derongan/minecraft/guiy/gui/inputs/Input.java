package com.derongan.minecraft.guiy.gui.inputs;

import java.util.function.Consumer;

/**
 * An input that produces a value
 *
 * @param <E> The type of value this input produces
 */
public interface Input<E> {
    /**
     * Gets the result currently stored in this input. Inputs are fully mutable,
     * so this result may change.
     *
     * @return The result of this input
     */
    E getResult();

    /**
     * Sets the callback that will be run when this Input is submitted.
     *
     * @param consumer The callback
     */
    void setSubmitAction(Consumer<E> consumer);

    /**
     * Gets the callback that will be run when this Input is submitted.
     */
    Consumer<E> getSubmitAction();

    /**
     * Submit the information stored in this Input. In most cases this calls the submit action callback.
     */
    default void onSubmit() {
        getSubmitAction().accept(getResult());
    }
}
