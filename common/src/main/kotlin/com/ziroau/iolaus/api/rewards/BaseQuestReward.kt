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

package com.ziroau.iolaus.api.rewards

import com.ziroau.iolaus.api.data.CustomizableQuestElementExtended
import earth.terrarium.heracles.api.rewards.QuestReward

/**
 * An extension of [QuestReward] that enforces common behaviour and adds some optional functionality.
 * Implements [CustomizableQuestElementExtended], so you don't need to implement that interface separately for your reward.
 *
 * @param T The type of the extended quest reward, which must extend [BaseQuestReward].
 */
interface BaseQuestReward<T : BaseQuestReward<T>> : QuestReward<T>, CustomizableQuestElementExtended {
    val rewardId: String
    override fun id(): String = rewardId
}
