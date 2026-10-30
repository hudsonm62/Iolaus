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
}
