package com.ziroau.iolaus_testmod.client.settings

import com.teamresourceful.resourcefullib.common.codecs.predicates.RestrictedEntityPredicate
import com.ziroau.iolaus.api.client.settings.AbstractSettingInitializer
import com.ziroau.iolaus_testmod.tasks.EntityRenderTestTask
import earth.terrarium.heracles.api.client.settings.SettingInitializer
import earth.terrarium.heracles.api.client.settings.base.RegistrySetting
import net.minecraft.entity.EntityType
import java.util.Optional

class EntityRenderTestTaskSettings : AbstractSettingInitializer<EntityRenderTestTask>() {
    companion object {
        @JvmField
        val INSTANCE = EntityRenderTestTaskSettings()
    }

    override fun create(task: EntityRenderTestTask?): SettingInitializer.CreationData {
        val settings = SettingInitializer.CreationData()
        settings.putDisplaySettings(task)
        settings.put("entity", RegistrySetting.ENTITY, getDefaultEntity(task))
        return settings
    }
    override fun create(
        id: String,
        task: EntityRenderTestTask?,
        data: SettingInitializer.Data
    ): EntityRenderTestTask {
        val entityType = data.get("entity", RegistrySetting.ENTITY)
            .orElse(getDefaultEntity(task))

        /**
         * Creates a new [RestrictedEntityPredicate] using the entity type
         */
        val old = task?.entity
        val entity = RestrictedEntityPredicate(
            entityType,
            old?.location() ?: Optional.empty(),
            old?.effects() ?: Optional.empty(),
            old?.nbt() ?: Optional.empty(),
            old?.flags() ?: Optional.empty(),
            old?.targetedEntity() ?: Optional.empty()
        )
        return EntityRenderTestTask(
            id,
            data.getDisplaySettings(task),
            entity
        )
    }

    private fun getDefaultEntity(task: EntityRenderTestTask?): EntityType<*> = task?.entity?.entityType() ?: EntityType.ZOMBIE
}
