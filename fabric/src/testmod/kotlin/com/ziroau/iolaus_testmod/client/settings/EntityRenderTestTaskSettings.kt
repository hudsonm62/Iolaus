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

package com.ziroau.iolaus_testmod.client.settings

import com.teamresourceful.resourcefullib.common.codecs.predicates.RestrictedEntityPredicate
import com.ziroau.iolaus.api.client.settings.AbstractSettingInitializer
import com.ziroau.iolaus_testmod.client.settings.base.AlphabetAutocomplete
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
        settings.put("alphabet", AlphabetAutocomplete(), task?.alphabet ?: "a")
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
            entity,
            data.get("alphabet", AlphabetAutocomplete()).orElse("a")
        )
    }

    private fun getDefaultEntity(task: EntityRenderTestTask?): EntityType<*> = task?.entity?.entityType() ?: EntityType.ZOMBIE
}
