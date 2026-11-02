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
