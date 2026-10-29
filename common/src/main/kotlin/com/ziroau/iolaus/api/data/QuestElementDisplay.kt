package com.ziroau.iolaus.api.data

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import earth.terrarium.heracles.api.quests.QuestIcon
import earth.terrarium.heracles.api.quests.QuestIcons
import earth.terrarium.heracles.api.quests.defaults.ItemQuestIcon

/**
 * Represents the appearance of quest tasks/rewards, such as its title, description, and icon.
 *
 * Use the [codec] method to set default values for serialization. Otherwise, just use the [CODEC] property.
 *
 * @property titleOverride The title of the quest. If empty, the default title will be used.
 * @property descriptionOverride The description of the quest. If empty, the default description will be used.
 * @property iconOverride The icon representing the quest.
 */
open class QuestElementDisplay(
    val titleOverride: String = "",
    val descriptionOverride: String = "",
    val iconOverride: QuestIcon<*> = ItemQuestIcon.AIR
) {
    companion object {
        fun codec(defaults: QuestElementDisplay = QuestElementDisplay()) : MapCodec<QuestElementDisplay> =
            RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    Codec.STRING
                        .optionalFieldOf("title", defaults.titleOverride)
                        .forGetter(QuestElementDisplay::titleOverride),

                    Codec.STRING
                        .optionalFieldOf("description", defaults.descriptionOverride)
                        .forGetter(QuestElementDisplay::descriptionOverride),

                    QuestIcons.CODEC
                        .lenientOptionalFieldOf("icon", defaults.iconOverride)
                        .forGetter(QuestElementDisplay::iconOverride)
                ).apply(instance, ::QuestElementDisplay)
            }

        val CODEC: MapCodec<QuestElementDisplay> = codec()
    }
}
