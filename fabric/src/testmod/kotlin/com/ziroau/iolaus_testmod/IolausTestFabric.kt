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

package com.ziroau.iolaus_testmod

import com.ziroau.iolaus.api.registry.RewardsRegistry
import com.ziroau.iolaus.api.registry.TasksRegistry
import com.ziroau.iolaus_testmod.rewards.CommandsListTestReward
import com.ziroau.iolaus_testmod.rewards.EnumTestReward
import com.ziroau.iolaus_testmod.tasks.BoolTestTask
import com.ziroau.iolaus_testmod.tasks.EntityRenderTestTask
import com.ziroau.iolaus_testmod.tasks.ExtendedDisplayTestTask
import com.ziroau.iolaus_testmod.tasks.LongStorageTestTask
import net.fabricmc.api.ModInitializer

class IolausTestFabric : ModInitializer {
    companion object {
        const val MOD_ID = "iolaus_testmod"
    }
    override fun onInitialize() {
        TasksRegistry.registerAll(
            EntityRenderTestTask.TYPE,
            LongStorageTestTask.TYPE,
            BoolTestTask.TYPE,
            ExtendedDisplayTestTask.TYPE
        )
        RewardsRegistry.registerAll(
            EnumTestReward.TYPE,
            CommandsListTestReward.TYPE
        )
        IolausTestCommands.register()
    }
}
