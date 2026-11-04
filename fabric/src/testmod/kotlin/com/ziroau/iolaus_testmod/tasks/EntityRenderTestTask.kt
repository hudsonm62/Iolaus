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

package com.ziroau.iolaus_testmod.tasks

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import com.teamresourceful.resourcefullib.common.codecs.predicates.RestrictedEntityPredicate
import com.ziroau.iolaus.api.data.BasicQuestElementDisplay
import com.ziroau.iolaus.api.registry.QuestTypesFactory
import com.ziroau.iolaus.api.tasks.BaseQuestTask
import com.ziroau.iolaus_testmod.IolausTestFabric
import earth.terrarium.heracles.api.tasks.QuestTaskType
import earth.terrarium.heracles.api.tasks.storage.defaults.IntegerTaskStorage
import net.minecraft.nbt.AbstractNbtNumber
import net.minecraft.util.Identifier

class EntityRenderTestTask (
    override val taskId: String,
    override val display: BasicQuestElementDisplay,
    val entity: RestrictedEntityPredicate,
    val alphabet: String = "a"
) : BaseQuestTask<Any, AbstractNbtNumber, EntityRenderTestTask> {
    override fun test(type: QuestTaskType<*>, progress: AbstractNbtNumber, input: Any): AbstractNbtNumber = progress
    override fun getProgress(progress: AbstractNbtNumber): Float = 0f
    override fun storage(): IntegerTaskStorage = IntegerTaskStorage.INSTANCE

    override fun type(): QuestTaskType<EntityRenderTestTask> = TYPE
    companion object {
        @JvmField
        val TYPE: QuestTaskType<EntityRenderTestTask> =
            QuestTypesFactory.task(
                Identifier.of(IolausTestFabric.MOD_ID, "entityrendertest_task")
            ) { id ->
                RecordCodecBuilder.mapCodec { instance ->
                    instance.group(
                        RecordCodecBuilder.point(id),

                        BasicQuestElementDisplay.CODEC
                            .forGetter(EntityRenderTestTask::display),

                        RestrictedEntityPredicate.CODEC
                            .fieldOf("entity")
                            .forGetter(EntityRenderTestTask::entity),

                        Codec.STRING
                            .optionalFieldOf("alphabet", "a")
                            .forGetter(EntityRenderTestTask::alphabet)

                    ).apply(instance, ::EntityRenderTestTask)
                }
            }
    }
}
