package com.ziroau.iolaus

import com.ziroau.iolaus.commands.IolausPermissions

object Iolaus {
    const val MOD_ID = "iolaus"
    fun initialize() {
        IolausPermissions.init()
    }
}
