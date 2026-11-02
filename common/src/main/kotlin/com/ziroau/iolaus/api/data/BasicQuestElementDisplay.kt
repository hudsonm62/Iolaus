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
