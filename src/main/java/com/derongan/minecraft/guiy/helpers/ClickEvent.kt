package com.derongan.minecraft.guiy.helpers

import com.derongan.minecraft.guiy.gui.ClickEvent

fun ClickEvent.offset(x: Int = this.x, y: Int = this.y): ClickEvent = ClickEvent.offsetClickEvent(this, x, y)