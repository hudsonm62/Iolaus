package com.ziroau.iolaus_testmod.client

import com.ziroau.iolaus.api.client.registry.RewardsClientRegistry
import com.ziroau.iolaus.api.client.registry.TasksClientRegistry
import com.ziroau.iolaus_testmod.client.settings.BoolTestTaskSettings
import com.ziroau.iolaus_testmod.client.settings.CommandsListTestRewardSettings
import com.ziroau.iolaus_testmod.client.settings.EntityRenderTestTaskSettings
import com.ziroau.iolaus_testmod.client.settings.EnumTestRewardSettings
import com.ziroau.iolaus_testmod.client.settings.ExtendedDisplayTestTaskSettings
import com.ziroau.iolaus_testmod.client.settings.LongStorageTestTaskSettings
import com.ziroau.iolaus_testmod.client.widgets.BoolTestTaskWidget
import com.ziroau.iolaus_testmod.client.widgets.CommandsListTestRewardWidget
import com.ziroau.iolaus_testmod.client.widgets.EntityRenderTestTaskWidget
import com.ziroau.iolaus_testmod.client.widgets.EnumTestRewardWidget
import com.ziroau.iolaus_testmod.client.widgets.ExtendedDisplayTestTaskWidget
import com.ziroau.iolaus_testmod.client.widgets.LongStorageTestTaskWidget
import com.ziroau.iolaus_testmod.rewards.CommandsListTestReward
import com.ziroau.iolaus_testmod.rewards.EnumTestReward
import com.ziroau.iolaus_testmod.tasks.BoolTestTask
import com.ziroau.iolaus_testmod.tasks.EntityRenderTestTask
import com.ziroau.iolaus_testmod.tasks.ExtendedDisplayTestTask
import com.ziroau.iolaus_testmod.tasks.LongStorageTestTask
import net.fabricmc.api.ClientModInitializer

class IolausTestFabricClient : ClientModInitializer {
    override fun onInitializeClient() {
        TasksClientRegistry.register(
            EntityRenderTestTask.TYPE,
            EntityRenderTestTaskSettings.INSTANCE,
            ::EntityRenderTestTaskWidget
        ) { progress, task -> "123" }
        TasksClientRegistry.register(
            LongStorageTestTask.TYPE,
            LongStorageTestTaskSettings.INSTANCE,
            ::LongStorageTestTaskWidget
        )  { progress, task ->
            val progress = task.storage().readLong(progress.progress()).coerceAtLeast(0L)
            "$progress/${task.amount.coerceAtLeast(1L)}"
        }
        TasksClientRegistry.register(
            ExtendedDisplayTestTask.TYPE,
            ExtendedDisplayTestTaskSettings.INSTANCE,
            ::ExtendedDisplayTestTaskWidget
        )  { progress, task ->
            val progress = task.storage().readLong(progress.progress()).coerceAtLeast(0L)
            "$progress/${task.amount.coerceAtLeast(1L)}"
        }
        TasksClientRegistry.register(
            BoolTestTask.TYPE,
            BoolTestTaskSettings.INSTANCE,
            ::BoolTestTaskWidget
        ) { _, _ ->
            "" // Boolean Widget handles coloured display text, so we don't need to provide any text here.
               // It's purely for the [formatter] property to be filled with something to compile
        }

        RewardsClientRegistry.register(
            EnumTestReward.TYPE,
            EnumTestRewardSettings.INSTANCE,
            EnumTestRewardWidget::of
        )
        RewardsClientRegistry.register(
            CommandsListTestReward.TYPE,
            CommandsListTestRewardSettings.INSTANCE,
            CommandsListTestRewardWidget::of
        )
    }
}
