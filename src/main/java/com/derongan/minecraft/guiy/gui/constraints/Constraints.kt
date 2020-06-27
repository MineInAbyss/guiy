package com.derongan.minecraft.guiy.gui.constraints

import com.derongan.minecraft.guiy.gui.properties.scroll.ScrollAlignment

class Constraints {
    val padding: Padding = Padding()
    val margin: Padding = TODO()
    var alignment: ScrollAlignment = ScrollAlignment.TOP
}

class Dims {
    var width = 0
    var height = 0

}

class Padding {
    var left = 0
    var right = 0
    var top = 0
    var bottom = 0
}