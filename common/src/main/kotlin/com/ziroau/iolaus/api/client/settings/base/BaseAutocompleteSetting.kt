package com.ziroau.iolaus.api.client.settings.base

import earth.terrarium.heracles.api.client.settings.Setting
import earth.terrarium.olympus.client.components.textbox.autocomplete.AutocompleteTextBox

/**
 * A simple base class creating a Text [Setting] with [suggestions] autocomplete.
 * It provides a default implementation for creating the widget and getting the value from it.
 *
 * @param allowRawInput Whether to allow "raw" input that is not in the suggestions list.
 *
 * @property suggestions The [List] of suggestions to display in the autocomplete dropdown.
 * @property defaultValue The value to default to if the input is empty, invalid or null
 *
 * @see Setting
 * @see AutocompleteTextBox
 */
abstract class BaseAutocompleteSetting (
    protected val allowRawInput: Boolean = false
): Setting<String, AutocompleteTextBox<String>> {
    /**
     * Value to default to if the input is empty, invalid or null - This is used to ensure that the setting always has a valid value.
     *
     * If you want to allow an empty or just any value, even if it's not in the suggestions list,
     * set this variable to "" and set [allowRawInput] to `true`.
     */
    abstract val defaultValue: String

    /**
     * List of suggestions to display in the autocomplete dropdown.
     * You don't need to `sort()` or `distinct()` this list, as it will be done automatically.
     */
    abstract val suggestions: List<String>

    override fun createWidget(
        old: AutocompleteTextBox<String>?, width: Int, value: String
    ): AutocompleteTextBox<String> {
        val input = suggestions.distinct().sorted()

        return AutocompleteTextBox(
            old, value, width, 24, input,
            { typed: String, candidate: String ->
                candidate.contains(typed.trim(), ignoreCase = true) &&
                        !candidate.equals(typed.trim(), ignoreCase = true)
            },
            { candidate: String -> candidate }
        )
    }

    override fun getValue(widget: AutocompleteTextBox<String>?): String {
        val value = widget?.rawValue?.trim() ?: return defaultValue

        if (allowRawInput) return value

        return suggestions.firstOrNull {
            it.equals(value, ignoreCase = true)
        } ?: defaultValue
    }
}
