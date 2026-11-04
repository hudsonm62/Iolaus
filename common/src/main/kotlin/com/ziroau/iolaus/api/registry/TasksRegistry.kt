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

import earth.terrarium.heracles.api.tasks.QuestTask
import earth.terrarium.heracles.api.tasks.QuestTaskType
import earth.terrarium.heracles.api.tasks.QuestTasks
import net.minecraft.nbt.NbtElement

/**
 * Functions to quickly register custom quest tasks to Heracles
 *
 * Everything here must be called during mod initialization.
 */
object TasksRegistry {

    /**
     * Register a custom quest task type
     */
    @JvmStatic
    fun <I, S : NbtElement, T : QuestTask<I, S, T>> register(type: QuestTaskType<T>) {
        QuestTasks.register(type)
    }

    /**
     * Register multiple quest task types at once
     */
    @JvmStatic
    fun registerAll(vararg types: QuestTaskType<*>) {
        types.forEach { type ->
            QuestTasks.register(type)
        }
    }
}
