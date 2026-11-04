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

import earth.terrarium.heracles.api.CustomizableQuestElement
import earth.terrarium.heracles.api.quests.QuestIcon
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

    /**
     * The icon of the element, used in the Quest Viewer.
     */
    override fun icon(): QuestIcon<*> = display.iconOverride
}
