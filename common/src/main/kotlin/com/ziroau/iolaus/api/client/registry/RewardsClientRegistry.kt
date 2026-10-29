package com.ziroau.iolaus.api.client.registry

import earth.terrarium.heracles.api.client.settings.SettingInitializer
import earth.terrarium.heracles.api.client.settings.Settings
import earth.terrarium.heracles.api.rewards.QuestReward
import earth.terrarium.heracles.api.rewards.QuestRewardType
import earth.terrarium.heracles.api.rewards.client.QuestRewardWidgetFactory
import earth.terrarium.heracles.api.rewards.client.QuestRewardWidgets

/**
 * Functions to quickly register custom quest rewards and the accompanying components to Heracles
 *
 * Everything here must be called during client mod initialization.
 */
object RewardsClientRegistry {
    /**
     * Registers all client components for a quest reward.
     * This is probably the method you want to use when registering your quest reward.
     *
     * @param type Your defined [QuestRewardType]
     * @param settings The initializer for your rewards settings screen in the quest editor.
     * @param widget The display for your reward in the quest viewer - This is what players will see.
     */
    @JvmStatic
    fun <T : QuestReward<T>> register(
        type: QuestRewardType<T>,
        settings: SettingInitializer<T>,
        widget: QuestRewardWidgetFactory<T>
    ) {
        registerSettings(type, settings)
        registerWidget(type, widget)
    }

    /**
     * Register only the reward's settings screen.
     *
     * @param type Your defined [QuestRewardType]
     * @param settings The initializer for your rewards settings screen in the quest editor.
     */
    @JvmStatic
    fun <T : QuestReward<T>> registerSettings(
        type: QuestRewardType<T>,
        settings: SettingInitializer<T>
    ) {
        Settings.register(type, settings)
    }

    /**
     * Register only the reward's display widget
     *
     * @param type Your defined [QuestRewardType]
     * @param widget The display for your reward in the quest viewer - This is what players will see.
     */
    @JvmStatic
    fun <T : QuestReward<T>> registerWidget(
        type: QuestRewardType<T>,
        widget: QuestRewardWidgetFactory<T>
    ) {
        QuestRewardWidgets.register(type, widget)
    }
}
