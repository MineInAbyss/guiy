@file:JvmName("Cells")

package com.derongan.minecraft.guiy.helpers

import com.derongan.minecraft.guiy.gui.Cell
import com.derongan.minecraft.guiy.gui.Element
import de.erethon.headlib.HeadLib
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

fun HeadLib.toCell(): Element = Cell.forItemStack(this.toItemStack())
fun HeadLib.toCell(name: String): Element = Cell.forItemStack(this.toItemStack(name))

fun ItemStack.toCell(): Element = Cell.forItemStack(this)
fun ItemStack.toCell(name: String): Element = Cell.forItemStack(this, name)

fun Material.toCell(): Element = Cell.forMaterial(this)
fun Material.toCell(name: String): Element = Cell.forMaterial(this, name)