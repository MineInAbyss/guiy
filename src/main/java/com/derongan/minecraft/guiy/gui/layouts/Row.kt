package com.derongan.minecraft.guiy.gui.layouts

import com.derongan.minecraft.guiy.gui.Element

class Row : ListLayout() {
    override fun calculateDrawLocations(): Map<Pair<Int, Int>, Element> {
        var xPosition = 0
        return children.associateBy { child ->
            Pair(xPosition, 0).also { xPosition += child.dims.width }
        }
    }
}