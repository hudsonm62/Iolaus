# Widget Icon Renderer

Iolaus provides a [`WidgetIconRenderer`](https://github.com/hudsonm62/Iolaus/blob/master/common/src/main/kotlin/com/ziroau/iolaus/api/client/widgets/renderers/WidgetIconRenderer.kt) interface
for handling and displaying advanced icons in widgets.

Assuming you're using an [Iolaus based widget](https://github.com/hudsonm62/Iolaus/tree/master/common/src/main/kotlin/com/ziroau/iolaus/api/client/widgets),
it should also be noted that the Item Icon defined in [`QuestElementDisplay`](https://github.com/hudsonm62/Iolaus/blob/master/common/src/main/kotlin/com/ziroau/iolaus/api/data/QuestElementDisplay.kt)
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

Iolaus' [`EntityIconRenderer`](https://github.com/hudsonm62/Iolaus/blob/master/common/src/main/kotlin/com/ziroau/iolaus/api/client/widgets/renderers/EntityIconRenderer.kt) creates a 3D render of an entity to display as the icon.
The result looks identical to Heracles' existing entity rendering, as we're using the same underlying functions.

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
If you set this up correctly, you'll also get [Autocomplete support](../features/autocomplete.md) for any of the `RestrictedEntityPredicate` fields in your settings.

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

## Item Carousel Renderer

Iolaus' [`ItemIconCarouselRenderer`](https://github.com/hudsonm62/Iolaus/blob/master/common/src/main/kotlin/com/ziroau/iolaus/api/client/widgets/renderers/ItemIconCarouselRenderer.kt)
renders a "carousel" of items, which is used to rotate through a list of items in a widget. This could be particularly
useful for tasks and rewards that involve multiple blocks/items.

![item carousel](/assets/images/carousel-renderer.gif)

!!! tip

    You can pass in either [`ItemStack`](https://maven.fabricmc.net/docs/yarn-1.21.1+build.1/net/minecraft/item/ItemStack.html) or [`Item`](https://maven.fabricmc.net/docs/yarn-1.21.1+build.1/net/minecraft/item/package-summary.html) instances.

```kotlin
/**
 * This is just a test to show the ItemIconCarouselRenderer.
 *
 * In practice, you would/could allow Quest Editors to define
 * icons to be used, instead of hardcoding them like this.
 */
class VeryCoolWidget (
    override val task: VeryCoolTask,
    override val progress: TaskProgress<AbstractNbtNumber>
) : AbstractProgressTaskWidget<Any, AbstractNbtNumber, VeryCoolTask>() {
    override val iconRenderer = ItemIconCarouselRenderer.of(
        1000L, // Optional rotation interval in ms
        Items.ENDER_CHEST, Items.CHEST, Items.BARREL, Items.SHULKER_BOX
    )
    //... insert remaining implementation here
}
```

## Creating Custom Icon Renderers

TBA

<!-- will use cobblemon renderer as the example later -->
