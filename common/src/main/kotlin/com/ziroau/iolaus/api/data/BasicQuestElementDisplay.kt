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

package com.ziroau.iolaus.api.data

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import earth.terrarium.heracles.api.quests.QuestIcon
import earth.terrarium.heracles.api.quests.QuestIcons
import earth.terrarium.heracles.api.quests.defaults.ItemQuestIcon

/**
 * A basic implementation of [QuestElementDisplay] that could be used for most tasks and rewards.
 *
 * If you need _more_ display settings, you should consider implementing your own [QuestElementDisplay] instead of using this class.
 *
 * @param titleOverride The title to override the default quest element title.
 * @param descriptionOverride The description to override the default quest element description.
 * @param iconOverride The item icon to override the default quest element icon.
 *
 * @see QuestElementDisplay
 * @see CustomizableQuestElementExtended
 */
data class BasicQuestElementDisplay(
    override val titleOverride: String = "",
    override val descriptionOverride: String = "",
    override val iconOverride: QuestIcon<*> = ItemQuestIcon.AIR
) : QuestElementDisplay {
    companion object {
        fun codec(
            defaults: BasicQuestElementDisplay = BasicQuestElementDisplay()
        ): MapCodec<BasicQuestElementDisplay> =
            RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    Codec.STRING
                        .optionalFieldOf("title", defaults.titleOverride)
                        .forGetter(BasicQuestElementDisplay::titleOverride),

                    Codec.STRING
                        .optionalFieldOf("description", defaults.descriptionOverride)
                        .forGetter(BasicQuestElementDisplay::descriptionOverride),

                    QuestIcons.CODEC
                        .lenientOptionalFieldOf("icon", defaults.iconOverride)
                        .forGetter(BasicQuestElementDisplay::iconOverride)
                ).apply(instance, ::BasicQuestElementDisplay)
            }

        val CODEC = codec()
    }
}
