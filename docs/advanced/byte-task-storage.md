# Byte Task Storage

Iolaus adds a [`ByteTaskStorage`](https://github.com/hudsonm62/Iolaus/blob/master/common/src/main/kotlin/com/ziroau/iolaus/api/tasks/storage/ByteTaskStorage.kt) class
for storing an [`NbtByte`](https://maven.fabricmc.net/docs/yarn-1.21.1+build.1/net/minecraft/nbt/NbtByte.html).

You'll _likely_ only use this to create boolean-style tasks, where each task can be either true or false.
To accompany this, you can use [`BooleanQuestTask`](https://github.com/hudsonm62/Iolaus/blob/master/common/src/main/kotlin/com/ziroau/iolaus/api/tasks/BooleanQuestTask.kt) & [`AbstractBooleanTaskWidget`](https://github.com/hudsonm62/Iolaus/blob/master/common/src/main/kotlin/com/ziroau/iolaus/api/client/widgets/AbstractBooleanTaskWidget.kt)
for easily creating and displaying `true`/`false` tasks.

!!! tip

    You don't have to use `BooleanQuestTask` to use `AbstractBooleanTaskWidget`.

    It will accept any generic quest task type as long as `TaskProgress` is of type, `NbtByte`.

![incomplete sample](/assets/images/bool-quest-incomplete.png) ![complete sample](/assets/images/bool-quest-complete.png)

In my opinion, this looks MUCH nicer than tracking with, let's say, `0/1` progress. It also limits the possible values & reduces errors.

!!! warning

    For mod developers (and _not_ modpack makers), you may want to reconsider using [`CountedQuestTask`](https://github.com/hudsonm62/Iolaus/blob/master/common/src/main/kotlin/com/ziroau/iolaus/api/tasks/CountedQuestTask.kt)
    instead if you want to allow your users to still be able to force the single task to have be "accomplished" multiple times to be marked completed.

---

For example:

```kotlin
// Define a task
class BoolTestTask(
    override val taskId: String,
    override val display: BasicQuestElementDisplay
) : BooleanQuestTask<BoolTestTask> {

    // The underlying input is literally just a [Boolean]
    // Test the task type with True and this task will be "completed"

    override fun type(): QuestTaskType<BoolTestTask> = TYPE
    companion object {
        @JvmField
        val TYPE: QuestTaskType<BoolTestTask> =
            QuestTypesFactory.task(
                Identifier.of(IolausTestFabric.MOD_ID, "booltest_task")
            ) { id ->
                RecordCodecBuilder.mapCodec { instance ->
                    instance.group(
                        RecordCodecBuilder.point<BoolTestTask, String>(id),
                        QuestElementDisplay.CODEC
                            .forGetter(BoolTestTask::display),
                    ).apply(instance, ::BoolTestTask)
                }
            }
    }
}
```

```kotlin
// Use the `AbstractBooleanTaskWidget`
class BoolTestTaskWidget(
    override val task: BoolTestTask,
    override val progress: TaskProgress<NbtByte>
) : AbstractBooleanTaskWidget<Boolean, BoolTestTask>() {
    override val widgetItemIcon: QuestIcon<*>
        get() = task.icon()
    override val widgetTitle: Text
        get() = task.titleOr(Text.literal("Boolean Test Task"))
    override val widgetDescription: Text
        get() = task.descriptionOr(Text.literal("This is a test task for byte storage with Booleans"))
}
```

When registering the widget on the client, `AbstractBooleanTaskWidget` handles the display text, so we can just pass in an empty string for the formatter:

```kotlin
TasksClientRegistry.register(
    BoolTestTask.TYPE,
    BoolTestTaskSettings.INSTANCE,
    ::BoolTestTaskWidget
) { _, _ -> "" } // handled by widget
```

!!! success

    You can also see the [test implementation here](https://github.com/hudsonm62/Iolaus/tree/master/fabric/src/testmod/kotlin/com/ziroau/iolaus_testmod/tasks/BoolTestTask.kt).
