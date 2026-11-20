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

package com.ziroau.iolaus.api.commands

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import earth.terrarium.heracles.api.quests.Quest
import earth.terrarium.heracles.common.commands.ModCommands.QUESTS
import earth.terrarium.heracles.common.handlers.quests.QuestHandler
import net.minecraft.server.command.ServerCommandSource
import java.util.concurrent.CompletableFuture

/**
 * A collection of Brigadier suggestion providers for Iolaus commands
 */
object IolausSuggestions {
    /**
     * Suggests entries belonging to a quest resolved from the command context.
     *
     * @param context Brigadier command context containing the quest ID argument.
     * @param builder Suggestions builder.
     * @param questIdArgumentName Name of the quest ID argument (defaults to "questid").
     * @param entries Function extracting the entries to suggest from the resolved quest.
     *
     * @return An empty suggestion result if the quest ID cannot be resolved.
     */
    @JvmStatic @JvmOverloads
    fun suggestEntriesForQuest(
        context: CommandContext<ServerCommandSource>,
        builder: SuggestionsBuilder,
        questIdArgumentName: String = "questid",
        entries: (Quest) -> Collection<String>
    ): CompletableFuture<Suggestions> {
        // Resolve the quest ID from the command context
        val questId = runCatching {
            StringArgumentType.getString(
                context, questIdArgumentName.trim().lowercase()
            )
        }.getOrNull() ?: return builder.buildFuture()

        // Resolve the quest from the quest ID
        val quest = QuestHandler.get(questId)
            ?: return builder.buildFuture()

        // Now fill out suggestions from the quest
        entries(quest).forEach(builder::suggest)

        return builder.buildFuture()
    }

    @JvmField
    val QUEST_ID_SUGGESTER: SuggestionProvider<ServerCommandSource> = QUESTS
    @JvmField
    val TASK_ID_SUGGESTER: SuggestionProvider<ServerCommandSource> =
        SuggestionProvider { context, builder ->
            suggestEntriesForQuest(context, builder) { it.tasks().keys }
        }
    @JvmField
    val REWARD_ID_SUGGESTER: SuggestionProvider<ServerCommandSource> =
        SuggestionProvider { context, builder ->
            suggestEntriesForQuest(context, builder) { it.rewards().keys }
        }
}
