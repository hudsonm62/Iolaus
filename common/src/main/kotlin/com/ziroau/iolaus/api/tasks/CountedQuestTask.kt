package com.ziroau.iolaus.api.tasks

import earth.terrarium.heracles.api.tasks.QuestTask
import earth.terrarium.heracles.api.tasks.QuestTaskType
import earth.terrarium.heracles.api.tasks.storage.defaults.IntegerTaskStorage
import net.minecraft.nbt.AbstractNbtNumber

/**
 * An extension of [BaseQuestTask] that handles the boilerplate for progressing a task that has a "countable goal",
 * such as "Collect 10x apples" or "Defeat 5x enemies".
 *
 * @param I The type of input this task processes (e.g., an event or action)
 * @param T This type of the subclass of task itself, extending [QuestTask]
 *
 * @sample com.ziroau.iolaus.samples.tasks.MyCoolTask
 */
interface CountedQuestTask<I, T : CountedQuestTask<I, T>> : BaseQuestTask<I, AbstractNbtNumber, T>  {
    val amount: Int

    /**
     * Determines if the given input matches the task's requirements, and increments progress by 1 if it does.
     *
     * If you need to increment (or set/decrement) by a different amount, either override [test]
     * or use [AccumulatingQuestTask.contribution] instead.
     *
     * @param input The input to check against the task's requirements, as defined by the implementation of this method
     * @return true if the input matches the requirements, therefore incrementing progress, false otherwise
     * @see AccumulatingQuestTask
     * @see test
     */
    fun matches(input: I): Boolean

    override fun test(
        type: QuestTaskType<*>, progress: AbstractNbtNumber, input: I
    ): AbstractNbtNumber {
        // If the input doesn't match the task's requirements, return the current progress without modification
        if (!matches(input)) return progress

        // Increment the progress by 1, but do not exceed the target amount
        val target = amount.coerceAtLeast(1)
        val current = storage().readInt(progress)
        return storage().set(
            (current + 1).coerceAtMost(target)
        )
    }

    override fun getProgress(progress: AbstractNbtNumber): Float {
        val target = amount.coerceAtLeast(1)
        return (storage().readInt(progress).toFloat() / target)
            .coerceIn(0f, 1f)
    }

    override fun storage(): IntegerTaskStorage = IntegerTaskStorage.INSTANCE
}
