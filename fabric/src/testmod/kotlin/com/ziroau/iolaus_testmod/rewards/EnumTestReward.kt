package com.ziroau.iolaus_testmod.rewards

import com.mojang.serialization.codecs.RecordCodecBuilder
import com.teamresourceful.resourcefullib.common.codecs.EnumCodec
import com.ziroau.iolaus.api.data.QuestElementDisplay
import com.ziroau.iolaus.api.registry.QuestTypesFactory
import com.ziroau.iolaus.api.rewards.BaseQuestReward
import com.ziroau.iolaus_testmod.IolausTestFabric.Companion.MOD_ID
import com.ziroau.iolaus_testmod.data.enums.TestEnum1
import com.ziroau.iolaus_testmod.data.enums.TestEnum2
import earth.terrarium.heracles.api.rewards.QuestRewardType
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier
import java.util.stream.Stream

class EnumTestReward(
    override val rewardId: String,
    val enum1: TestEnum1 = TestEnum1.VALUE1,
    val enum2: TestEnum2 = TestEnum2.VALUE3,
    override val display: QuestElementDisplay
) : BaseQuestReward<EnumTestReward> {
    override fun reward(player: ServerPlayerEntity): Stream<ItemStack?> {
        println("Rewarding player ${player.name.string} with enum1: $enum1 and enum2: $enum2")
        return Stream.empty()
    }

    override fun type(): QuestRewardType<EnumTestReward> = TYPE
    companion object {
        @JvmField
        val TYPE: QuestRewardType<EnumTestReward> =
            QuestTypesFactory.reward(
                Identifier.of(MOD_ID, "enumtest_reward")
            ) { id ->
                RecordCodecBuilder.mapCodec { instance ->
                    instance.group(
                        RecordCodecBuilder.point(id),

                        EnumCodec.of(TestEnum1::class.java)
                            .fieldOf("enum1")
                            .forGetter(EnumTestReward::enum1),

                        EnumCodec.of(TestEnum2::class.java)
                            .fieldOf("enum2")
                            .forGetter(EnumTestReward::enum2),

                        QuestElementDisplay
                            .CODEC.fieldOf("display")
                            .forGetter(EnumTestReward::display)
                    ).apply(instance) { rewardId, enum1, enum2, display ->
                        EnumTestReward(rewardId, enum1, enum2, display)
                    }
                }
            }
    }
}
