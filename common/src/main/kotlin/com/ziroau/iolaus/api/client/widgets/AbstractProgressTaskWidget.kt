package com.ziroau.iolaus.api.client.widgets

import earth.terrarium.heracles.api.client.WidgetUtils
import earth.terrarium.heracles.api.client.theme.QuestScreenTheme
import earth.terrarium.heracles.api.tasks.QuestTask
import earth.terrarium.heracles.common.handlers.progress.TaskProgress
import net.minecraft.client.gui.DrawContext
import net.minecraft.nbt.AbstractNbtNumber

/**
 *  A widget that displays a progress bar for a specific quest task
 *
 *  @param I The type of the input for the quest task
 *  @param S NBT number type used for tracking progress.
 *  @param T The type of the quest task.
 */
abstract class AbstractProgressTaskWidget<I, S : AbstractNbtNumber, T : QuestTask<I, S, T>> : AbstractDisplayWidget() {
    abstract val task: T

    abstract val progress: TaskProgress<S>

    override val titleColor: Int
        get() = QuestScreenTheme.getTaskTitle()

    override val descriptionColor: Int
        get() = QuestScreenTheme.getTaskDescription()

    override fun renderExtra(graphics: DrawContext, x: Int, y: Int, width: Int, mouseX: Int, mouseY: Int) {
        WidgetUtils.drawProgressText(graphics, x, y, width, task, progress)
        WidgetUtils.drawProgressBar(
            graphics,
            textX(x),
            y + getHeight(width) - font.fontHeight - 5,
            x + width - 5,
            y + getHeight(width) - 6,
            task, progress
        )
    }
}
