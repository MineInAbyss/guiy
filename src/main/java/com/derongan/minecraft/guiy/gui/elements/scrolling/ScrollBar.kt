package com.derongan.minecraft.guiy.gui.elements.scrolling

import com.derongan.minecraft.guiy.gui.layouts.GridLayout
import com.derongan.minecraft.guiy.gui.properties.scroll.ScrollAlignment.*
import com.derongan.minecraft.guiy.gui.properties.scroll.ScrollType
import com.derongan.minecraft.guiy.helpers.toCell
import com.derongan.minecraft.guiy.kotlin_dsl.button
import de.erethon.headlib.HeadLib

class ScrollBar(
        val width: Int,
        val height: Int,
        val previousButtonAction: ScrollBar.() -> Unit,
        val nextButtonAction: ScrollBar.() -> Unit
) : GridLayout() {
    var prevButtonName = "Previous"
    var nextButtonName = "Next"
    var alignment = INLINE
    var scrollType = ScrollType.NONE

    init {
        val (firstArrow, secondArrow) =
                if (scrollType == ScrollType.HORIZONTAL) HeadLib.WOODEN_ARROW_LEFT to HeadLib.WOODEN_ARROW_RIGHT
                else HeadLib.WOODEN_ARROW_UP to HeadLib.WOODEN_ARROW_DOWN

        if (alignment != INLINE) {
            val (innerX, innerY, width, height) = when (alignment) {
                LEFT -> listOf(1, 0, width - 1, height)
                RIGHT -> listOf(0, 0, width - 1, height)
                TOP -> listOf(0, 1, width, height - 1)
                BOTTOM -> listOf(0, 0, width, height - 1)
                INLINE -> error("Cannot set alignment for inline list")
            }
//            editOnClick = this@ListContainable.innerList(this, width, height).at(innerX, innerY)
        }

        //decide where buttons should be positioned
        val (prevX, prevY, nextX, nextY) = when (alignment) {
            LEFT -> listOf(0, 0, 0, height - 1)
            RIGHT -> listOf(width - 1, 0, width - 1, height - 1)
            TOP -> listOf(0, 0, width - 1, 0)
            BOTTOM -> listOf(0, height - 1, width - 1, height - 1)
            INLINE -> listOf(0, 0, width - 1, height - 1)
        }

        button(firstArrow.toCell(prevButtonName)) {
            previousButtonAction()
        }.at(prevX, prevY)

        button(secondArrow.toCell(nextButtonName)) {
            nextButtonAction()
        }.at(nextX, nextY)
    }
}