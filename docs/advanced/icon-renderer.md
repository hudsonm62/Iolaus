# Widget Icon Renderer

Iolaus provides a [`WidgetIconRenderer`](https://github.com/hudsonm62/Iolaus/blob/master/common/src/main/kotlin/com/ziroau/iolaus/api/client/widgets/renderers/WidgetIconRenderer.kt) interface
for handling and displaying advanced icons in widgets.

It should also be noted that the Item Icon defined in [`QuestElementDisplay`](https://github.com/hudsonm62/Iolaus/blob/master/common/src/main/kotlin/com/ziroau/iolaus/api/data/QuestElementDisplay.kt)
will always override the renderer icon, if one is provided.

```kotlin
class ExampleWidget (
    override val task: ExampleTask,
    override val progress: TaskProgress<AbstractNbtNumber>
) : AbstractProgressTaskWidget<Any, AbstractNbtNumber, ExampleTask>() {

    // Icon renderer for the entity specified in the task
    override val iconRenderer: WidgetIconRenderer? = SomeCoolRenderer(task.veryCoolIcon)
}
```

!!! note

    An `iconRenderer` field is available on all widgets inheriting [`AbstractDisplayWidget`](https://github.com/hudsonm62/Iolaus/blob/master/common/src/main/kotlin/com/ziroau/iolaus/api/client/widgets/AbstractDisplayWidget.kt).

## Entity Renderer

Iolaus provides an [`EntityIconRenderer`](https://github.com/hudsonm62/Iolaus/blob/master/common/src/main/kotlin/com/ziroau/iolaus/api/client/widgets/renderers/EntityIconRenderer.kt), which is a implementation of the [Widget Icon Renderer](#widget-icon-renderer).
The output looks identical to Heracles' existing entity rendering, as we're using the same underlying functions.

![Entity Icon Renderer Example](/assets/images/entity-renderer.png)

!!! tip

    See [`EntityRenderTestTask`](https://github.com/hudsonm62/Iolaus/blob/master/fabric/src/testmod/kotlin/com/ziroau/iolaus_testmod/tasks/EntityRenderTestTask.kt) for a working example of the Entity Renderer.

[ResourcefulLib](https://github.com/Team-Resourceful/ResourcefulLib) provides a serialized [`RestrictedEntityPredicate`](https://github.com/Team-Resourceful/ResourcefulLib/blob/1.21.1/common/src/main/java/com/teamresourceful/resourcefullib/common/codecs/predicates/RestrictedEntityPredicate.java)
that can be used to define and (de)serialize entities, which we can utilize for rendering purposes.

In your task/reward, define a `RestrictedEntityPredicate`:

```kotlin
class EntityRenderTestTask (
    override val taskId: String,
    override val display: QuestElementDisplay,
    val entity: RestrictedEntityPredicate
) {
    // implementation here
}
```

And in your serialization codec:

```kotlin
RecordCodecBuilder.mapCodec { instance ->
    instance.group(
        RecordCodecBuilder.point(id),

        QuestElementDisplay.CODEC
            .forGetter(EntityRenderTestTask::display),

        // Entity field for the renderer
        RestrictedEntityPredicate.CODEC
            .fieldOf("entity")
            .forGetter(EntityRenderTestTask::entity),
    ).apply(instance, ::EntityRenderTestTask)
}
```

---

In your settings, if you're using a `RestrictedEntityPredicate`, you need to define your serialization.
If you set this up correctly, you'll also get Autocomplete support for any of the `RestrictedEntityPredicate` fields in your settings.

![entity autocomplete](/assets/images/entity-autocomplete.png)

You can see a full example of this in the [`EntityRenderTestTaskSettings`](https://github.com/hudsonm62/Iolaus/blob/master/fabric/src/testmod/kotlin/com/ziroau/iolaus_testmod/client/settings/EntityRenderTestTaskSettings.kt).

---

Finally, we can use the `EntityIconRenderer` in our widget to render the entity icon based on the `RestrictedEntityPredicate` defined in our task.
This ensures that the icon displayed in the widget accurately represents the entity specified by the predicate:

```kotlin
class EntityRenderTestTaskWidget (
    override val task: EntityRenderTestTask,
    override val progress: TaskProgress<AbstractNbtNumber>
) : AbstractProgressTaskWidget<Any, AbstractNbtNumber, EntityRenderTestTask>() {

    // Icon renderer for the entity specified in the task
    override val iconRenderer: WidgetIconRenderer = EntityIconRenderer.of(task.entity)

    /** Takes precedence over the [iconRenderer] if present */
    override val widgetItemIcon: QuestIcon<*>
        get() = task.icon()
}
```

!!! note

    There are several overloads of `EntityIconRenderer.of` that allow you to render entities from different sources,
    such as `RestrictedEntityPredicate`, `EntityType`, or even directly from a `String` identifier.

## Creating Custom Icon Renderers

TBA

<!-- will cobblemon renderer as the example later -->
