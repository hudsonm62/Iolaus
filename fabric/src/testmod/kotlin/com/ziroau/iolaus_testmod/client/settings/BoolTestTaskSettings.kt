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
