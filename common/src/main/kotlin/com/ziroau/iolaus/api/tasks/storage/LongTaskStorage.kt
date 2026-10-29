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
