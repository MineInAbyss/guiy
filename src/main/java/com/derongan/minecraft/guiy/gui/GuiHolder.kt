package com.derongan.minecraft.guiy.gui

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

/**
 * An [InventoryHolder] with extra functionality for rendering Guiy Elements and Layouts
 */
open class GuiHolder @JvmOverloads constructor(private val numRows: Int, title: String, val plugin: Plugin, private var initial: Element = Layout()) : InventoryHolder {
    open val root: Element = initial

    private val inventory: Inventory = Bukkit.createInventory(this, 9 * numRows, title)
    override fun getInventory(): Inventory = inventory

    fun render() {
        inventory.clear()
        initial.draw(GuiRenderer { x: Int, y: Int, itemStack: ItemStack? ->
            if (x + 9 * y < numRows * 9) {
                if (itemStack == null) {
                    inventory.clear(x + 9 * y)
                } else if (x + 9 * y < inventory.size && x + 9 * y >= 0) {
                    inventory.setItem(x + 9 * y, itemStack)
                }
            }
        })
    }

    private fun renderFuture() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin) { render() }
    }

    fun onClick(clickEvent: ClickEvent) {
        initial.onClick(clickEvent)
        renderFuture()
    }

    open fun show(player: Player) {
        render()
        player.openInventory(inventory)
    }

    open fun setElement(element: Element) {
        initial = element
    }
}