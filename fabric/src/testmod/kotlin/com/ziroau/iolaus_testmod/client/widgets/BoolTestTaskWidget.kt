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

import com.ziroau.iolaus.api.client.widgets.AbstractBooleanTaskWidget
import com.ziroau.iolaus.api.client.widgets.renderers.PlayerHeadIconRenderer
import com.ziroau.iolaus.api.client.widgets.renderers.utility.PlayerHeadRenderMode.THREE_DIMENSIONAL
import com.ziroau.iolaus_testmod.tasks.BoolTestTask
import earth.terrarium.heracles.api.quests.QuestIcon
import earth.terrarium.heracles.common.handlers.progress.TaskProgress
import net.minecraft.client.MinecraftClient
import net.minecraft.nbt.NbtByte
import net.minecraft.text.Text

class BoolTestTaskWidget(
    override val task: BoolTestTask,
    override val progress: TaskProgress<NbtByte>
) : AbstractBooleanTaskWidget<Boolean, BoolTestTask>() {
    override val widgetItemIcon: QuestIcon<*>
        get() = task.icon()

    override val widgetTitle: Text
        get() = task.titleOr(Text.literal("Boolean Test Task"))

    override val widgetDescription: Text
        get() = task.descriptionOr(Text.literal("This is a test task for byte storage with Booleans"))

    //val viewingPlayerProfile = MinecraftClient.getInstance().player?.gameProfile
    //override val iconRenderer = if(viewingPlayerProfile != null) {
    //    PlayerHeadIconRenderer.of(viewingPlayerProfile, THREE_DIMENSIONAL)
    //} else { null }

    // Find Base64 textures on Head Databases
    /// Or encode your own skin image to Base64 and use that instead!
    override val iconRenderer = PlayerHeadIconRenderer
        .ofTexture(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzMzZGNmYjRkYTEwMTc3MjY0OTY4YjQ0OWU3MjRhZGViZWUzYmMzM2I3MmJhZTg1ODQyYjRhYWI5YmQ5YzRkYiJ9fX0=",
            THREE_DIMENSIONAL
        )
}
