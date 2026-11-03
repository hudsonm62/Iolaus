package com.ziroau.iolaus.commands

import com.ziroau.permissionvalidator.api.PermissionLevel
import com.ziroau.permissionvalidator.api.PermissionManager

object IolausPermissions {
    /** /iolaus & /iolaus help */
    @JvmStatic
    val ROOT = PermissionManager.create("iolaus", PermissionLevel.NONE)

    /** /iolaus completetask */
    @JvmStatic
    val COMPLETE_TASK = PermissionManager.create("iolaus.completetask", PermissionLevel.MULTIPLAYER_MANAGEMENT)

    /** /iolaus claimreward */
    @JvmStatic
    val CLAIM_REWARD = PermissionManager.create("iolaus.claimreward", PermissionLevel.MULTIPLAYER_MANAGEMENT)

    /** /iolaus debug */
    @JvmStatic
    val DEBUG = PermissionManager.create("iolaus.debug", PermissionLevel.MULTIPLAYER_MANAGEMENT)

    fun init() {
        PermissionManager.initialize(listOf(
            ROOT,
            COMPLETE_TASK,
            CLAIM_REWARD,
            DEBUG
        ))
    }
}
