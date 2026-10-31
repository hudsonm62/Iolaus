package com.ziroau.iolaus_testmod.client.settings.base

import com.ziroau.iolaus.api.client.settings.base.BaseAutocompleteSetting

class AlphabetAutocomplete : BaseAutocompleteSetting(
    // Lets you write/save anything,
    // even if it isn't in the suggestions list
    allowRawInput = false
) {
    override val suggestions: List<String> = ('a'..'z').map { it.toString() }
    override val defaultValue: String = "a"
}
