package com.derongan.minecraft.guiy.gui.inputs;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Stream;

import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Convenience utility for formatting protobuf {@link Message}'s in an appealing manner to be used in Lore.
 */
public final class MessageFormater {
    /**
     * Returns a list of strings that describe the {@link Message} and are fit for showing in Lore.
     * <p>
     * Nested Messages will be indented.
     *
     * @param message The message to create lore representation for.
     */
    public static List<String> format(Message message) {
        return format(message, 0);
    }

    /**
     * Returns a list of strings that describe the {@link Message} and are fit for showing in Lore.
     * <p>
     * Nested Messages will be indented.
     *
     * @param message The message to create lore representation for.
     * @param indent  The amount to indent each line.
     */
    public static List<String> format(Message message, int indent) {
        return message.getDescriptorForType()
                .getFields()
                .stream()
                .flatMap(field -> {
                    String prefix = ChatColor.RESET + "" + ChatColor.RED + "" + ChatColor.BOLD + field.getName() + ChatColor.RESET + "" + ChatColor.WHITE + " : ";

                    if (field.getType() == Descriptors.FieldDescriptor.Type.MESSAGE) {
                        Message indented = (Message) message.getField(field);

                        return Stream.concat(Stream.of(prefix), format(indented, indent + 1).stream());
                    } else {
                        return Stream.of(prefix + ChatColor.GREEN +
                                message.getField(field));
                    }
                })
                .map(str -> {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < indent; i++) {
                        stringBuilder.append("  ");
                    }
                    return stringBuilder.append(str).toString();
                })
                .collect(toImmutableList());
    }
}
