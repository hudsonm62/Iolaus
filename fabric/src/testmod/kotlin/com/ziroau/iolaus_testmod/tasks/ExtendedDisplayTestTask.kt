package com.ziroau.iolaus_testmod.tasks

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import com.ziroau.iolaus.api.registry.QuestTypesFactory
import com.ziroau.iolaus.api.tasks.AccumulatingQuestTask
import com.ziroau.iolaus_testmod.IolausTestFabric.Companion.MOD_ID
import com.ziroau.iolaus_testmod.data.TestExtendedQuestElementDisplay
import earth.terrarium.heracles.api.tasks.QuestTaskType
import net.minecraft.util.Identifier

/**
 * Testing custom [QuestElementDisplay] implementations, such as [TestExtendedQuestElementDisplay].
 */
class ExtendedDisplayTestTask(
    override val taskId: String,
    override val amount: Long = 100L,
    override val display: TestExtendedQuestElementDisplay = TestExtendedQuestElementDisplay()
) : AccumulatingQuestTask<Long, ExtendedDisplayTestTask> {

    override fun contribution(input: Long): Long = input

    override fun type(): QuestTaskType<ExtendedDisplayTestTask> = TYPE

    fun testEntry(): String = display.testEntry

    companion object {
        @JvmField
        val TYPE: QuestTaskType<ExtendedDisplayTestTask> =
            QuestTypesFactory.task(
                Identifier.of(MOD_ID, "extended_display_test_task")
            ) { id ->
                RecordCodecBuilder.mapCodec { instance ->
                    instance.group(
                        RecordCodecBuilder.point(id),

                        Codec.LONG
                            .fieldOf("amount")
                            .forGetter(ExtendedDisplayTestTask::amount),

                        TestExtendedQuestElementDisplay.CODEC
                            .forGetter(ExtendedDisplayTestTask::display)

                    ).apply(instance, ::ExtendedDisplayTestTask)
                }
            }
    }
}

