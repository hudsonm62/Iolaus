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

package com.ziroau.iolaus_testmod.client.settings

import com.ziroau.iolaus.api.client.settings.AbstractSettingInitializer
import com.ziroau.iolaus_testmod.rewards.CommandsListTestReward
import earth.terrarium.heracles.api.client.settings.SettingInitializer

class CommandsListTestRewardSettings : AbstractSettingInitializer<CommandsListTestReward>() {
    companion object {
        @JvmField
        val INSTANCE = CommandsListTestRewardSettings()
    }

    override fun create(reward: CommandsListTestReward?): SettingInitializer.CreationData {
        val settings = SettingInitializer.CreationData()
        settings.put("commands", stringListSetting(), reward?.commands ?: emptyList())
        settings.put("permission", intSetting(), reward?.permission ?: 4)
        settings.putDisplaySettings(reward)
        return settings
    }
    override fun create(
        id: String, reward: CommandsListTestReward?, data: SettingInitializer.Data
    ): CommandsListTestReward {
        return CommandsListTestReward(
            id,
            data.get("commands", stringListSetting()).orElse(reward?.commands ?: emptyList()),
            data.get("permission", intSetting()).orElse(reward?.permission ?: 4),
            data.getDisplaySettings(reward)
        )
    }
}
