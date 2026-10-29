package com.ziroau.iolaus_testmod.client

import net.fabricmc.api.ClientModInitializer

class IolausTestFabricClient : ClientModInitializer {
    override fun onInitializeClient() {
        println("Hello from Fabric client!")
    }
}
