package com.ziroau.iolaus.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.ziroau.iolaus.api.data.IolausSuggestions.QUEST_ID_SUGGESTER
import com.ziroau.iolaus.api.data.IolausSuggestions.REWARD_ID_SUGGESTER
import com.ziroau.iolaus.api.data.IolausSuggestions.TASK_ID_SUGGESTER
import com.ziroau.permissionvalidator.api.PermissionManager
import com.ziroau.permissionvalidator.api.PermissionManager.requires
import earth.terrarium.heracles.api.events.HeraclesEvents
import earth.terrarium.heracles.api.events.TaskEventTarget
import earth.terrarium.heracles.common.handlers.progress.QuestProgressHandler
import earth.terrarium.heracles.common.handlers.quests.QuestHandler
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

/**
 * The `/iolaus` commands tree
 *
 * Commands here may _potentially_ be removed in the future if they ever become redundant with Heracles' own commands.
 *
 * Try to only use these for debug & server administrative purposes.
 * Avoid outright relying on them in mcfunctions or otherwise in production unless truly necessary.
 */
object IolausCommand {
    fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        val root = CommandManager.literal("iolaus")
            .requires(IolausPermissions.ROOT)
            .executes { modInfo(it.source) }
            .then(
                CommandManager.literal("help")
                    .requires(IolausPermissions.ROOT)
                    .executes { modInfo(it.source) }
            )
            .then(completeTaskNode())
            .then(claimRewardNode())

        dispatcher.register(root)
    }

    private fun completeTaskNode() =
        CommandManager.literal("completetask")
            .requires(IolausPermissions.COMPLETE_TASK)
            .then(
                CommandManager.argument("player", EntityArgumentType.player())
                    .then(
                        CommandManager.argument("questid", StringArgumentType.word())
                            .suggests(QUEST_ID_SUGGESTER)
                            .then(
                                CommandManager.argument("taskid", StringArgumentType.word())
                                    .suggests(TASK_ID_SUGGESTER)
                                    .executes { ctx -> completeQuestTask(ctx) }
                            )
                    )
            )

    private fun claimRewardNode() =
        CommandManager.literal("claimreward")
            .requires(IolausPermissions.CLAIM_REWARD)
            .then(
                CommandManager.argument("player", EntityArgumentType.player())
                    .then(
                        CommandManager.argument("questid", StringArgumentType.word())
                            .suggests(QUEST_ID_SUGGESTER)
                            .then(
                                CommandManager.argument("rewardid", StringArgumentType.word())
                                    .suggests(REWARD_ID_SUGGESTER)
                                    .executes { ctx -> claimReward(ctx) }
                            )
                    )
            )

    private fun modInfo(source: ServerCommandSource): Int {
        fun ServerCommandSource.titleLine(title: MutableText) {
            sendFeedback(
                { title.formatted(Formatting.AQUA) },
                false
            )
        }
        fun ServerCommandSource.helpLine(command: String, description: MutableText) {
            val formattedCmd = Text.literal(command).formatted(Formatting.WHITE)
            sendFeedback(
                {
                    formattedCmd.append(Text.literal(" - ").formatted(Formatting.GRAY)).append(description.formatted(Formatting.GRAY))
                },
                false
            )
        }

        source.titleLine(Text.literal("---- Iolaus Commands ----"))
        source.helpLine("/iolaus help", Text.translatable("command.iolaus.help.help"))
        if (PermissionManager.hasPermission(source, IolausPermissions.COMPLETE_TASK)) {
            source.helpLine("/iolaus completetask <player> <questid> <taskid>", Text.translatable("command.iolaus.help.completetask"))
        }
        if(PermissionManager.hasPermission(source, IolausPermissions.CLAIM_REWARD)) {
            source.helpLine("/iolaus claimreward <player> <questid> <rewardid>", Text.translatable("command.iolaus.help.claimreward"))
        }

        return 1
    }

    /// This ignores tasks requirements - needs more testing, may not work as intended on all task types i.e. if retested
    private fun completeQuestTask(ctx: CommandContext<ServerCommandSource>): Int {
        val source = ctx.source
        val player = EntityArgumentType.getPlayer(ctx, "player")
        val taskId = StringArgumentType.getString(ctx, "taskid")
        val questId = StringArgumentType.getString(ctx, "questid")

        val quest = QuestHandler.get(questId) ?: run {
            return source.failure("command.iolaus.completetask.failure.questnotfound", questId)
        }
        val task = quest.tasks()[taskId] ?: run {
            return source.failure("command.iolaus.completetask.failure.tasknotfound", taskId, questId)
        }

        val questsProgress = QuestProgressHandler.getProgress(player.server, player.uuid)
        val questProgress = questsProgress.getProgress(questId)
        val taskProgress = questProgress.getTask(task)

        if (taskProgress.isComplete)
            return source.failure("command.iolaus.completetask.failure.alreadycomplete", taskId, questId, player.name.string)

        // Force completion regardless of task type
        taskProgress.isComplete = true

        // Trigger Heracles' task completion event
        HeraclesEvents.TaskCompleteListener.fire(
            TaskEventTarget.create(task, player)
        )

        // Recalculate quest completion, unlocks and team progress
        questsProgress.sendOutQuestChanged(
            questId, quest, questProgress, player
        )

        // Sync updated progress to the client
        QuestProgressHandler.sync(player, listOf(questId))

        return if (taskProgress.isComplete) {
            source.success("command.iolaus.completetask.success", true, taskId, questId, player.name.string)
        } else {
            source.failure("command.iolaus.completetask.failure.unknown", taskId, questId, player.name.string)
        }
    }

    private fun claimReward(ctx: CommandContext<ServerCommandSource>): Int {
        val source = ctx.source
        val player = EntityArgumentType.getPlayer(ctx, "player")
        val rewardId = StringArgumentType.getString(ctx, "rewardid")
        val questId = StringArgumentType.getString(ctx, "questid")

        val questObj = QuestHandler.get(questId) ?: run {
            return source.failure("command.iolaus.claimreward.questnotfound", questId)
        }
        questObj.rewards[rewardId] ?: run {
            return source.failure("command.iolaus.claimreward.rewardnotfound", rewardId, questId)
        }
        val questProg = QuestProgressHandler.getProgress(player.server, player.uuid).getProgress(questId)
        if(!questProg.canClaim(rewardId)) {
            return source.failure("command.iolaus.claimreward.rewardnotclaimable", rewardId, questId)
        }

        questObj.claimAllowedReward(player, questId, rewardId)

        return source.success(
            "command.iolaus.claimreward.success", true,
            rewardId, questId, player.name.string
        )
    }

    private fun ServerCommandSource.success(message: String, broadcastToOps: Boolean = false, vararg args: String): Int {
        sendFeedback(
            { Text.translatable(message).formatted(Formatting.GREEN) },
            broadcastToOps
        )
        return 1
    }

    private fun ServerCommandSource.info(message: String, broadcastToOps: Boolean = false, vararg args: String) {
        sendFeedback(
            { Text.translatable(message).formatted(Formatting.GRAY) },
            broadcastToOps
        )
    }

    private fun ServerCommandSource.failure(message: String, broadcastToOps: Boolean = false, vararg args: String): Int {
        sendError(Text.translatable(message).formatted(Formatting.RED))
        return 0
    }
}
