package com.ziroau.iolaus.api.registry

import earth.terrarium.heracles.api.rewards.QuestReward
import earth.terrarium.heracles.api.rewards.QuestRewardType
import earth.terrarium.heracles.api.rewards.QuestRewards

/**
 * Functions to quickly register custom quest rewards to Heracles
 *
 * Everything here must be called during mod initialization.
 */
object RewardsRegistry {
    /**
     * Register a custom quest reward type.
     */
    @JvmStatic
    fun <T : QuestReward<T>> register(type: QuestRewardType<T>) {
        QuestRewards.register(type)
    }

    /**
     * Register multiple quest reward types at once.
     */
    @JvmStatic
    fun registerAll(vararg types: QuestRewardType<*>) {
        types.forEach { type ->
            QuestRewards.register(type)
        }
    }
}
