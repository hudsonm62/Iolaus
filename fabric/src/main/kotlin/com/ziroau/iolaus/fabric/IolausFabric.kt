package com.ziroau.iolaus.fabric

import com.ziroau.iolaus.Iolaus.initialize
import com.ziroau.iolaus.commands.IolausCommand
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback

class IolausFabric : ModInitializer {
    override fun onInitialize() {
        initialize()
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            IolausCommand.register(dispatcher)
        }
    }
}
