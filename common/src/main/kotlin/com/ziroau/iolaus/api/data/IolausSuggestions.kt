package com.ziroau.iolaus.api.data

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
    @JvmStatic
    fun suggestEntriesForQuest(
        context: CommandContext<ServerCommandSource>,
        builder: SuggestionsBuilder,
        questIdArgumentName: String = "questid",
        entries: (Quest) -> Collection<String>
    ): CompletableFuture<Suggestions> {
        val questId = runCatching {
            StringArgumentType.getString(context, questIdArgumentName)
        }.getOrNull() ?: return builder.buildFuture()

        val quest = QuestHandler.get(questId)
            ?: return builder.buildFuture()

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
