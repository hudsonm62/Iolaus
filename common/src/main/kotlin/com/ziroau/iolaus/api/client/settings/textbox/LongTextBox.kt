package com.ziroau.iolaus.api.client.settings.textbox

import earth.terrarium.olympus.client.components.textbox.TextBox
import earth.terrarium.olympus.client.utils.ListenableState
import java.util.OptionalLong
import java.util.function.LongConsumer

/**
 * A numeric text box supporting 64-bit signed integers.
 *
 * Functions similarly to Heracles' [earth.terrarium.heracles.client.components.widgets.textbox.IntTextBox],
 * but supports the full Long range.
 */
class LongTextBox private constructor(
    state: ListenableState<String>,
    width: Int, height: Int,
    responder: LongConsumer
) : TextBox(state) {
    init {
        withSize(width, height)
        withMaxLength(20) // Long.MAX_VALUE is 19 digits, plus one for a negative sign
        withFilter(::filter)
        state.registerListener { text ->
            responder.accept(text.trim().toLongOrNull() ?: 0L)
        }
    }
    companion object {
        private const val TEXT_COLOR = 0xE0E0E0
        private const val ERROR_COLOR = 0xFF5555
        private fun filter(value: String): Boolean =
            value.isEmpty() || value == "-" || value.toLongOrNull() != null
    }

    constructor(
        old: LongTextBox?,
        width: Int, height: Int,
        value: Long, responder: LongConsumer
    ) : this(
        ListenableState.of(old?.value ?: value.toString()),
        width, height,
        responder
    )

    /**
     * Updates the value only when the widget is not focused.
     */
    fun setIfNotFocused(value: Long) {
        if (!isFocused) {
            val current = getLongValue()
            if (current.isEmpty || current.asLong != value) {
                setValue(value.toString())
            }
        }
    }

    /**
     * Returns the parsed Long, or empty if invalid.
     */
    fun getLongValue(): OptionalLong =
        value.toLongOrNull()?.let(OptionalLong::of)
            ?: OptionalLong.empty()

    override fun getTextColor(): Int =
        if (value.toLongOrNull() != null) TEXT_COLOR
        else ERROR_COLOR
}
