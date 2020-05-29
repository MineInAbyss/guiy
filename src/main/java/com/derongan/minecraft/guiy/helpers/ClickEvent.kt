package com.derongan.minecraft.guiy.helpers

import com.derongan.minecraft.guiy.GuiListener
import com.derongan.minecraft.guiy.gui.ClickEvent
import org.bukkit.plugin.Plugin

fun ClickEvent.offset(x: Int = this.x, y: Int = this.y): ClickEvent = ClickEvent.offsetClickEvent(this, x, y)

fun Plugin.registerGuiyListener() = server.pluginManager.registerEvents(GuiListener(this), this)