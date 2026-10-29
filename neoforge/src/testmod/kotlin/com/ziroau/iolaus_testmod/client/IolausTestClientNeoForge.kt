package com.ziroau.iolaus_testmod.client

import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent

@Suppress("unused")
@Mod(value = "iolaus_testmod", dist = [Dist.CLIENT])
class IolausTestClientNeoForge(modBus: IEventBus) {
    init {
        modBus.addListener(::onClientSetup)
    }

    private fun onClientSetup(event: FMLClientSetupEvent) {
        event.enqueueWork {
            println("Hello from NeoForge client!")
        }
    }
}
