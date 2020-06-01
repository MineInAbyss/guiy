package com.derongan.minecraft.guiy.gui.layouts

import com.derongan.minecraft.guiy.gui.Element
import com.derongan.minecraft.guiy.gui.GuiHolder
import com.derongan.minecraft.guiy.gui.Layout
import com.derongan.minecraft.guiy.gui.elements.ClickableElement
import com.derongan.minecraft.guiy.helpers.toCell
import de.erethon.headlib.HeadLib
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.*

@RequiresOptIn(level = RequiresOptIn.Level.WARNING, message = "This is an experimental feature in Guiy designed for Kotlin. It may be changed in the future without notice.")
annotation class GuiyExperimentalKotlinAPI

@GuiyExperimentalKotlinAPI
abstract class HistoryGuiHolder @JvmOverloads constructor(numRows: Int, title: String, plugin: Plugin, initial: Element = Layout()) : GuiHolder(numRows, title, plugin, initial) {
    private var player: Player? = null
    private val history: MutableList<Layout> = ArrayList()
    private val backButton: ClickableElement

    private fun addBackButton(layout: Layout) {
        history.add(layout)
        layout.setElement(8, 5, backButton)
    }

    fun backInHistory() {
        if (history.size <= 1 && player != null) {
            player!!.closeInventory()
            return
        }
        //TODO change to removeLast when that's no longer marked experimental
        history.removeAt(history.size - 1)
        setElement(history.last())
        render()
    }

    override fun show(player: Player) {
        setElement(root)
        super.show(player)
        this.player = player
    }

    /** Sets the element, then adds a back button if it's a layout*/
    override fun setElement(element: Element) {
        super.setElement(element)
        if (element is Layout && history.lastOrNull() != element)
            addBackButton(element)
    }

    init {
        //create back button
        val cell = HeadLib.RED_X.toCell("Back")
        backButton = ClickableElement(cell) { backInHistory() }
    }
}