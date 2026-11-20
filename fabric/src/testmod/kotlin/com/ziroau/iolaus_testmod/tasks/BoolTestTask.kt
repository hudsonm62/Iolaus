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

import com.mojang.serialization.codecs.RecordCodecBuilder
import com.ziroau.iolaus.api.data.BasicQuestElementDisplay
import com.ziroau.iolaus.api.registry.QuestTypesFactory
import com.ziroau.iolaus.api.tasks.BooleanQuestTask
import com.ziroau.iolaus_testmod.IolausTestFabric
import earth.terrarium.heracles.api.tasks.QuestTaskType
import net.minecraft.util.Identifier

class BoolTestTask(
    override val taskId: String,
    override val display: BasicQuestElementDisplay
) : BooleanQuestTask<BoolTestTask> {
    override fun type(): QuestTaskType<BoolTestTask> = TYPE
    companion object {
        @JvmField
        val TYPE: QuestTaskType<BoolTestTask> =
            QuestTypesFactory.task(
                Identifier.of(IolausTestFabric.MOD_ID, "booltest_task")
            ) { id ->
                RecordCodecBuilder.mapCodec { instance ->
                    instance.group(
                        RecordCodecBuilder.point(id),

                        BasicQuestElementDisplay.CODEC
                            .forGetter(BoolTestTask::display),
                    ).apply(instance, ::BoolTestTask)
                }
            }
    }
}
