package com.derongan.minecraft.guiy.gui.elements.containers

import com.derongan.minecraft.guiy.GuiyKeys
import com.derongan.minecraft.guiy.gui.Cell
import com.derongan.minecraft.guiy.gui.ClickEvent
import com.derongan.minecraft.guiy.helpers.toCell
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.Plugin

/**
 * Element that acts like a normal inventory, but only accepts items with a certain TYPE_KEY NBT value.
 * Items will only be able to be placed in this container if they have a [custom tag][allowedKeys] set with a [NamespacedKey]
 * containing the provided [plugin] and the value "type".
 *
 * @param height      Height of the element
 * @param width       Width of the element
 * @param allowedKeys Set of strings allowed
 * @param plugin      Plugin the tags belong to
 */
class ContainerElement(height: Int, width: Int, private val allowedKeys: Set<String?>?, private val plugin: Plugin) : FillableElement(height, width) {
    override fun onClick(clickEvent: ClickEvent) {
        clickEvent.isCancelled = false
        if (allowedKeys == null) {
            setElement(clickEvent.x, clickEvent.y, clickEvent.itemOnCursor.toCell())
            return
        }

        if (clickEvent.itemOnCursor.type == Material.AIR) {
            removeElement(clickEvent.x, clickEvent.y)
            return
        }

        val itemOnCursor = clickEvent.itemOnCursor
        val itemMeta = itemOnCursor.itemMeta ?: { clickEvent.isCancelled = true }.let { return }

        if (!itemMeta.persistentDataContainer.has(NamespacedKey(plugin, GuiyKeys.TYPE_KEY), PersistentDataType.STRING)) {
            clickEvent.isCancelled = true
            return
        }

        val itemType = itemMeta.persistentDataContainer
                .get(NamespacedKey(plugin, GuiyKeys.TYPE_KEY), PersistentDataType.STRING)

        val allowed = allowedKeys.contains(itemType)
        if (allowed) {
            setElement(clickEvent.x, clickEvent.y, Cell.forItemStack(itemOnCursor.clone()))
        } else {
            clickEvent.isCancelled = true
        }
    }

}