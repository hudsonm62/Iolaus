# Quest Element Displays

Iolaus provides `QuestElementDisplay` to represent the shared display properties of quest tasks and rewards, including their title, description, and an item icon.

The default `BasicQuestElementDisplay` implementation provides these properties out of the box. However, mods may require additional display information specific to their own functionality.

To support this, you can implement `QuestElementDisplay` with your own additional properties while delegating the common properties to `BasicQuestElementDisplay`.

!!! tip

    If you want, you can avoid delegating all together and write your own implementation of `QuestElementDisplay` from scratch. However that leads
    to writing your own boilerplate for handling the common display properties.

## Creating an Extended Display

Create a class implementing `QuestElementDisplay` and delegate its shared properties to a `BasicQuestElementDisplay` instance.

You can then add any additional properties your mod requires.

```kotlin
class ExampleQuestElementDisplay(
    val base: BasicQuestElementDisplay = BasicQuestElementDisplay(),
    val customProperty: String = ""
) : QuestElementDisplay by base
```

## Codec

Your extended display should provide its own `MapCodec`, composing `BasicQuestElementDisplay.codec()` with the codecs for any additional properties.

This allows the common and custom properties to be serialized together without introducing a nested `base` object in the resulting JSON.

```kotlin
class ExampleQuestElementDisplay(
    val base: BasicQuestElementDisplay = BasicQuestElementDisplay(),
    val customProperty: String = ""
) : QuestElementDisplay by base {
    companion object {
        fun codec(
            defaults: TestExtendedQuestElementDisplay = TestExtendedQuestElementDisplay()
        ): MapCodec<TestExtendedQuestElementDisplay> =
            RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    BasicQuestElementDisplay
                        .codec(defaults.base)
                        .forGetter(TestExtendedQuestElementDisplay::base),

                    Codec.STRING
                        .optionalFieldOf(
                            "test_entry",
                            defaults.testEntry
                        )
                        .forGetter(TestExtendedQuestElementDisplay::testEntry)
                ).apply(instance, ::TestExtendedQuestElementDisplay)
            }

        val CODEC = codec()
    }
}
```

## Using Extended Displays

When implementing a quest task or reward, use your extended display class as the concrete type of its `display` property and reference its codec in the element's `RecordCodecBuilder`.

Iolaus' existing APIs can continue accepting it through the `QuestElementDisplay` interface.

```kotlin
// In your tasks/rewards type definition
RecordCodecBuilder.mapCodec { instance ->
    instance.group(
        RecordCodecBuilder.point(id),
        TestExtendedQuestElementDisplay.CODEC
            .forGetter(ExtendedDisplayTestTask::display)
    ).apply(instance, ::ExtendedDisplayTestTask)
}
```

## Settings

You can continue using Iolaus' shared display settings helpers for the common properties.
Your mod only needs to provide settings for its additional properties and reconstruct its extended display when saving changes.

This keeps common display functionality within Iolaus while allowing individual mods to extend it with their own requirements.
