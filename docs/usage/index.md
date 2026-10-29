---
icon: lucide/book
---

# Using the Library

Assuming you've [added Iolaus into your `build.gradle`](../index.md#using-iolaus){ data-preview } already, you can start using the library in your mod.

Before you begin, it's important to understand there are several moving parts when working with Heracles, that Iolaus helps to manage, and in some cases abstract away.

It essentially boils down to:

1. Defining Tasks & Rewards
2. Feeding Input Data to Tasks, then handling progress
3. Displaying Progress to the Client in the Viewer
4. Displaying Settings to the Client in the Editor

## Defining Tasks

For Tasks, two main components are provided in Iolaus, the `BaseQuestTask` class, and a `CountedQuestTask` class.
The `CountedQuestTask` is an implementation of `BaseQuestTask` that handles all the storage & NBT heavy lifting for tracking progress
of how many times `X` needs to be done to complete the task as a whole, and is what you'll likely use for _most_ objectives in your quests.

We'll also need to define a `QuestTaskType` boilerplate code for serialization and registration with Heracles.

```kotlin
package your.cool.mod.tasks

import com.ziroau.iolaus.api.tasks.BaseQuestTask
import com.ziroau.iolaus.api.tasks.CountedQuestTask
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import earth.terrarium.heracles.api.tasks.QuestTaskType
import net.minecraft.util.Identifier

class MyCoolTask (
    override val taskId: String,
    override val appearance: QuestElementDisplay,
    val someConfigProperty: String,
    override val amount: Int = 1 // required times to complete the task
) : CountedQuestTask<MyCoolTask> {
    // Implement your matching logic here
    override fun matches(input: MyCoolInputData): Boolean {
        // everytime `input` matches the criteria for this task, return true
        return input.someProperty == someConfigProperty
    }

    // Necessary `Type` Boilerplate below for serialization
    // and registration with Heracles
    companion object {
        @JvmField
        val TYPE: QuestTaskType<MyCoolTask> = Type()
    }
    override fun type(): QuestTaskType<MyCoolTask> = TYPE
    private class Type : QuestTaskType<MyCoolTask> {
        override fun id(): Identifier = Identifier.of(MOD_ID, "mycooltask_id")

        override fun codec(id: String): MapCodec<MyCoolTask> =
            RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    RecordCodecBuilder.point<MyCoolTask, String>(id),

                    QuestElementDisplay.CODEC
                        .forGetter(MyCoolTask::appearance),

                    Codec.STRING
                        .optionalFieldOf("someConfigProperty", "")
                        .forGetter(MyCoolTask::someConfigProperty),

                    Codec.INT
                        .optionalFieldOf("amount", 1)
                        .forGetter(MyCoolTask::amount),

                ).apply(instance, ::MyCoolTask)
            }
    }
}
```

!!! note

    See implementation of `CountedQuestTask` if you want to understand how to handle storage & progress tracking, for overriding the `test` method for custom logic.

### Feeding Input Data to Tasks

You'll need to provide your own mechanism for feeding input data into your tasks so that the `matches` method can evaluate it and progress the task accordingly.

Typically you might want to "subscribe" to an event or data source that provides the input data relevant to your task:

```kotlin
import earth.terrarium.heracles.common.handlers.progress.QuestProgressHandler
import net.minecraft.server.network.ServerPlayerEntity

object MyCoolEventListener {
    fun registerEventListener() {
        // Example: Subscribe to an event that provides MyCoolInputData
        SomeEvent.subscribe { input: MyCoolInputData ->
            // Feed the input data into your task system
            val sourcePlayer: ServerPlayerEntity = input.sourcePlayer

            // Hand off to the server thread for processing
            sourcePlayer.server.execute {
                val questProgress = QuestProgressHandler.getProgress(
                    sourcePlayer.server, sourcePlayer.uuid
                )

                // Let [MyCoolTask] handle the rest
                questProgress.testAndProgressTaskType(
                    sourcePlayer,
                    input,
                    MyCoolTask.TYPE
                )
            }
        }
    }
}
```

## Defining Rewards

