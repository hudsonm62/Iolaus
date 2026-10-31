package com.ziroau.iolaus_testmod.tasks

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import com.ziroau.iolaus.api.data.QuestElementDisplay
import com.ziroau.iolaus.api.registry.QuestTypesFactory
import com.ziroau.iolaus.api.tasks.AccumulatingQuestTask
import com.ziroau.iolaus_testmod.IolausTestFabric.Companion.MOD_ID
import earth.terrarium.heracles.api.tasks.QuestTaskType
import net.minecraft.util.Identifier

class LongStorageTestTask(
    override val taskId: String,
    override val amount: Long = 100L,
    override val display: QuestElementDisplay
) : AccumulatingQuestTask<Long, LongStorageTestTask> {
    override fun contribution(input: Long): Long = input

    override fun type(): QuestTaskType<LongStorageTestTask> = TYPE
    companion object {
        @JvmField
        val TYPE: QuestTaskType<LongStorageTestTask> =
            QuestTypesFactory.task(
                Identifier.of(MOD_ID, "longstorage_test_task")
            ) { id ->
                RecordCodecBuilder.mapCodec { instance ->
                    instance.group(
                        RecordCodecBuilder.point(id),

                        Codec.LONG
                            .fieldOf("amount")
                            .forGetter(LongStorageTestTask::amount),

                        QuestElementDisplay.CODEC
                            .forGetter(LongStorageTestTask::display),

                    ).apply(instance, ::LongStorageTestTask)
                }
            }
    }
}
