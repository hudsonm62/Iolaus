package com.ziroau.iolaus.api.registry

import com.mojang.serialization.MapCodec
import earth.terrarium.heracles.api.rewards.QuestReward
import earth.terrarium.heracles.api.rewards.QuestRewardType
import earth.terrarium.heracles.api.tasks.QuestTask
import earth.terrarium.heracles.api.tasks.QuestTaskType
import net.minecraft.util.Identifier

object QuestTypesFactory {
    fun <T : QuestTask<*, *, T>> task(
        typeId: Identifier,
        codecFactory: (String) -> MapCodec<T>
    ): QuestTaskType<T> = object : QuestTaskType<T> {

        override fun id(): Identifier = typeId

        override fun codec(id: String): MapCodec<T> =
            codecFactory(id)
    }

    fun <T : QuestReward<T>> reward(
        typeId: Identifier,
        codecFactory: (String) -> MapCodec<T>
    ): QuestRewardType<T> = object : QuestRewardType<T> {

        override fun id(): Identifier = typeId

        override fun codec(id: String): MapCodec<T> =
            codecFactory(id)
    }
}
