# Text Auto Complete

Iolaus has a [`BaseAutocompleteSetting`](https://github.com/hudsonm62/Iolaus/blob/master/common/src/main/kotlin/com/ziroau/iolaus/api/client/settings/base/BaseAutocompleteSetting.kt), which is a minimal implementation of Olympus' [`AutocompleteTextBox`](https://github.com/terrarium-earth/Olympus/blob/1.21.1/common/src/main/java/earth/terrarium/olympus/client/components/textbox/autocomplete/AutocompleteTextBox.java).

The implementation exposes a `suggestions` property that you can fill with a list of strings to be used as autocomplete suggestions without needing further customization.

!!! note

    If you need more advanced autocomplete functionality, you'll need to implement your own autocomplete setting instead of relying on `BaseAutocompleteSetting`.

For example, this creates an autocomplete setting for the alphabet, and doesn't let you select anything outside of it:

```kotlin
import com.ziroau.iolaus.api.client.settings.base.BaseAutocompleteSetting

class AlphabetAutocomplete : BaseAutocompleteSetting(
    // Lets you write/save anything,
    // even if it isn't in the suggestions list
    allowRawInput = false
) {
    override val suggestions: List<String> = ('a'..'z').map { it.toString() }
    override val defaultValue: String = "a"
}
```

You can then use `AlphabetAutocomplete` in your Settings:

```kotlin
override fun create(task: FakeTask?): SettingInitializer.CreationData {
    val settings = SettingInitializer.CreationData()
    settings.put("alphabet", AlphabetAutocomplete(), task?.alphabet ?: "a")
    return settings
}
override fun create(
    id: String, task: FakeTask?, data: SettingInitializer.Data
): FakeTask {
    return FakeTask(
        id,
        data.get("alphabet", AlphabetAutocomplete()).orElse("a")
    )
}
```

Now you have a custom autocomplete!

![autocomplete example](/assets/images/autocomplete_example.png)
