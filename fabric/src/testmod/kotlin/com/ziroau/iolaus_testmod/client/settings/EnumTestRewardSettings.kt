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
import com.ziroau.iolaus.api.client.settings.enumSetting
import com.ziroau.iolaus_testmod.data.enums.TestEnum1
import com.ziroau.iolaus_testmod.data.enums.TestEnum2
import com.ziroau.iolaus_testmod.rewards.EnumTestReward
import earth.terrarium.heracles.api.client.settings.SettingInitializer

class EnumTestRewardSettings : AbstractSettingInitializer<EnumTestReward>() {
    companion object {
        @JvmField
        val INSTANCE = EnumTestRewardSettings()
    }

    override fun create(task: EnumTestReward?): SettingInitializer.CreationData {
        val settings = SettingInitializer.CreationData()
        settings.put("enum1", enumSetting(TestEnum1.VALUE1), task?.enum1 ?: TestEnum1.VALUE1)
        settings.put("enum2", enumSetting(TestEnum2.VALUE3), task?.enum2 ?: TestEnum2.VALUE3)
        settings.putDisplaySettings(task)
        return settings
    }
    override fun create(
        id: String, task: EnumTestReward?, data: SettingInitializer.Data
    ): EnumTestReward {
        return EnumTestReward(
            id,
            data.get("enum1", enumSetting(TestEnum1.VALUE1)).orElse(TestEnum1.VALUE1),
            data.get("enum2", enumSetting(TestEnum2.VALUE3)).orElse(TestEnum2.VALUE3),
            data.getDisplaySettings(task)
        )
    }
}
