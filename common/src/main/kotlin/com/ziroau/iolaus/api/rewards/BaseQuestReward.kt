package com.ziroau.iolaus.api.rewards

import com.ziroau.iolaus.api.data.CustomizableQuestElementExtended
import earth.terrarium.heracles.api.rewards.QuestReward

/**
 * An extension of [QuestReward] that enforces common behaviour and adds some optional functionality.
 * Implements [CustomizableQuestElementExtended], so you don't need to implement that interface separately for your reward.
 *
 * @param T The type of the extended quest reward, which must extend [BaseQuestReward].
 *
 * @sample com.ziroau.iolaus.samples.rewards.MyCoolReward
 */
interface BaseQuestReward<T : BaseQuestReward<T>> : QuestReward<T>, CustomizableQuestElementExtended {
    val rewardId: String
    override fun id(): String = rewardId
}
