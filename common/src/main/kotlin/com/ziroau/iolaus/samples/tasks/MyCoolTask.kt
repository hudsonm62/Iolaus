package com.ziroau.iolaus.samples.tasks

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import com.ziroau.iolaus.api.data.QuestElementDisplay
import com.ziroau.iolaus.api.registry.QuestTypesFactory
import com.ziroau.iolaus.api.tasks.CountedQuestTask
import com.ziroau.iolaus.samples.data.MyCoolInputData
import earth.terrarium.heracles.api.tasks.QuestTaskType
import net.minecraft.util.Identifier

class MyCoolTask (
    override val taskId: String,
    override val display: QuestElementDisplay,
    val someConfigProperty: String,
    override val amount: Int = 1 // required times to complete the task
) : CountedQuestTask<MyCoolInputData, MyCoolTask> {
    // Implement your matching logic here
    override fun matches(input: MyCoolInputData): Boolean {
        // everytime `input` matches the criteria for this task, return true
        return input.someProperty == someConfigProperty
    }

    // Necessary `QuestTaskType` Boilerplate below
    override fun type(): QuestTaskType<MyCoolTask> = TYPE
    companion object {
        @JvmField
        val TYPE: QuestTaskType<MyCoolTask> =
            QuestTypesFactory.task(
                Identifier.of("samplemod", "mycooltask_id")
            ) { id ->
                RecordCodecBuilder.mapCodec { instance ->
                    instance.group(
                        RecordCodecBuilder.point<MyCoolTask, String>(id),

                        QuestElementDisplay.CODEC
                            .forGetter(MyCoolTask::display),

                        Codec.STRING
                            .optionalFieldOf("someConfigProperty", "")
                            .forGetter(MyCoolTask::someConfigProperty),

                        Codec.INT
                            .optionalFieldOf("amount", 1)
                            .forGetter(MyCoolTask::amount)

                    ).apply(instance, ::MyCoolTask)
                }
            }
    }
}
