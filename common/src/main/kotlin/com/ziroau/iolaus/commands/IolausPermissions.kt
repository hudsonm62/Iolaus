/*
 * Copyright (c) 2026 Hudson M.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
