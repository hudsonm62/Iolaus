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
