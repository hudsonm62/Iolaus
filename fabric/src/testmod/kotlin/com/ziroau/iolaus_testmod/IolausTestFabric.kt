package com.ziroau.iolaus_testmod

import com.ziroau.iolaus.api.registry.RewardsRegistry
import com.ziroau.iolaus.api.registry.TasksRegistry
import com.ziroau.iolaus_testmod.rewards.CommandsListTestReward
import com.ziroau.iolaus_testmod.rewards.EnumTestReward
import com.ziroau.iolaus_testmod.tasks.EntityRenderTestTask
import com.ziroau.iolaus_testmod.tasks.LongStorageTestTask
import net.fabricmc.api.ModInitializer

class IolausTestFabric : ModInitializer {
    companion object {
        const val MOD_ID = "iolaus_testmod"
    }
    override fun onInitialize() {
        TasksRegistry.registerAll(
            EntityRenderTestTask.TYPE,
            LongStorageTestTask.TYPE
        )
        RewardsRegistry.registerAll(
            EnumTestReward.TYPE,
            CommandsListTestReward.TYPE
        )
        IolausTestCommands.register()
    }
}
