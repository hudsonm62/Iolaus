package com.ziroau.iolaus_testmod.client.widgets

import com.ziroau.iolaus.api.client.widgets.AbstractRewardWidget
import com.ziroau.iolaus_testmod.rewards.CommandsListTestReward
import earth.terrarium.heracles.api.quests.QuestIcon
import earth.terrarium.heracles.common.handlers.progress.QuestProgress
import net.minecraft.text.Text

class CommandsListTestRewardWidget private constructor(
    override val reward: CommandsListTestReward,
    override val questId: String,
    override val progress: QuestProgress?,
    override val interactive: Boolean
): AbstractRewardWidget() {
    companion object {
        @JvmStatic
        fun of(reward: CommandsListTestReward, interactive: Boolean): CommandsListTestRewardWidget {
            val (questId, progress) = getQuestContext()
            return CommandsListTestRewardWidget(
                reward, questId, progress, interactive
            )
        }
    }

    override val widgetItemIcon: QuestIcon<*>
        get() = reward.icon()

    override val widgetTitle: Text
        get() = reward.titleOr(Text.literal("Commands List Test Reward"))

    override val widgetDescription: Text
        get() = reward.descriptionOr(Text.literal("A test reward that executes a list of commands."))
}
