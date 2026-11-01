package com.ziroau.iolaus.neoforge

import com.ziroau.iolaus.Iolaus.MOD_ID
import com.ziroau.iolaus.Iolaus.initialize
import com.ziroau.iolaus.commands.IolausCommand
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.RegisterCommandsEvent

@Mod(MOD_ID)
class IolausNeoForge {
    init {
        initialize()
        NeoForge.EVENT_BUS.addListener<RegisterCommandsEvent> { event ->
            IolausCommand.register(event.dispatcher)
        }
    }
}
