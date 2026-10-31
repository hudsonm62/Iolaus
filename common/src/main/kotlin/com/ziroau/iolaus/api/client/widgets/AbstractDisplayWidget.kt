package com.ziroau.iolaus.api.client.widgets

import com.teamresourceful.resourcefullib.client.scissor.ScissorBoxStack
import com.ziroau.iolaus.api.client.widgets.renderers.WidgetIconRenderer
import earth.terrarium.heracles.api.client.DisplayWidget
import earth.terrarium.heracles.api.quests.QuestIcon
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text

/**
 * Base class for most widgets.
 * Provides logic for displaying an icon, overridden title, and description.
 */
abstract class AbstractDisplayWidget : DisplayWidget {
    companion object {
        const val ICON_SIZE = 32
        const val ROW_HEIGHT = 42
        const val TEXT_OFFSET = ICON_SIZE + 16
    }

    protected val font: TextRenderer
        get() = MinecraftClient.getInstance().textRenderer

    protected abstract val widgetItemIcon: QuestIcon<*>

    /**
     * Renders a custom icon (i.e. an [net.minecraft.entity.Entity]) if the implementing widget has one, and [widgetItemIcon] is empty/air.
     *
     * @see com.ziroau.iolaus.api.client.widgets.renderers.WidgetIconRenderer
     * @see com.ziroau.iolaus.api.client.widgets.renderers.EntityIconRenderer
     * @see com.ziroau.iolaus.api.client.widgets.renderers.ItemIconCarouselRenderer
     */
    protected open val iconRenderer: WidgetIconRenderer? = null

    protected abstract val widgetTitle: Text
    protected abstract val widgetDescription: Text

    protected abstract val titleColor: Int
    protected abstract val descriptionColor: Int

    fun textX(x: Int) = x + TEXT_OFFSET

    override fun getHeight(width: Int) = ROW_HEIGHT

    /**
     * Render additional elements specific to the subclass (e.g., progress bars, buttons)
     */
    abstract fun renderExtra(graphics: DrawContext, x: Int, y: Int, width: Int, mouseX: Int, mouseY: Int)

    override fun render(
        graphics: DrawContext,
        scissor: ScissorBoxStack,
        x: Int, y: Int, width: Int,
        mouseX: Int, mouseY: Int,
        hovered: Boolean,
        partialTicks: Float
    ) {
        val iconX = x + 5
        val iconY = y + 5
        if (!widgetItemIcon.render(
                graphics,
                iconX, iconY,
                ICON_SIZE, ICON_SIZE
        )) {
            iconRenderer?.render(graphics, scissor, iconX, iconY, ICON_SIZE)
        }

        // Title
        graphics.drawText(
            font, widgetTitle,
            textX(x), y + 6,
            titleColor, false
        )

        // Description
        graphics.drawText(
            font, widgetDescription,
            textX(x), y + 8 + font.fontHeight,
            descriptionColor, false
        )

        // Subclass-specific rendering
        renderExtra(
            graphics, x, y, width, mouseX, mouseY
        )
    }
}
