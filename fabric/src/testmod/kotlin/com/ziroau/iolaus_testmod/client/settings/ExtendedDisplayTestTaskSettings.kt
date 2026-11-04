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
import com.ziroau.iolaus.api.data.BasicQuestElementDisplay
import com.ziroau.iolaus_testmod.data.TestExtendedQuestElementDisplay
import com.ziroau.iolaus_testmod.tasks.ExtendedDisplayTestTask
import earth.terrarium.heracles.api.client.settings.SettingInitializer

class ExtendedDisplayTestTaskSettings :
    AbstractSettingInitializer<ExtendedDisplayTestTask>() {

    companion object {
        @JvmField
        val INSTANCE = ExtendedDisplayTestTaskSettings()
    }

    override fun create(
        task: ExtendedDisplayTestTask?
    ): SettingInitializer.CreationData {
        val settings = SettingInitializer.CreationData()
        settings.putDisplaySettings(task, task?.display?.base ?: BasicQuestElementDisplay())
        /** Extended setting provided by [TestExtendedQuestElementDisplay] */
        settings.put("test_entry", textSetting(), task?.display?.testEntry ?: "")

        settings.put("amount", longSetting(), task?.amount ?: 100L)
        return settings
    }

    override fun create(
        id: String,
        task: ExtendedDisplayTestTask?,
        data: SettingInitializer.Data
    ): ExtendedDisplayTestTask {
        val base = data.getDisplaySettings(task, task?.display?.base ?: BasicQuestElementDisplay())
        val display = TestExtendedQuestElementDisplay(
            base, data.get("test_entry", textSetting()).orElse(task?.display?.testEntry ?: "")
        )

        return ExtendedDisplayTestTask(
            id,
            data.get("amount", longSetting())
                .orElse(task?.amount ?: 100L),
            display
        )
    }
}