Whilst similar to [Tasks](#defining-tasks), rewards are a bit simpler to implement - We define a class that extends the Iolaus [`BaseQuestReward`](https://github.com/hudsonm62/Iolaus/blob/master/common/src/main/kotlin/com/ziroau/iolaus/api/rewards/BaseQuestReward.kt),
then provide the necessary properties.

For rewards, override the `reward` method to define the behavior when the reward is granted. Similar to tasks, we also need to provide the necessary `QuestRewardType` boilerplate for config serialization.

```kotlin
package your.cool.mod.rewards

import com.ziroau.iolaus.api.rewards.BaseQuestReward
import com.ziroau.iolaus.api.data.QuestElementDisplay
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import earth.terrarium.heracles.api.rewards.QuestRewardType
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier
import java.util.stream.Stream

class MyCoolReward(
    override val rewardId: String,
    override val appearance: QuestElementDisplay,
    val someConfigProperty: String
) : BaseQuestReward<MyCoolReward> {
    // `reward()` defines the behavior when the reward is granted
    override fun reward(player: ServerPlayerEntity): Stream<ItemStack?> {
        LOGGER.info("Reward '$rewardId' with value $someConfigProperty has been granted :)")

        // Heracles expects a stream, despite the reward not necessarily producing items
        return Stream.empty()
    }

    // Necessary `Type` Boilerplate below for serialization
    // and registration with Heracles
    companion object {
        @JvmField
        val TYPE: QuestRewardType<MyCoolReward> = Type()
    }
    override fun type(): QuestRewardType<MyCoolReward> = TYPE
    private class Type : QuestRewardType<MyCoolReward> {
        override fun id(): Identifier = Identifier.of(MOD_ID, "mycoolreward_id")

        override fun codec(id: String): MapCodec<MyCoolReward> =
            RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    RecordCodecBuilder.point<MyCoolReward, String>(id),

                    QuestElementDisplay.CODEC
                        .forGetter(MyCoolReward::appearance),

                    Codec.STRING
                        .optionalFieldOf("someConfigProperty", "")
                        .forGetter(MyCoolReward::someConfigProperty),

                ).apply(instance, ::MyCoolReward)
            }
    }
}
```

## Widgets & Settings

This is what is displayed on the client in the Quest Editor & Viewer.

### Defining Settings

**Settings** lets you edit the actual config properties for your task & rewards in the in-game editor -- It's then serialized into JSON into the Heracles quest files.

![Settings](./settings.png)

In order to define your settings, you can use the `AbstractSettingInitializer` class provided by Iolaus. Note that some methods here are provided for Kotlin inference and convenience.

We then override 2x `create()` methods, one is to "put" settings, and the other is to "get" settings and provide default values if necessary (i.e. for missing or malformed JSON properties).

```kotlin
package com.example.mymod.client.settings

class MyCoolTaskSettings private constructor() : AbstractSettingInitializer<MyCoolTask>() {
    companion object {
        @JvmField
        val INSTANCE = MyCoolTaskSettings()
    }
    override fun create(task: MyCoolTask?): SettingInitializer.CreationData {
        val settings = SettingInitializer.CreationData()
        settings.put("someConfigProperty", textSetting(), task?.someConfigProperty ?: "")
        settings.putAppearance(task)
        settings.put("amount", intSetting(), task?.amount ?: 1)
        return settings
    }
    override fun create(
        id: String, task: MyCoolTask?, data: SettingInitializer.Data
    ): MyCoolTask {
        val result = MyCoolTask(
            id,
            data.get("someConfigProperty", textSetting()).orElse(task?.someConfigProperty ?: ""),
            data.getAppearance(task),
            data.get("amount", intSetting()).orElse(task?.amount ?: 1)
        )
        return result
    }
}
```

### Defining Widgets

**Widgets** represent the visual components for your task or reward in the viewer. They're responsible for rendering the task/reward title, description, icon, etc.
Widgets can use task/reward properties to dynamically render content based on the current state or configuration.

![Widgets](./widget.png)

Depending on if you're extending a Task or Reward, there are several [abstract classes](https://github.com/hudsonm62/Iolaus/blob/master/common/src/main/kotlin/com/ziroau/iolaus/api/client/widgets) provided by Iolaus for creating widgets.

Let's create a widget class for our `MyCoolTask`, which uses `AbstractProgressTaskWidget` which includes a builtin progress bar:

```kotlin
package com.example.mymod.client.widgets

import com.ziroau.iolaus.api.client.widgets.AbstractProgressTaskWidget
import com.example.mymod.MyCoolTask

class MyCoolTaskWidget (
    override val task: MyCoolTask,
    override val progress: TaskProgress<AbstractNbtNumber>
) : AbstractProgressTaskWidget<MyCoolInputData, AbstractNbtNumber, MyCoolTask>() {
    // this uses the overridden icon automatically if defined
    override val widgetItemIcon: QuestIcon<*>
    get() = task.icon()

    // Here we can define the title & description, if the overridden values are not provided by the settings
    //// This lets us define cool dynamic texts based on the element's current state or configuration too if you so desire
    override val widgetTitle: Text
        get() = task.titleOr(
            Text.translatable("mydefault.title.translation.key")
        )

    override val widgetDescription: Text
        get() = task.descriptionOr(
            Text.translatable("mydefault.description.translation.key")
        )
}
```

Or for `MyCoolReward`, you would extend `AbstractRewardWidget` instead as this class provides the button and layout for claiming the reward.

```kotlin
package com.example.mymod.client.widgets

import com.ziroau.iolaus.api.client.widgets.AbstractRewardWidget
import com.example.mymod.MyCoolReward

class MyCoolRewardWidget private constructor(
    override val reward: MyCoolReward,
    override val questId: String,
    override val progress: QuestProgress?,
    override val interactive: Boolean
) : AbstractRewardWidget() {
    companion object {
        @JvmStatic
        fun of(reward: MyCoolReward, interactive: Boolean): MyCoolRewardWidget {
            val (questId, progress) = getQuestContext()
            return MyCoolRewardWidget(
                reward, questId, progress, interactive
            )
        }
    }

    override val widgetItemIcon: QuestIcon<*>
        get() = reward.icon()

    override val widgetTitle: Text
        get() = reward.titleOr(
            Text.translatable("mydefault.title.translation.key")
        )

    override val widgetDescription: Text
        get() = reward.descriptionOr(
            Text.translatable("mydefault.description.translation.key")
        )
}
```

## Registering Everything

Of course, you'll need to actually register your tasks, rewards, widgets & settings for them to be recognized and used within Heracles.

Iolaus provides some functions you can run from your initialization code to quickly achieve this:

**From Common Initializer:**

```kotlin

```

**From Client Initializer:**

```kotlin

```
