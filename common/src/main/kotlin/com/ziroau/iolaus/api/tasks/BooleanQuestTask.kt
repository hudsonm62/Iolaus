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

import com.ziroau.iolaus.api.tasks.storage.ByteTaskStorage
import earth.terrarium.heracles.api.tasks.QuestTaskType
import net.minecraft.nbt.NbtByte

/**
 * Simple yes/no/true/false quest task.
 *
 * Doesn't provide any "counting" or other tracking,
 * just a simple boolean check and then stores the result as an [NbtByte].
 */
interface BooleanQuestTask<T : BooleanQuestTask<T>> : BaseQuestTask<Boolean, NbtByte, T> {
    /**
     * Determines if the given input matches the task's requirements.
     * If true, the task is considered complete; if false, it is not.
     */
    fun matches(input: Boolean): Boolean = input
    override fun test(type: QuestTaskType<*>, progress: NbtByte, input: Boolean): NbtByte {
        if (!matches(input)) return progress
        return NbtByte.of(1)
    }

    override fun getProgress(progress: NbtByte): Float = if (progress.byteValue().toInt() != 0) 1f else 0f
    override fun storage(): ByteTaskStorage = ByteTaskStorage.INSTANCE
}
