package com.derongan.minecraft.guiy.gui.layouts

import com.derongan.minecraft.guiy.gui.Element

class Column : ListLayout() {
    override fun calculateDrawLocations(): Map<Pair<Int, Int>, Element> {
        var yPosition = 0
        return children.associateBy { child ->
            Pair(0, yPosition).also { yPosition += child.dims.height }
        }
    }
}