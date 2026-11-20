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

package com.ziroau.iolaus.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.ziroau.iolaus.Iolaus.MOD_ID
import com.ziroau.iolaus.api.commands.IolausArgument
import com.ziroau.permissionvalidator.api.PermissionManager.requires
import earth.terrarium.heracles.api.events.HeraclesEvents
import earth.terrarium.heracles.api.events.TaskEventTarget
import earth.terrarium.heracles.common.handlers.progress.QuestProgressHandler
import earth.terrarium.heracles.common.handlers.quests.QuestHandler
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
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
        val root = CommandManager.literal(MOD_ID)
            .requires(IolausPermissions.ROOT)
            .then(completeTaskNode())
            .then(claimRewardNode())
            .then(debugNode())

        dispatcher.register(root)
    }

    private fun completeTaskNode() =
        CommandManager.literal("completetask")
            .requires(IolausPermissions.COMPLETE_TASK)
            .then(CommandManager.argument("player", EntityArgumentType.player())
                    .then(IolausArgument.quests("questid")
                            .then(IolausArgument.tasks("taskid")
                                    .executes { ctx -> completeQuestTask(ctx) }
                            )
                    )
            )

    private fun claimRewardNode() =
        CommandManager.literal("claimreward")
            .requires(IolausPermissions.CLAIM_REWARD)
            .then(CommandManager.argument("player", EntityArgumentType.player())
                    .then(IolausArgument.quests("questid")
                            .then(IolausArgument.rewards("rewardid")
                                    .executes { ctx -> claimReward(ctx) }
                            )
                    )
            )

    private fun debugNode() =
        CommandManager.literal("debug")
            .requires(IolausPermissions.DEBUG)
            .then(CommandManager.argument("player", EntityArgumentType.player())
                    .then(IolausArgument.quests("questid")
                            .executes { ctx -> debug(ctx) }
                    )
            )

    /// This ignores tasks requirements - needs more testing, may not work as intended on all task types i.e. if retested
    private fun completeQuestTask(ctx: CommandContext<ServerCommandSource>): Int {
        val source = ctx.source
        val player = EntityArgumentType.getPlayer(ctx, "player")
        val taskId = StringArgumentType.getString(ctx, "taskid")
        val questId = StringArgumentType.getString(ctx, "questid")

        val quest = QuestHandler.get(questId) ?: run {
            return source.failure("command.iolaus.failure.questnotfound", questId)
        }
        val task = quest.tasks()[taskId] ?: run {
            return source.failure("command.iolaus.failure.tasknotfound", taskId, questId)
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
            return source.failure("command.iolaus.failure.questnotfound", questId)
        }
        questObj.rewards[rewardId] ?: run {
            return source.failure("command.iolaus.failure.rewardnotfound", rewardId, questId)
        }
        val questProg = QuestProgressHandler.getProgress(player.server, player.uuid).getProgress(questId)
        if(!questProg.canClaim(rewardId)) {
            return source.failure("command.iolaus.claimreward.failure.rewardnotclaimable", rewardId, questId)
        }

        questObj.claimAllowedReward(player, questId, rewardId)

        return source.success(
            "command.iolaus.claimreward.success", true,
            rewardId, questId, player.name.string
        )
    }

    fun debug(ctx: CommandContext<ServerCommandSource>): Int {
        val source = ctx.source
        val player = EntityArgumentType.getPlayer(ctx, "player")
        val questId = StringArgumentType.getString(ctx, "questid")
        val questObj = QuestHandler.get(questId) ?: run {
            return source.failure("command.iolaus.failure.questnotfound", questId)
        }
        val questProg = QuestProgressHandler.getProgress(player.server, player.uuid).getProgress(questId)
        val groupNames = questObj.display().groups().keys
        val isComplete = questProg.isComplete
        val completedTasks = questProg.tasks().filter{ it.value.isComplete }.keys
        val uncompletedTasks = questProg.tasks().filter{ !it.value.isComplete }.keys
        val claimedRewards = questProg.claimedRewards()
        val unclaimedRewards = questObj.rewards.keys.filter { !claimedRewards.contains(it) }
        val claimableRewards = questObj.rewards.keys.filter { questProg.canClaim(it) }
        val questDependencies = questObj.dependencies

        // Prints out quest progress information for debugging purposes as output:
        /**
         * - Groups: <groupName1>, ...
         * - Quest ID: <questId>
         * - Completed: <true/false>
         * - Completed Tasks: <taskId1>, <taskId2>, ...
         * - Uncompleted Tasks: <taskId1>, <taskId2>, ...
         * - Claimed Rewards: <rewardId1>, <rewardId2>, ...
         * - Unclaimed Rewards: <rewardId1>, <rewardId2>, ...
         * - Claimable Rewards: <rewardId1> (true/false), <rewardId2> (true/false), ...
         * - Completing Unlocks: <questId1>, <questId2>, ...
         */
        source.info("command.iolaus.debug.groups", false, groupNames.joinToString(", "))
        source.info("command.iolaus.debug.questid",     false, questId)
        source.info("command.iolaus.debug.completed",   false, isComplete.toString())
        source.info("command.iolaus.debug.completedtasks", false, completedTasks.joinToString(", "))
        source.info("command.iolaus.debug.uncompletedtasks", false, uncompletedTasks.joinToString(", "))
        source.info("command.iolaus.debug.claimedrewards", false, claimedRewards.joinToString(", "))
        source.info("command.iolaus.debug.unclaimedrewards", false, unclaimedRewards.joinToString(", "))
        source.info("command.iolaus.debug.canclaimrewards", false, claimableRewards.joinToString(", "))
        source.info("command.iolaus.debug.completingunlocks", false, questDependencies.joinToString(", "))

        return 1
    }

    private fun ServerCommandSource.success(message: String, broadcastToOps: Boolean = false, vararg args: String): Int {
        sendFeedback(
            { Text.translatable(message, *args).formatted(Formatting.GREEN) },
            broadcastToOps
        )
        return 1
    }

    private fun ServerCommandSource.info(message: String, broadcastToOps: Boolean = false, vararg args: String) {
        sendFeedback(
            { Text.translatable(message, *args).formatted(Formatting.GRAY) },
            broadcastToOps
        )
    }

    private fun ServerCommandSource.failure(message: String, vararg args: String): Int {
        sendError(Text.translatable(message, *args).formatted(Formatting.RED))
        return 0
    }
}
