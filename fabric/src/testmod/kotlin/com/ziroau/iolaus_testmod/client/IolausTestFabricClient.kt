package com.ziroau.iolaus_testmod.client

import com.ziroau.iolaus.api.client.registry.TasksClientRegistry
import com.ziroau.iolaus_testmod.client.settings.EntityRenderTestTaskSettings
import com.ziroau.iolaus_testmod.client.widgets.EntityRenderTestTaskWidget
import com.ziroau.iolaus_testmod.tasks.EntityRenderTestTask
import net.fabricmc.api.ClientModInitializer

class IolausTestFabricClient : ClientModInitializer {
    override fun onInitializeClient() {
        TasksClientRegistry.register(
            EntityRenderTestTask.TYPE,
            EntityRenderTestTaskSettings.INSTANCE,
            ::EntityRenderTestTaskWidget
        ) { progress, task -> "123" }
    }
}
