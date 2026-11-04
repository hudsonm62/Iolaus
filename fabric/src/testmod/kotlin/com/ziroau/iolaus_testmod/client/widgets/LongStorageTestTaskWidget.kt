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
import com.ziroau.iolaus.api.client.widgets.renderers.ItemIconCarouselRenderer
import com.ziroau.iolaus_testmod.tasks.LongStorageTestTask
import earth.terrarium.heracles.api.quests.QuestIcon
import earth.terrarium.heracles.common.handlers.progress.TaskProgress
import net.minecraft.item.Items
import net.minecraft.nbt.AbstractNbtNumber
import net.minecraft.text.Text

class LongStorageTestTaskWidget(
    override val task: LongStorageTestTask,
    override val progress: TaskProgress<AbstractNbtNumber>
) : AbstractProgressTaskWidget<Long, AbstractNbtNumber, LongStorageTestTask>() {
    override val widgetItemIcon: QuestIcon<*>
        get() = task.icon()

    override val widgetTitle: Text
        get() = task.titleOr(Text.literal("Long Storage Test Task"))

    override val widgetDescription: Text
        get() = task.descriptionOr(Text.literal("This is a test task for long storage."))

    // In practice, you could allow Quest Editors to define icons to be used
    // for tasks/rewards, instead of hardcoding them like this.
    //
    // This is just a test to show how to use the ItemIconCarouselRenderer.
    override val iconRenderer = ItemIconCarouselRenderer.of(
        Items.ENDER_CHEST, Items.CHEST, Items.BARREL, Items.SHULKER_BOX
    )
}
