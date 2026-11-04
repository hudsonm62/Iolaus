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

package com.ziroau.iolaus_testmod.client.widgets

import com.ziroau.iolaus.api.client.widgets.AbstractProgressTaskWidget
import com.ziroau.iolaus.api.client.widgets.renderers.EntityIconRenderer
import com.ziroau.iolaus.api.client.widgets.renderers.WidgetIconRenderer
import com.ziroau.iolaus_testmod.tasks.EntityRenderTestTask
import earth.terrarium.heracles.api.quests.QuestIcon
import earth.terrarium.heracles.common.handlers.progress.TaskProgress
import net.minecraft.nbt.AbstractNbtNumber
import net.minecraft.text.Text

/**
 * A test widget for rendering an entity in a task or reward
 */
class EntityRenderTestTaskWidget (
    override val task: EntityRenderTestTask,
    override val progress: TaskProgress<AbstractNbtNumber>
) : AbstractProgressTaskWidget<Any, AbstractNbtNumber, EntityRenderTestTask>() {

    override val iconRenderer: WidgetIconRenderer = EntityIconRenderer.of(task.entity)

    /**
     * Takes precedence over the [iconRenderer] if present
     */
    override val widgetItemIcon: QuestIcon<*>
        get() = task.icon()

    val entityTranslatedName: Text = Text.translatable(task.entity.entityType().translationKey)
    override val widgetTitle: Text
        get() = task.titleOr(entityTranslatedName.copy().append(" Render Test Task"))
    override val widgetDescription: Text
        get() = task.descriptionOr(Text.literal("This is a test task for rendering entities in widgets."))
    override val iconTooltip = listOf(Text.literal("Entity:"), entityTranslatedName)
}
