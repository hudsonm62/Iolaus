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
