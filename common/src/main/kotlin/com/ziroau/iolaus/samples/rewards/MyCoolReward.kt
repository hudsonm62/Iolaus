package com.ziroau.iolaus.samples.rewards

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import com.ziroau.iolaus.api.data.QuestElementDisplay
import com.ziroau.iolaus.api.registry.QuestTypesFactory
import com.ziroau.iolaus.api.rewards.BaseQuestReward
import earth.terrarium.heracles.api.rewards.QuestRewardType
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier
import java.util.stream.Stream

class MyCoolReward(
    override val rewardId: String,
    override val display: QuestElementDisplay,
    val someConfigProperty: String
) : BaseQuestReward<MyCoolReward> {
    // `reward()` defines the behaviour when the reward is granted
    override fun reward(player: ServerPlayerEntity): Stream<ItemStack?> {
        println("Reward '$rewardId' with value '$someConfigProperty' has been granted :)")

        // Heracles expects a stream, despite the reward not necessarily producing items
        return Stream.empty()
    }

    // Necessary `QuestRewardType` Boilerplate
    override fun type(): QuestRewardType<MyCoolReward> = TYPE
    companion object {
        @JvmField
        val TYPE: QuestRewardType<MyCoolReward> =
            QuestTypesFactory.reward(
                Identifier.of("samplemod", "mycoolreward_id")
            ) { id ->
                RecordCodecBuilder.mapCodec { instance ->
                    instance.group(
                        RecordCodecBuilder.point<MyCoolReward, String>(id),

                        QuestElementDisplay.CODEC
                            .forGetter(MyCoolReward::display),

                        Codec.STRING
                            .optionalFieldOf("someConfigProperty", "")
                            .forGetter(MyCoolReward::someConfigProperty)

                    ).apply(instance, ::MyCoolReward)
                }
            }
    }
}
