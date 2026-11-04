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

package com.ziroau.iolaus_testmod.data

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import com.ziroau.iolaus.api.data.BasicQuestElementDisplay
import com.ziroau.iolaus.api.data.QuestElementDisplay

/**
 * A test implementation of [QuestElementDisplay] that extends [BasicQuestElementDisplay] with a custom field.
 *
 * This makes use of delegation to [BasicQuestElementDisplay] to avoid having to reimplement all the methods in [QuestElementDisplay].
 * Of course, you could also just implement [QuestElementDisplay] directly, but this is a nice way to avoid rewriting boilerplate code.
 */
class TestExtendedQuestElementDisplay(
    val base: BasicQuestElementDisplay = BasicQuestElementDisplay(),
    val testEntry: String = ""
) : QuestElementDisplay by base {
    companion object {
        fun codec(
            defaults: TestExtendedQuestElementDisplay = TestExtendedQuestElementDisplay()
        ): MapCodec<TestExtendedQuestElementDisplay> =
            RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    BasicQuestElementDisplay
                        .codec(defaults.base)
                        .forGetter(TestExtendedQuestElementDisplay::base),

                    Codec.STRING
                        .optionalFieldOf(
                            "test_entry",
                            defaults.testEntry
                        )
                        .forGetter(TestExtendedQuestElementDisplay::testEntry)
                ).apply(instance, ::TestExtendedQuestElementDisplay)
            }

        val CODEC = codec()
    }
}
