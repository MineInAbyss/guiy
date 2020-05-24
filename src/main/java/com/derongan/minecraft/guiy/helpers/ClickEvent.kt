package com.derongan.minecraft.guiy.helpers

import com.derongan.minecraft.guiy.gui.ClickEvent

fun ClickEvent.offset(x: Int, y: Int): ClickEvent = ClickEvent.offsetClickEvent(this, x, y)