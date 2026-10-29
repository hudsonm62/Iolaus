package com.ziroau.iolaus.api.client.settings

import com.ziroau.iolaus.api.client.settings.base.StringListSetting
import com.ziroau.iolaus.api.data.CustomizableQuestElementExtended
import com.ziroau.iolaus.api.data.QuestElementDisplay
import earth.terrarium.heracles.api.client.settings.Setting
import earth.terrarium.heracles.api.client.settings.SettingInitializer
import earth.terrarium.heracles.api.client.settings.SettingInitializer.CreationData
import earth.terrarium.heracles.api.client.settings.SettingInitializer.Data
import earth.terrarium.heracles.api.client.settings.base.BooleanSetting
import earth.terrarium.heracles.api.client.settings.base.DoubleSetting
import earth.terrarium.heracles.api.client.settings.base.IntSetting
import earth.terrarium.heracles.api.client.settings.base.QuestIconSetting
import earth.terrarium.heracles.api.client.settings.base.TextSetting
import earth.terrarium.heracles.api.quests.defaults.ItemQuestIcon
import net.minecraft.client.gui.widget.ClickableWidget

/**
 * Provides common data methods for creation of Task/Reward Settings.
 *
 * Most of the methods here are just optional type-safe wrappers for the [SettingInitializer] methods,
 * but there are also some methods required for working with [CustomizableQuestElementExtended] & [QuestElementDisplay].
 */
abstract class AbstractSettingInitializer<T> : SettingInitializer<T> {
    @Suppress("UNCHECKED_CAST")
    protected fun textSetting(): Setting<String, ClickableWidget> =
        TextSetting.INSTANCE as Setting<String, ClickableWidget>

    @Suppress("UNCHECKED_CAST")
    protected fun intSetting(): Setting<Int, ClickableWidget> =
        IntSetting.ONE as Setting<Int, ClickableWidget>

    @Suppress("UNCHECKED_CAST")
    protected fun intZeroSetting(): Setting<Int, ClickableWidget> =
        IntSetting.ZERO as Setting<Int, ClickableWidget>

    @Suppress("UNCHECKED_CAST")
    protected fun boolTrueSetting(): Setting<Boolean, ClickableWidget> =
        BooleanSetting.TRUE as Setting<Boolean, ClickableWidget>

    @Suppress("UNCHECKED_CAST")
    protected fun boolFalseSetting(): Setting<Boolean, ClickableWidget> =
        BooleanSetting.FALSE as Setting<Boolean, ClickableWidget>

    @Suppress("UNCHECKED_CAST")
    protected fun doubleSetting(): Setting<Double, ClickableWidget> =
        DoubleSetting.INSTANCE as Setting<Double, ClickableWidget>

    @Suppress("UNCHECKED_CAST")
    protected fun stringListSetting(): Setting<List<String>, ClickableWidget> =
        StringListSetting as Setting<List<String>, ClickableWidget>

    @Suppress("UNCHECKED_CAST")
    protected fun questIconSetting(): Setting<ItemQuestIcon, ClickableWidget> =
        QuestIconSetting.INSTANCE as Setting<ItemQuestIcon, ClickableWidget>

    /**
     * Helper method to add the appearance settings to a [CreationData] object.
     * @param element The [CustomizableQuestElementExtended] to get the appearance data from.
     * @param defaults The default [QuestElementDisplay] to use if the element is null.
     */
    protected fun SettingInitializer.CreationData.putAppearance(
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
     * Helper method to get the appearance data from a [Data] object.
     * @param element The [CustomizableQuestElementExtended] to get the appearance data from.
     * @param defaults The default [QuestElementDisplay] to use if the element is null.
     */
    protected fun SettingInitializer.Data.getAppearance(
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
