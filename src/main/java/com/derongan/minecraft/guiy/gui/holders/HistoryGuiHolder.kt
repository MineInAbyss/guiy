package com.derongan.minecraft.guiy.gui.holders

import com.derongan.minecraft.guiy.gui.Element
import com.derongan.minecraft.guiy.gui.elements.ClickableElement
import com.derongan.minecraft.guiy.gui.layouts.GridLayout
import com.derongan.minecraft.guiy.helpers.toCell
import de.erethon.headlib.HeadLib
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.*

/**
 * A version of [GuiHolder] that adds a back button to any layouts set inside of it. Pressing the back button will go
 * to the previous layout, or close the GUI if the last is reached. There is no way to move forward in history, and
 * currently no size limit to the history.
 */
abstract class HistoryGuiHolder @JvmOverloads constructor(
        numRows: Int,
        title: String,
        plugin: Plugin,
        initial: Element = GridLayout()
) : GuiHolder(numRows, title, plugin, initial) {
    private var player: Player? = null //TODO clean up if player should be immutable
    private val history: MutableList<GridLayout> = ArrayList()
    private val backButton: ClickableElement = ClickableElement(HeadLib.RED_X.toCell("Back")) { backInHistory() }

    /** Adds the [backButton] to the specified [layout] and adds the layout into [history] */
    private fun addBackButton(layout: GridLayout) {
        history.add(layout)
        layout.setElement(8, 5, backButton) //TODO a way of customizing where the back button goes
    }

    /** Goes to the previous [GridLayout] the player visited */
    fun backInHistory() {
        if (history.size <= 1 && player != null) {
            player?.closeInventory()
            return
        }
        //TODO change to removeLast when that's no longer marked experimental
        history.removeAt(history.size - 1)
        setElement(history.last())
        render()
    }

    override fun show(player: Player) {
        this.player = player
        super.show(player)
    }

    /** Sets the element, then adds a back button if it's a layout */
    override fun setElement(element: Element) {
        super.setElement(element)
        if (element is GridLayout && history.lastOrNull() != element)
            addBackButton(element)
    }
}