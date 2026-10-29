package com.ziroau.iolaus.api.data

import earth.terrarium.heracles.api.CustomizableQuestElement
import net.minecraft.text.Text

/**
 * An extended version of [CustomizableQuestElement] to accommodate common [display] properties for tasks and rewards.
 */
interface CustomizableQuestElementExtended : CustomizableQuestElement {
    /**
     * The appearance of the element, used in the Quest Viewer.
     * Provides all the necessary overridable properties for tasks & rewards without needing to duplicate them in every element.
     */
    val display: QuestElementDisplay

    /**
     * The title of the element, used in the Quest Viewer.
     */
    override fun title(): String = display.titleOverride

    /**
     * The title if it is not empty, otherwise returns the overridden title.
     * @param overridden The title to use in your widget, if the element's override title is empty.
     */
    override fun titleOr(overridden: Text): Text {
        return super.titleOr(overridden)
    }

    /**
     * The description of the element, used in the Quest Viewer.
     */
    fun description(): String = display.descriptionOverride

    /**
     * The description if it is not empty, otherwise returns the overridden description.
     * @param overridden The description to use in your widget, if the element's override description is empty.
     */
    fun descriptionOr(overridden: Text): Text {
        return if (description().isNotEmpty()) {
            Text.translatable(description())
        } else {
            overridden
        }
    }
}
