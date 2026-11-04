/*
 * Copyright (c) 2026 Hudson M.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ziroau.iolaus_testmod.client.widgets

import com.ziroau.iolaus.api.client.widgets.AbstractRewardWidget
import com.ziroau.iolaus_testmod.rewards.EnumTestReward
import earth.terrarium.heracles.api.quests.QuestIcon
import earth.terrarium.heracles.common.handlers.progress.QuestProgress
import net.minecraft.text.Text

class EnumTestRewardWidget private constructor(
    override val reward: EnumTestReward,
    override val questId: String,
    override val progress: QuestProgress?,
    override val interactive: Boolean
) : AbstractRewardWidget() {
    companion object {
        @JvmStatic
        fun of(reward: EnumTestReward, interactive: Boolean): EnumTestRewardWidget {
            val (questId, progress) = getQuestContext()
            return EnumTestRewardWidget(
                reward, questId, progress, interactive
            )
        }
    }

    override val widgetItemIcon: QuestIcon<*>
        get() = reward.icon()

    override val widgetTitle: Text
        get() = reward.titleOr(
            Text.literal("Test Enum Reward Widget")
        )

    override val widgetDescription: Text
        get() = reward.descriptionOr(
            Text.literal("Enum1: ${reward.enum1}, Enum2: ${reward.enum2}")
        )
}
