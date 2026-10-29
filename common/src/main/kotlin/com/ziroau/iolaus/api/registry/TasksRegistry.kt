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
