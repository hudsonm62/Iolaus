# Multiline Strings

Heracles doesn't provide built-in multiline strings, so Iolaus has implemented a solution to handle them. It's actually pretty simple and follows the same pattern as other data types in Heracles.

Since a nicely formatted text box for multiline input didn't exist either, we implemented [`BorderedMultiLineTextBox`](https://github.com/hudsonm62/Iolaus/blob/master/common/src/main/kotlin/com/ziroau/iolaus/api/client/settings/textbox/BorderedMultiLineTextBox.kt)
which gets created automatically when using `stringListSetting()` - You can reuse this textbox for any custom multiline string setting input needed in your mod.

---

!!! tip

    See [`CommandsListTestReward`](https://github.com/hudsonm62/Iolaus/blob/master/fabric/src/testmod/kotlin/com/ziroau/iolaus_testmod/tasks/CommandsListTestReward.kt)
    for a working implementation of multiline strings.

In your task or reward, we can use a `List<String>`, where each line represents an entry in the list:

```kotlin
class CommandsListTestReward(
    override val rewardId: String,
    val commands: List<String>,
    val permission: Int = 4,
    override val display: QuestElementDisplay
)
```

And in serialization, we can use a `Codec.STRING.listOf`:

```kotlin
Codec.STRING.listOf()
    .optionalFieldOf("commands", emptyList<String>())
    .forGetter(CommandsListTestReward::commands)
```

---

For settings, if you're using Iolaus' [`AbstractSettingInitializer`](https://github.com/hudsonm62/Iolaus/blob/master/common/src/main/kotlin/com/ziroau/iolaus/api/client/settings/AbstractSettingInitializer.kt),
you can define the field as a `stringListSetting()`:

```kotlin
override fun create(reward: CommandsListTestReward?): SettingInitializer.CreationData {
    val settings = SettingInitializer.CreationData()
    settings.put("commands", stringListSetting(), reward?.commands ?: emptyList())
    settings.put("permission", intSetting(), reward?.permission ?: 4)
    settings.putDisplaySettings(reward)
    return settings
}
override fun create(
    id: String, reward: CommandsListTestReward?, data: SettingInitializer.Data
): CommandsListTestReward {
    return CommandsListTestReward(
        id,
        data.get("commands", stringListSetting()).orElse(reward?.commands ?: emptyList()),
        data.get("permission", intSetting()).orElse(reward?.permission ?: 4),
        data.getDisplaySettings(reward)
    )
}
```

---

There's nothing special in widgets we need to mention.

In the test reward used, it's a just bare bones widget to display something in the UI.

- [`CommandsListTestRewardWidget`](https://github.com/hudsonm62/Iolaus/blob/master/fabric/src/testmod/kotlin/com/ziroau/iolaus_testmod/client/widgets/CommandsListTestRewardWidget.kt)

---

And thats it!

![commands list setting](/assets/images/commands-list-setting.png)

```json
"rewards": {
    "testreward": {
        "commands": [
            "say Hello world!",
            "give @s minecraft:diamond 5",
            "effect give @s minecraft:speed 30 1"
        ],
        "permission": 4,
        "type": "iolaus_testmod:commandslisttest_reward"
    }
}
```

Results when the reward is claimed:

![command result](/assets/images/commands-list-execution.png)

In case you're curious, this is all the `reward()` implementation is:

```kotlin
override fun reward(player: ServerPlayerEntity): Stream<ItemStack?> {
    val source = player.commandSource.withLevel(permission)
    val commandManager = player.server.commandManager
    commands.forEach { commandManager.executeWithPrefix(source, it) }
    return Stream.empty()
}
```
