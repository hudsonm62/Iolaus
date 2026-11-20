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
import net.minecraft.nbt.NbtByte
import net.minecraft.nbt.NbtElement

class ByteTaskStorage : TaskStorage<Byte, NbtByte> {
    private fun clamp(value: Int): Byte =
        value.coerceIn(Byte.MIN_VALUE.toInt(), Byte.MAX_VALUE.toInt()).toByte()

    companion object {
        @JvmField
        val INSTANCE = ByteTaskStorage()
    }

    fun readByte(tag: NbtElement): Byte =
        if (tag is AbstractNbtNumber) tag.byteValue() else 0.toByte()

    override fun createDefault(): NbtByte = NbtByte.of(0)

    override fun read(tag: NbtByte): Byte = tag.byteValue()

    override fun same(tag1: NbtElement, tag2: NbtElement): Boolean =
        readByte(tag1) == readByte(tag2)

    fun add(progress: NbtByte, amount: Byte): NbtByte =
        NbtByte.of(clamp(readByte(progress).toInt() + amount.toInt()))

    fun set(value: Byte): NbtByte = NbtByte.of(value)

    fun max(progress: NbtByte, amount: Byte): NbtByte =
        NbtByte.of(readByte(progress).coerceAtLeast(amount))
}
