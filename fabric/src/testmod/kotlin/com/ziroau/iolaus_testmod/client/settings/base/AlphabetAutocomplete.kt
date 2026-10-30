package com.ziroau.iolaus_testmod.client.settings.base

import com.ziroau.iolaus.api.client.settings.base.BaseAutocompleteSetting

class AlphabetAutocomplete : BaseAutocompleteSetting() {
    override val suggestions: List<String> = ('a'..'z').map { it.toString() }
    override val defaultValue: String = "a"

    // Lets you write/save anything that isn't in the suggestions list, even if its empty
    // override val allowRawInput: Boolean = false
}
