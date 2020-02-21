package com.derongan.minecraft.guiy.gui.layouts

import com.derongan.minecraft.guiy.gui.ClickableElement
import com.derongan.minecraft.guiy.gui.GuiHolder
import com.derongan.minecraft.guiy.gui.Layout
import com.derongan.minecraft.guiy.helpers.toCell
import de.erethon.headlib.HeadLib
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.*

open class HistoryGuiHolder(numRows: Int, title: String?, plugin: Plugin?) : GuiHolder(numRows, title, Layout(), plugin) {
    private var player: Player? = null
    private val history: MutableList<Layout> = ArrayList()
    val backButton: ClickableElement

    fun addBackButton(layout: Layout) {
        history.add(layout)
        layout.setElement(8, 5, backButton)
    }

    fun backInHistory() {
        if (history.size <= 1 && player != null) {
            player!!.closeInventory()
            return
        }
        val previous = history[history.size - 2]
        history.removeAt(history.size - 1)
        setElement(previous)
    }

    override fun show(player: Player) {
        super.show(player)
        this.player = player
    }

    init {
        //create back button
        val cell = HeadLib.RED_X.toCell("Back")
        backButton = ClickableElement(cell) { backInHistory() }
    }
}