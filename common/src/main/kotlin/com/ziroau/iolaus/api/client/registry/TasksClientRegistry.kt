package com.ziroau.iolaus.api.client.registry

import earth.terrarium.heracles.api.client.settings.SettingInitializer
import earth.terrarium.heracles.api.client.settings.Settings
import earth.terrarium.heracles.api.tasks.QuestTask
import earth.terrarium.heracles.api.tasks.QuestTaskDisplayFormatter
import earth.terrarium.heracles.api.tasks.QuestTaskType
import earth.terrarium.heracles.api.tasks.client.QuestTaskWidgets
import net.minecraft.nbt.NbtElement

/**
 * Functions to quickly register custom quest tasks and the accompanying components to Heracles
 *
 * Everything here must be called during client mod initialization.
 */
object TasksClientRegistry {
    /**
     * Registers all client components for a quest task.
     * This is probably the method you want to use when registering your quest task.
     *
     * @param type Your defined [QuestTaskType]
     * @param settings The initializer for your task's settings screen in the quest editor.
     * @param widget The display for your task in the quest viewer - This is what players will see.
     * @param formatter The formatter for your task's progress display in the quest viewer
     */
    @JvmStatic
    fun <I, S : NbtElement, T : QuestTask<I, S, T>> register(
        type: QuestTaskType<T>,
        settings: SettingInitializer<T>,
        widget: QuestTaskWidgets.QuestTaskWidgetSimpleFactory<I, S, T>,
        formatter: QuestTaskDisplayFormatter.Formatter<I, S, T>
    ) {
        registerSettings(type, settings)
        registerWidget(type, widget)
        registerFormatter(type, formatter)
    }

    /**
     * Register only the task's settings screen.
     *
     * @param type Your defined [QuestTaskType]
     * @param settings The initializer for your task's settings screen in the quest editor.
     */
    @JvmStatic
    fun <I, S : NbtElement, T : QuestTask<I, S, T>> registerSettings(
        type: QuestTaskType<T>,
        settings: SettingInitializer<T>
    ) {
        Settings.register(type, settings)
    }

    /**
     * Register only the task's display widget
     *
     * @param type Your defined [QuestTaskType]
     * @param widget The display for your task in the quest viewer - This is what players will see.
     */
    @JvmStatic
    fun <I, S : NbtElement, T : QuestTask<I, S, T>> registerWidget(
        type: QuestTaskType<T>,
        widget: QuestTaskWidgets.QuestTaskWidgetSimpleFactory<I, S, T>
    ) {
        QuestTaskWidgets.registerSimple(type, widget)
    }

    /**
     * Register only the task's progress display formatter
     *
     * @param type Your defined [QuestTaskType]
     * @param formatter The formatter for your task's progress display in the quest viewer
     */
    @JvmStatic
    fun <I, S : NbtElement, T : QuestTask<I, S, T>> registerFormatter(
        type: QuestTaskType<T>,
        formatter: QuestTaskDisplayFormatter.Formatter<I, S, T>
    ) {
        QuestTaskDisplayFormatter.register(type, formatter)
    }
}