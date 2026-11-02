package com.ziroau.iolaus.api.data

import earth.terrarium.heracles.api.quests.QuestIcon

/**
 * Represents the display properties of a quest task or reward, such as its title,
 * description, and icon.
 *
 * Use [BasicQuestElementDisplay] for standard display properties. Implement this
 * interface with additional properties when you need to store mod-specific
 * display data. Implementations should provide their own codecs for serializing additional properties.
 *
 * @property titleOverride The title to display. If empty, the default title is used.
 * @property descriptionOverride The description to display. If empty, the default description is used.
 * @property iconOverride The icon used to represent this quest element.
 */
interface QuestElementDisplay {
    val titleOverride: String
    val descriptionOverride: String
    val iconOverride: QuestIcon<*>
}
