package com.ziroau.iolaus.api.client.settings.textbox

import earth.terrarium.heracles.client.components.widgets.textbox.editor.MultiLineTextBox
import earth.terrarium.heracles.client.components.widgets.textbox.editor.TextHighlighter
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext

/**
 * Extension of [MultiLineTextBox] with a border for Settings screens.
 */
class BorderedMultiLineTextBox(
    old: MultiLineTextBox?, font: TextRenderer,
    width: Int, height: Int,
    highlighter: TextHighlighter,
    var fillColour: Long = 0xFF202020,
    var borderColour: Long = 0xFF555555,
    var borderColourFocused: Long = 0xFFFFFFFF
) : MultiLineTextBox(old, font, width, height, highlighter) {
    override fun drawBox(context: DrawContext) {
        context.fill(
            x, y,
            x + width, y + height,
            fillColour.toInt()
        )

        val borderColourResult = if (isFocused)
            borderColourFocused
        else
            borderColour

        context.drawBorder(x, y, width, height, borderColourResult.toInt())
    }
}
