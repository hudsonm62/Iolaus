# Enums

Heracles supports "dropdown" boxes for Enums. However they didn't necessarily provide a flexible way to handle them, so Iolaus has bridged that gap.

!!! note

    You can see a basic implementation of this in the [Test Mod](https://github.com/hudsonm62/Iolaus/tree/master/fabric/src/testmod/kotlin/com/ziroau/iolaus_testmod/rewards/EnumTestReward.kt).

---

The first few steps are pretty straightforward..

First, define a [`StringIdentifiable`](https://maven.fabricmc.net/docs/yarn-1.21.1+build.1/net/minecraft/util/StringIdentifiable.html) enum in your code:

```kotlin
enum class MyEnum : StringIdentifiable {
    OPTION_ONE,
    OPTION_TWO,
    OPTION_THREE;

    override fun asString(): String {
        return name.lowercase()
    }
    // you could add methods for translations in widgets too
}
```

Then attach to your Task or Reward properties:

```kotlin
class EnumTestReward(
    override val rewardId: String,
    val myEnum: MyEnum = MyEnum.OPTION_ONE,
    override val display: QuestElementDisplay
) : BaseQuestReward<EnumTestReward> {
    // ...

    // Also ensure it's part of your codec boilerplate:
    companion object {
        @JvmField
        val TYPE: QuestRewardType<EnumTestReward> =
            QuestTypesFactory.reward(
                Identifier.of("samplemod", "testenum_reward")
            ) { id ->
                RecordCodecBuilder.mapCodec { instance ->
                    instance.group(
                        //...

                        EnumCodec.of(MyEnum::class.java)
                            .fieldOf("myEnum")
                            .forGetter(EnumTestReward::myEnum)

                        //...
                    )
                }
            }
    }
}
```

---

Now we need a way to serialize this enum for settings.
Iolaus provides a [`EnumSetting`](https://github.com/hudsonm62/Iolaus/blob/master/common/src/main/kotlin/com/ziroau/iolaus/api/client/settings/EnumSettings.kt#L18)
that handles creating a Heracles Setting ready for use.

```kotlin
class EnumTestRewardSettings : AbstractSettingInitializer<EnumTestReward>() {
    override fun create(task: EnumTestReward?): SettingInitializer.CreationData {
        //..

        settings.put("myEnum", enumSetting(MyEnum.OPTION_ONE), task?.myEnum ?: MyEnum.OPTION_ONE)

        //..
        return settings
    }

    override fun create(id: String, task: EnumTestReward?, data: SettingInitializer.Data) : EnumTestReward {
        return EnumTestReward(
            //..

            data.get("myEnum", enumSetting(MyEnum.OPTION_ONE)).orElse(MyEnum.OPTION_ONE)

            //..
        )
    }
}
```

---

And thats essentially it!
You could also take it a step further in your widgets, and make it translatable so your default titles and descriptions can be localized,
or perhaps set a custom icon for each enum option. But you can experiment with that on your own as needed.
