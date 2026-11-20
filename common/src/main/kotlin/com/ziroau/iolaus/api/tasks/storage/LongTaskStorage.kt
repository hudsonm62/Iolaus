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

package com.ziroau.iolaus.api.tasks.storage

import earth.terrarium.heracles.api.tasks.storage.TaskStorage
import net.minecraft.nbt.AbstractNbtNumber
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtLong

class LongTaskStorage : TaskStorage<Long, AbstractNbtNumber> {
    companion object {
        @JvmField
        val INSTANCE = LongTaskStorage()
    }

    fun readLong(tag: NbtElement): Long = if (tag is AbstractNbtNumber) tag.longValue() else 0L

    override fun createDefault(): AbstractNbtNumber = NbtLong.of(0L)

    override fun read(tag: AbstractNbtNumber): Long = tag.longValue()

    override fun same(tag1: NbtElement, tag2: NbtElement): Boolean = readLong(tag1) == readLong(tag2)

    fun add(progress: AbstractNbtNumber, amount: Long): NbtLong = NbtLong.of(readLong(progress) + amount)

    fun set(value: Long): NbtLong = NbtLong.of(value)

    fun max(progress: AbstractNbtNumber, amount: Long): NbtLong = NbtLong.of(readLong(progress).coerceAtLeast(amount))
}
