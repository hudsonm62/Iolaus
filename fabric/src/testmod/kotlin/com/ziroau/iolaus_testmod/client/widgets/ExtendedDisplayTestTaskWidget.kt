package com.ziroau.iolaus_testmod.client.widgets

import com.ziroau.iolaus.api.client.widgets.AbstractProgressTaskWidget
import com.ziroau.iolaus_testmod.tasks.ExtendedDisplayTestTask
import earth.terrarium.heracles.api.quests.QuestIcon
import earth.terrarium.heracles.common.handlers.progress.TaskProgress
import net.minecraft.nbt.AbstractNbtNumber
import net.minecraft.text.Text

class ExtendedDisplayTestTaskWidget(
    override val task: ExtendedDisplayTestTask,
    override val progress: TaskProgress<AbstractNbtNumber>
) : AbstractProgressTaskWidget<Long, AbstractNbtNumber, ExtendedDisplayTestTask>() {
    override val widgetItemIcon: QuestIcon<*>
        get() = task.icon()

    override val widgetTitle: Text
        get() = task.titleOr(Text.literal("Extended Display Test Task"))

    override val widgetDescription: Text
        get() = task.descriptionOr(Text.literal("Custom Element: ${task.testEntry()}"))
}
