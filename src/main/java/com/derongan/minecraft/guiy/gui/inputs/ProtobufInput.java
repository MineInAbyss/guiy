package com.derongan.minecraft.guiy.gui.inputs;

import com.derongan.minecraft.guiy.gui.*;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import de.erethon.headlib.HeadLib;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.google.common.collect.ImmutableList.toImmutableList;


/**
 * Input that produces a {@link Message} result.
 * This input is multistage and uses other input types as appropriate for setting fields.
 *
 * @param <E> Type of {@link Message} to produce.
 */
//TODO figure out generics here
public class ProtobufInput<E extends Message> implements Element, Input<E> {
    private final Message.Builder builder;
    private final Map<String, List<String>> stringValueProviders;
    private Descriptors.Descriptor protoDescriptor;

    private SwappableElement swappableElement;
    private FillableElement selectFieldFillable;

    private Layout base;
    private Consumer<E> consumer = a -> {
    };

    private Input activeInput;

    /**
     * Constructs a {@link ProtobufInput}
     *
     * @param defaultInstance Default instance of the {@link Message} type being inputted. Use {@link Message#getDefaultInstanceForType()} to get this object.
     */
    public ProtobufInput(E defaultInstance) {
        this(defaultInstance, ImmutableMap.of());
    }

    /**
     * Constructs a {@link ProtobufInput} with a map that can be used to provide string values.
     * The map contains a mapping from field name to a list of strings that can be used for that field name.
     *
     * @param defaultInstance      Default instance of the {@link Message} type being inputted. Use {@link Message#getDefaultInstanceForType()} to get this object.
     * @param stringValueProviders A mapping from field name to possible field string values.
     */
    public ProtobufInput(E defaultInstance, Map<String, List<String>> stringValueProviders) {
        this.builder = defaultInstance.toBuilder();
        this.stringValueProviders = stringValueProviders;
        this.protoDescriptor = builder.getDescriptorForType();

        this.selectFieldFillable = new FillableElement(4, 4);
        this.swappableElement = new SwappableElement(selectFieldFillable);

        base = new Layout();

        base.setElement(0, 0, swappableElement);

        protoDescriptor.getFields().forEach(fieldDescriptor -> {
            ItemStack itemStack;

            ItemStack scrollUp = HeadLib.WOODEN_ARROW_UP.toItemStack("Scroll Up");
            ItemStack scrollDown = HeadLib.WOODEN_ARROW_DOWN.toItemStack("Scroll Down");

            String name = ChatColor.RESET + "" + ChatColor.BOLD + "" + ChatColor.RED + fieldDescriptor.getName() + " : " + ChatColor.GREEN + (builder
                    .getField(fieldDescriptor) != null ? builder.getField(fieldDescriptor)
                    .toString() : "?");

            switch (fieldDescriptor.getType()) {
                case MESSAGE:
                    itemStack = HeadLib.WOODEN_PLUS.toItemStack(name);
                    break;
                case BOOL:
                    itemStack = HeadLib.WOODEN_B.toItemStack(name);
                    break;
                case STRING:
                    itemStack = HeadLib.WOODEN_S.toItemStack(name);
                    break;
                case ENUM:
                    itemStack = HeadLib.WOODEN_E.toItemStack(name);
                    break;
                //TODO there are more cases of numeric
                case INT32:
                case FLOAT:
                case DOUBLE:
                case INT64:
                case SINT32:
                case SINT64:
                case UINT32:
                    itemStack = HeadLib.WOODEN_0.toItemStack(name);
                    break;
                default:
                    itemStack = HeadLib.WOODEN_QUESTION_MARK.toItemStack(name);
            }

            Element element = Cell.forItemStack(itemStack);

            ClickableElement clickableElement = new ClickableElement(element);

            if (fieldDescriptor.getType() == Descriptors.FieldDescriptor.Type.BOOL || fieldDescriptor.getType() == Descriptors.FieldDescriptor.Type.ENUM || fieldDescriptor
                    .getType() == Descriptors.FieldDescriptor.Type.DOUBLE || fieldDescriptor.getType() == Descriptors.FieldDescriptor.Type.INT64 || fieldDescriptor
                    .getType() == Descriptors.FieldDescriptor.Type.FLOAT ||
                    fieldDescriptor.getType() == Descriptors.FieldDescriptor.Type.MESSAGE || fieldDescriptor.getType() == Descriptors.FieldDescriptor.Type.STRING) {
                clickableElement.setClickAction(clickEvent -> {
                    Input input;
                    Element elem;
                    if (fieldDescriptor.getType() == Descriptors.FieldDescriptor.Type.BOOL) {
                        input = new BooleanInput((Boolean) builder.getField(fieldDescriptor));
                        elem = (Element) input;
                    } else if (fieldDescriptor.getType() == Descriptors.FieldDescriptor.Type.ENUM) {
                        Input<String> fixedInput = new FixedStringInput(8, fieldDescriptor.getEnumType()
                                .getValues()
                                .stream()
                                .map(Descriptors.EnumValueDescriptor::getName)
                                .collect(toImmutableList()));

                        input = new Input<Descriptors.EnumValueDescriptor>() {
                            private Consumer<Descriptors.EnumValueDescriptor> consumer;

                            @Override
                            public Descriptors.EnumValueDescriptor getResult() {
                                return fieldDescriptor.getEnumType()
                                        .findValueByName(fixedInput.getResult());
                            }

                            @Override
                            public void setSubmitAction(Consumer<Descriptors.EnumValueDescriptor> consumer) {
                                this.consumer = consumer;
                            }

                            @Override
                            public Consumer<Descriptors.EnumValueDescriptor> getSubmitAction() {
                                return consumer;
                            }
                        };
                        elem = new VerticalScrollingElement(5, (Element) fixedInput, scrollUp, scrollDown);
                    } else if (fieldDescriptor.getType() == Descriptors.FieldDescriptor.Type.DOUBLE || fieldDescriptor.getType() == Descriptors.FieldDescriptor.Type.INT64 || fieldDescriptor
                            .getType() == Descriptors.FieldDescriptor.Type.FLOAT) {
                        input = new NumberInput();

                        elem = (Element) input;
                    } else if (fieldDescriptor.getType() == Descriptors.FieldDescriptor.Type.STRING && stringValueProviders
                            .containsKey(fieldDescriptor.getName())) {
                        input = new FixedStringInput(8, stringValueProviders.get(fieldDescriptor.getName()));
                        elem = new VerticalScrollingElement(5, (Element) input, scrollUp, scrollDown);
                    } else {
                        input = new ProtobufInput((Message) builder.getField(fieldDescriptor), stringValueProviders);
                        elem = (Element) input;
                    }


                    activeInput = input;

                    input.setSubmitAction(result -> {
                        swappableElement.swap(selectFieldFillable);

                        boolean repeated = fieldDescriptor.isRepeated();

                        if (result == null) {
                            builder.clearField(fieldDescriptor);
                        } else if (fieldDescriptor.getType() == Descriptors.FieldDescriptor.Type.INT64) {
                            long l = new Double((double) result).longValue();
                            builder.setField(fieldDescriptor, repeated ? ImmutableList.of(l) : l);
                        } else if (fieldDescriptor.getType() == Descriptors.FieldDescriptor.Type.FLOAT) {
                            float l = new Double((double) result).floatValue();
                            builder.setField(fieldDescriptor, repeated ? ImmutableList.of(l) : l);
                        } else {
                            builder.setField(fieldDescriptor, repeated ? ImmutableList.of(result) : result);
                        }

                        ItemMeta itemMeta = itemStack.getItemMeta();

                        String name2 = ChatColor.RESET + "" + ChatColor.BOLD + "" + ChatColor.RED + fieldDescriptor.getName() + " : " + ChatColor.GREEN + (builder
                                .getField(fieldDescriptor) != null ? builder.getField(fieldDescriptor)
                                .toString() : "?");

                        if (result instanceof Message) {
                            name2 = ChatColor.RESET + "" + ChatColor.BOLD + "" + ChatColor.RED + fieldDescriptor.getName();

                            itemMeta.setLore(MessageFormater.format((Message) result));
                        }

                        itemMeta.setDisplayName(name2);

                        itemStack.setItemMeta(itemMeta);

                        activeInput = null;
                    });

                    swappableElement.swap(elem);
                });
            }

            selectFieldFillable.addElement(clickableElement);
        });
    }

    @Override
    public Size getSize() {
        return base.getSize();
    }

    @Override
    public void draw(GuiRenderer guiRenderer) {
        base.draw(guiRenderer);
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        base.onClick(clickEvent);
    }

    @Override
    public E getResult() {
        return (E) builder.build();
    }

    @Override
    public void setSubmitAction(Consumer<E> consumer) {
        this.consumer = consumer;
    }

    @Override
    public Consumer<E> getSubmitAction() {
        return consumer;
    }

    @Override
    public void onSubmit() {
        if (activeInput != null) {
            activeInput.onSubmit();
        } else {
            getSubmitAction().accept(getResult());
        }
    }
}
