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
