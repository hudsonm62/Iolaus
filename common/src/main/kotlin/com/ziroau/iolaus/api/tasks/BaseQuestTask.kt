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

package com.ziroau.iolaus.api.tasks

import com.ziroau.iolaus.api.data.CustomizableQuestElementExtended
import earth.terrarium.heracles.api.tasks.QuestTask
import net.minecraft.nbt.NbtElement

/**
 * An extension of [QuestTask] that enforces common behaviour and adds some optional functionality.
 * Implements [CustomizableQuestElementExtended], so you don't need to implement that interface separately for your task.
 *
 * @param I The type of the input data for the task.
 * @param S The type of the NBT element used for serialization of the task progress.
 * @param T The type of the extended quest task, which must extend [BaseQuestTask].
 */
interface BaseQuestTask<I, S : NbtElement, T : BaseQuestTask<I, S, T>> : QuestTask<I, S, T>,
    CustomizableQuestElementExtended {
    val taskId: String
    override fun id(): String = taskId
}
