package com.ziroau.iolaus.api.client.widgets

import earth.terrarium.heracles.api.client.theme.QuestScreenTheme
import earth.terrarium.heracles.api.tasks.QuestTask
import earth.terrarium.heracles.common.handlers.progress.TaskProgress
import net.minecraft.client.gui.DrawContext
import net.minecraft.nbt.NbtByte
import net.minecraft.text.Text

/**
 * A widget that displays the completion state of a byte-backed quest task.
 *
 * This works with any [QuestTask] whose progress storage type is NbtByte.
 *
 * @param I The input type of the quest task (typically a [Boolean] or [Byte])
 * @param T The quest task type.
 *
 * @see com.ziroau.iolaus.api.tasks.BooleanQuestTask
 * @see com.ziroau.iolaus.api.tasks.storage.ByteTaskStorage
 */
abstract class AbstractBooleanTaskWidget<I, T : QuestTask<I, NbtByte, T>> : AbstractDisplayWidget() {
    abstract val task: T

    abstract val progress: TaskProgress<NbtByte>

    override val titleColor: Int
        get() = QuestScreenTheme.getTaskTitle()

    override val descriptionColor: Int
        get() = QuestScreenTheme.getTaskDescription()

    protected open fun getStatusText(completed: Boolean): Text =
        Text.literal(if (completed) "✔ " else "✘ ")
            .append(
                Text.translatable(
                    if (completed) "gui.iolaus.widget.complete"
                    else "gui.iolaus.widget.incomplete"
                )
            )

    protected open fun getStatusColor(completed: Boolean): Int =
        if (completed) 0x55FF55 else 0xFF5555

    override fun renderExtra(
        graphics: DrawContext,
        x: Int, y: Int,
        width: Int,
        mouseX: Int, mouseY: Int
    ) {
        val completed = progress.isComplete
        val text = getStatusText(completed)
        graphics.drawText(
            font,
            text,
            x + width - 5 - font.getWidth(text),
            y + 6,
            getStatusColor(completed),
            false
        )
    }
}
