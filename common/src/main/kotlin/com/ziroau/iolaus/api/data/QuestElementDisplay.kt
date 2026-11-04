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
