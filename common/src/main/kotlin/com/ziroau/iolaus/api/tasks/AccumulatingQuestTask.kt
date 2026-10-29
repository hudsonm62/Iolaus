package com.ziroau.iolaus.api.tasks

import com.ziroau.iolaus.api.tasks.storage.LongTaskStorage
import earth.terrarium.heracles.api.tasks.QuestTaskType
import net.minecraft.nbt.AbstractNbtNumber
import kotlin.math.nextDown

/**
 * An extension of [BaseQuestTask] that handles the boilerplate for progressing a task that has a "total number goal",
 * such as "Deal 50 Damage" or "Generate 1000 Energy" where counting in increments of 1 is not sufficient.
 *
 * This is different from CountedQuestTask, in that each input can contribute a variable amount towards the task's target.
 * It also supports negative contributions & [Long] values.
 *
 * @param I The type of input this task processes (e.g., an event or action)
 * @param T This type of the subclass of task itself, extending from [earth.terrarium.heracles.api.tasks.QuestTask]
 *
 * @see CountedQuestTask
 * @see LongTaskStorage
 */
interface AccumulatingQuestTask<I, T : AccumulatingQuestTask<I, T>> : BaseQuestTask<I, AbstractNbtNumber, T> {
    val amount: Long

    /**
     * Returns how much progress this input contributes.
     *
     * Negative contributions are supported.
     * Implementations handle task-specific clamping.
     */
    fun contribution(input: I): Long

    override fun test(
        type: QuestTaskType<*>,
        progress: AbstractNbtNumber,
        input: I
    ): AbstractNbtNumber {
        val current = storage().readLong(progress)
        val delta = contribution(input)

        // Only guard against Long overflow
        val updated = when {
            delta > 0 && current > Long.MAX_VALUE - delta ->
                Long.MAX_VALUE

            delta < 0 && current < Long.MIN_VALUE - delta ->
                Long.MIN_VALUE

            else -> current + delta
        }

        return storage().set(updated)
    }

    override fun getProgress(progress: AbstractNbtNumber): Float {
        // Need to handle floating point precision issues here :/
        // For now, using `nextDown()` to ensure that we don't return 1.0f when the progress is not actually complete
        if (amount == 0L) return 0f
        val current = storage().readLong(progress)
        val ratio = (current.toDouble() / amount.toDouble()).toFloat()
        return if (current < amount) minOf(ratio, 1f.nextDown()) else ratio
    }

    override fun storage(): LongTaskStorage = LongTaskStorage.INSTANCE
}
