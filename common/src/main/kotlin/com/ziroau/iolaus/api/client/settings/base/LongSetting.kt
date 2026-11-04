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

package com.ziroau.iolaus.api.client.settings.base

import com.ziroau.iolaus.api.client.settings.textbox.LongTextBox
import earth.terrarium.heracles.api.client.settings.Setting

/**
 * A setting that accepts a 64-bit signed integer.
 *
 * @param defaultValue Used when the widget value is invalid
 */
@JvmRecord
data class LongSetting(
    val defaultValue: Long = 0L
) : Setting<Long, LongTextBox> {
    override fun createWidget(old: LongTextBox?, width: Int, value: Long?): LongTextBox =
        LongTextBox(old, width, 24, value ?: defaultValue) {}

    override fun getValue(widget: LongTextBox): Long =
        widget.getLongValue().orElse(defaultValue)

    companion object {
        @JvmField
        val ZERO = LongSetting(0L)
        @JvmField
        val ONE  = LongSetting(1L)
    }
}
