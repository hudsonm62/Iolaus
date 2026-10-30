package com.ziroau.iolaus_testmod.client

import com.ziroau.iolaus.api.client.registry.RewardsClientRegistry
import com.ziroau.iolaus.api.client.registry.TasksClientRegistry
import com.ziroau.iolaus_testmod.client.settings.EntityRenderTestTaskSettings
import com.ziroau.iolaus_testmod.client.settings.EnumTestRewardSettings
import com.ziroau.iolaus_testmod.client.widgets.EntityRenderTestTaskWidget
import com.ziroau.iolaus_testmod.client.widgets.EnumTestRewardWidget
import com.ziroau.iolaus_testmod.rewards.EnumTestReward
import com.ziroau.iolaus_testmod.tasks.EntityRenderTestTask
import net.fabricmc.api.ClientModInitializer

class IolausTestFabricClient : ClientModInitializer {
    override fun onInitializeClient() {
        TasksClientRegistry.register(
            EntityRenderTestTask.TYPE,
            EntityRenderTestTaskSettings.INSTANCE,
            ::EntityRenderTestTaskWidget
        ) { progress, task -> "123" }

        RewardsClientRegistry.register(
            EnumTestReward.TYPE,
            EnumTestRewardSettings.INSTANCE,
            EnumTestRewardWidget::of
        )
    }
}
