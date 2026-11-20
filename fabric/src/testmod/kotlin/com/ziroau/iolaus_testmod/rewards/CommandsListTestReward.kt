/*
 * Copyright (c) 2026 Hudson M.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ziroau.iolaus_testmod.rewards

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import com.ziroau.iolaus.api.data.BasicQuestElementDisplay
import com.ziroau.iolaus.api.registry.QuestTypesFactory
import com.ziroau.iolaus.api.rewards.BaseQuestReward
import com.ziroau.iolaus_testmod.IolausTestFabric.Companion.MOD_ID
import earth.terrarium.heracles.api.rewards.QuestRewardType
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier
import java.util.stream.Stream

class CommandsListTestReward(
    override val rewardId: String,
    val commands: List<String>,
    val permission: Int = 4,
    override val display: BasicQuestElementDisplay
) : BaseQuestReward<CommandsListTestReward> {
    override fun reward(player: ServerPlayerEntity): Stream<ItemStack?> {
        /** Sample List:
         * - "say Hello world!",
         * - "give @s minecraft:diamond 5",
         * - "effect give @s minecraft:speed 30 1"
         */
        val source = player.commandSource.withLevel(permission)
        val commandManager = player.server.commandManager
        commands.forEach { commandManager.executeWithPrefix(source, it) }
        return Stream.empty()
    }

    override fun type(): QuestRewardType<CommandsListTestReward> = TYPE
    companion object {
        @JvmField
        val TYPE: QuestRewardType<CommandsListTestReward> =
            QuestTypesFactory.reward(
                Identifier.of(MOD_ID, "commandslisttest_reward")
            ) { id ->
                RecordCodecBuilder.mapCodec { instance ->
                    instance.group(
                        RecordCodecBuilder.point(id),

                        Codec.STRING.listOf()
                            .optionalFieldOf("commands", emptyList<String>())
                            .forGetter(CommandsListTestReward::commands),

                        Codec.INT
                            .optionalFieldOf("permission", 4)
                            .forGetter(CommandsListTestReward::permission),

                        BasicQuestElementDisplay.CODEC
                            .forGetter(CommandsListTestReward::display)

                        ).apply(instance, ::CommandsListTestReward)
                }
            }
    }
}
