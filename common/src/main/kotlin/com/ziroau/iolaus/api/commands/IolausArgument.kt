package com.ziroau.iolaus.api.commands

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.ziroau.iolaus.api.commands.IolausSuggestions.QUEST_ID_SUGGESTER
import com.ziroau.iolaus.api.commands.IolausSuggestions.REWARD_ID_SUGGESTER
import com.ziroau.iolaus.api.commands.IolausSuggestions.TASK_ID_SUGGESTER
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource

/**
 * A collection of Brigadier argument builders for Heracles-integrated commands.
 *
 * These builders automatically provide suggestions for quest IDs, task IDs, and reward IDs based on the current command context.
 */
object IolausArgument {

    /**
     * Creates a quest ID argument with automatic suggestions
     */
    @JvmStatic
    fun quests(name: String): RequiredArgumentBuilder<ServerCommandSource, String> {
        return CommandManager.argument(name, StringArgumentType.word())
            .suggests(QUEST_ID_SUGGESTER)
    }

    /**
     * Creates a task ID argument with automatic suggestions
     *
     * Suggestions are based on a previously specified `questid` argument.
     */
    @JvmStatic
    fun tasks(name: String): RequiredArgumentBuilder<ServerCommandSource, String> {
        return CommandManager.argument(name, StringArgumentType.word())
            .suggests(TASK_ID_SUGGESTER)
    }

    /**
     * Creates a reward ID argument with automatic suggestions
     *
     * Suggestions are based on a previously specified `questid` argument.
     */
    @JvmStatic
    fun rewards(name: String): RequiredArgumentBuilder<ServerCommandSource, String> {
        return CommandManager.argument(name, StringArgumentType.word())
            .suggests(REWARD_ID_SUGGESTER)
    }
}
