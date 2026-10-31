
package com.ziroau.iolaus_testmod

import com.mojang.brigadier.arguments.LongArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.ziroau.iolaus_testmod.tasks.LongStorageTestTask
import earth.terrarium.heracles.common.handlers.progress.QuestProgressHandler
import earth.terrarium.heracles.common.handlers.quests.QuestHandler
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.server.command.CommandManager
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object IolausTestCommands {
    fun register() {
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            dispatcher.register(
                CommandManager.literal("iolaustest")
                    .executes { 1 }
                    .then(
                        CommandManager.literal("deltalong")
                            .then(
                                CommandManager.argument("player", EntityArgumentType.player())
                                    .then(
                                        CommandManager.argument("questid", StringArgumentType.word())
                                            .then(
                                                CommandManager.argument("taskid", StringArgumentType.word())
                                                    .then(
                                                        CommandManager.argument("delta", LongArgumentType.longArg())
                                                            .executes { ctx ->
                                                                val player = EntityArgumentType.getPlayer(ctx, "player")
                                                                val questId = StringArgumentType.getString(ctx, "questid")
                                                                val taskId = StringArgumentType.getString(ctx, "taskid")
                                                                val delta = LongArgumentType.getLong(ctx, "delta")

                                                                deltaLongTask(player, questId, taskId, delta)
                                                                1
                                                            }
                                                    )
                                            )
                                    )
                            )
                    )
            )
        }
    }

    private fun deltaLongTask(player: ServerPlayerEntity, questId: String, taskId: String, delta: Long) {
        player.server.execute {
            val questProgress = QuestProgressHandler.getProgress(player.server, player.uuid)
            questProgress.testAndProgressTask(
                player,
                questId,
                taskId,
                delta,
                LongStorageTestTask.TYPE
            )
        }
    }
    private fun ServerPlayerEntity.error(message: String) {
        val txt = Text.literal(message).formatted(Formatting.RED)
        this.sendMessage(txt, false)
    }
}
