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
