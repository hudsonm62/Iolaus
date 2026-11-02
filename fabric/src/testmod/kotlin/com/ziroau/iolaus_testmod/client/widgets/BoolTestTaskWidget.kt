package com.ziroau.iolaus_testmod.client.widgets

import com.ziroau.iolaus.api.client.widgets.AbstractBooleanTaskWidget
import com.ziroau.iolaus_testmod.tasks.BoolTestTask
import earth.terrarium.heracles.api.quests.QuestIcon
import earth.terrarium.heracles.common.handlers.progress.TaskProgress
import net.minecraft.nbt.NbtByte
import net.minecraft.text.Text

class BoolTestTaskWidget(
    override val task: BoolTestTask,
    override val progress: TaskProgress<NbtByte>
) : AbstractBooleanTaskWidget<Boolean, BoolTestTask>() {
    override val widgetItemIcon: QuestIcon<*>
        get() = task.icon()

    override val widgetTitle: Text
        get() = task.titleOr(Text.literal("Boolean Test Task"))

    override val widgetDescription: Text
        get() = task.descriptionOr(Text.literal("This is a test task for byte storage with Booleans"))
}
