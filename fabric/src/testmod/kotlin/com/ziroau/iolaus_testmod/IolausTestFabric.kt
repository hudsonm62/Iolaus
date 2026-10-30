package com.ziroau.iolaus_testmod

import com.ziroau.iolaus.api.registry.RewardsRegistry
import com.ziroau.iolaus.api.registry.TasksRegistry
import com.ziroau.iolaus_testmod.rewards.EnumTestReward
import com.ziroau.iolaus_testmod.tasks.EntityRenderTestTask
import net.fabricmc.api.ModInitializer

class IolausTestFabric : ModInitializer {
    companion object {
        const val MOD_ID = "iolaus_testmod"
    }
    override fun onInitialize() {
        TasksRegistry.registerAll(
            EntityRenderTestTask.TYPE
        )
        RewardsRegistry.registerAll(
            EnumTestReward.TYPE
        )
    }
}
