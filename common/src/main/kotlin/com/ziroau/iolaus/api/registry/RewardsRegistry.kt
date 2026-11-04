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

package com.ziroau.iolaus.api.registry

import earth.terrarium.heracles.api.rewards.QuestReward
import earth.terrarium.heracles.api.rewards.QuestRewardType
import earth.terrarium.heracles.api.rewards.QuestRewards

/**
 * Functions to quickly register custom quest rewards to Heracles
 *
 * Everything here must be called during mod initialization.
 */
object RewardsRegistry {
    /**
     * Register a custom quest reward type.
     */
    @JvmStatic
    fun <T : QuestReward<T>> register(type: QuestRewardType<T>) {
        QuestRewards.register(type)
    }

    /**
     * Register multiple quest reward types at once.
     */
    @JvmStatic
    fun registerAll(vararg types: QuestRewardType<*>) {
        types.forEach { type ->
            QuestRewards.register(type)
        }
    }
}
