package com.ziroau.iolaus_testmod.tasks

import com.mojang.serialization.codecs.RecordCodecBuilder
import com.teamresourceful.resourcefullib.common.codecs.predicates.RestrictedEntityPredicate
import com.ziroau.iolaus.api.data.QuestElementDisplay
import com.ziroau.iolaus.api.registry.QuestTypesFactory
import com.ziroau.iolaus.api.tasks.BaseQuestTask
import com.ziroau.iolaus_testmod.IolausTestFabric
import earth.terrarium.heracles.api.tasks.QuestTaskType
import earth.terrarium.heracles.api.tasks.storage.defaults.IntegerTaskStorage
import net.minecraft.nbt.AbstractNbtNumber
import net.minecraft.util.Identifier

class EntityRenderTestTask (
    override val taskId: String,
    override val display: QuestElementDisplay,
    val entity: RestrictedEntityPredicate
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
                        RecordCodecBuilder.point<EntityRenderTestTask, String>(id),

                        QuestElementDisplay.CODEC
                            .forGetter(EntityRenderTestTask::display),

                        RestrictedEntityPredicate.CODEC
                            .fieldOf("entity")
                            .forGetter(EntityRenderTestTask::entity),

                    ).apply(instance, ::EntityRenderTestTask)
                }
            }
    }
}
