package com.ziroau.iolaus_testmod.client.settings

import com.ziroau.iolaus.api.client.settings.AbstractSettingInitializer
import com.ziroau.iolaus_testmod.tasks.LongStorageTestTask
import earth.terrarium.heracles.api.client.settings.SettingInitializer

class LongStorageTestTaskSettings : AbstractSettingInitializer<LongStorageTestTask>() {
    companion object {
        @JvmField
        val INSTANCE = LongStorageTestTaskSettings()
    }

    override fun create(task: LongStorageTestTask?): SettingInitializer.CreationData {
        val settings = SettingInitializer.CreationData()
        settings.putDisplaySettings(task)
        settings.put("amount", longSetting(), task?.amount ?: 100L)
        return settings
    }

    override fun create(
        id: String,
        task: LongStorageTestTask?,
        data: SettingInitializer.Data
    ): LongStorageTestTask {
        return LongStorageTestTask(
            id,
            data.get("amount", longSetting()).orElse(100L),
            data.getDisplaySettings(task)
        )
    }
}
