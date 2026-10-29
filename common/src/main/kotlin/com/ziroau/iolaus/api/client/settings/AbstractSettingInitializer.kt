package com.ziroau.iolaus.api.client.settings

import com.ziroau.iolaus.api.client.settings.base.LongSetting
import com.ziroau.iolaus.api.client.settings.base.StringListSetting
import com.ziroau.iolaus.api.data.CustomizableQuestElementExtended
import com.ziroau.iolaus.api.data.QuestElementDisplay
import earth.terrarium.heracles.api.client.settings.SettingInitializer
import earth.terrarium.heracles.api.client.settings.SettingInitializer.CreationData
import earth.terrarium.heracles.api.client.settings.SettingInitializer.Data
import earth.terrarium.heracles.api.client.settings.base.BooleanSetting
import earth.terrarium.heracles.api.client.settings.base.DoubleSetting
import earth.terrarium.heracles.api.client.settings.base.IntSetting
import earth.terrarium.heracles.api.client.settings.base.QuestIconSetting
import earth.terrarium.heracles.api.client.settings.base.TextSetting
import earth.terrarium.heracles.api.quests.defaults.ItemQuestIcon

/**
 * Provides common data methods for creation of Task/Reward Settings.
 *
 * Most of the methods here are just optional property wrappers for the [SettingInitializer] methods,
 * but there are also some methods required for working with [CustomizableQuestElementExtended] and [QuestElementDisplay].
 */
abstract class AbstractSettingInitializer<T> : SettingInitializer<T> {
    protected fun textSetting(): TextSetting<String> = TextSetting.INSTANCE

    protected fun intSetting(): IntSetting = IntSetting.ONE
    protected fun intZeroSetting(): IntSetting = IntSetting.ZERO

    protected fun longSetting(): LongSetting = LongSetting.ONE
    protected fun longZeroSetting(): LongSetting = LongSetting.ZERO

    protected fun boolTrueSetting(): BooleanSetting = BooleanSetting.TRUE
    protected fun boolFalseSetting(): BooleanSetting = BooleanSetting.FALSE

    protected fun doubleSetting(): DoubleSetting = DoubleSetting.INSTANCE

    protected fun stringListSetting(): StringListSetting = StringListSetting

    protected fun questIconSetting(): QuestIconSetting = QuestIconSetting.INSTANCE

    /**
     * Helper method to add the common display settings to a [CreationData] object.
     * @param element The [CustomizableQuestElementExtended] to get the common display data from.
     * @param defaults The default [QuestElementDisplay] to use if the element is null.
     */
    protected fun SettingInitializer.CreationData.putDisplaySettings(
        element: CustomizableQuestElementExtended?, defaults: QuestElementDisplay = QuestElementDisplay()
    ) {
        put("title", textSetting(),
            element?.title() ?: defaults.titleOverride)

        put("description", textSetting(),
            element?.description() ?: defaults.descriptionOverride)

        put("icon", questIconSetting(),
            (element?.icon() as? ItemQuestIcon)
                ?: (defaults.iconOverride as? ItemQuestIcon)
                ?: ItemQuestIcon.AIR)
    }

    /**
     * Helper method to get the common element display data from a [Data] object.
     * @param element The [CustomizableQuestElementExtended] to get the common display data from.
     * @param defaults The default [QuestElementDisplay] to use if the element is null.
     */
    protected fun SettingInitializer.Data.getDisplaySettings(
        element: CustomizableQuestElementExtended?, defaults: QuestElementDisplay = QuestElementDisplay()
    ): QuestElementDisplay {
        return QuestElementDisplay(
            titleOverride = get("title", textSetting())
                .orElse(element?.title() ?: defaults.titleOverride),

            descriptionOverride = get("description", textSetting())
                .orElse(element?.description() ?: defaults.descriptionOverride),

            iconOverride = get("icon", questIconSetting())
                .orElse(
                    (element?.icon() as? ItemQuestIcon)
                        ?: (defaults.iconOverride as? ItemQuestIcon)
                        ?: ItemQuestIcon.AIR
                )
        )
    }
}
