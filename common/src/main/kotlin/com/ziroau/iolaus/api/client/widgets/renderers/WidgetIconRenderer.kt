package com.ziroau.iolaus.api.client.widgets.renderers

import com.teamresourceful.resourcefullib.client.scissor.ScissorBoxStack
import net.minecraft.client.gui.DrawContext

/**
 * An Icon renderer for a widget.
 * Useful for rendering entities or other custom elements that isn't the standard item icon.
 *
 * @see EntityIconRenderer
 */
fun interface WidgetIconRenderer {
    fun render(context: DrawContext, scissor: ScissorBoxStack, x: Int, y: Int, size: Int): Boolean
}
