package com.ziroau.iolaus_testmod.client.settings

import com.ziroau.iolaus.api.client.settings.AbstractSettingInitializer
import com.ziroau.iolaus_testmod.tasks.BoolTestTask
import earth.terrarium.heracles.api.client.settings.SettingInitializer

class BoolTestTaskSettings : AbstractSettingInitializer<BoolTestTask>() {
    companion object {
        @JvmField
        val INSTANCE = BoolTestTaskSettings()
    }

    override fun create(task: BoolTestTask?): SettingInitializer.CreationData {
        val settings = SettingInitializer.CreationData()
        settings.putDisplaySettings(task)
        return settings
    }

    override fun create(
        id: String, task: BoolTestTask?, data: SettingInitializer.Data
    ): BoolTestTask {
        return BoolTestTask(
            id,
            data.getDisplaySettings(task)
        )
    }

}
