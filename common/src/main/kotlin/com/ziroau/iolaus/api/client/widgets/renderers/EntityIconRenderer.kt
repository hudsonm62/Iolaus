package com.ziroau.iolaus.api.client.widgets.renderers

import com.teamresourceful.resourcefullib.client.scissor.ScissorBoxStack
import com.teamresourceful.resourcefullib.client.utils.RenderUtils
import com.teamresourceful.resourcefullib.common.codecs.predicates.RestrictedEntityPredicate
import earth.terrarium.heracles.api.client.WidgetUtils
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType

/**
 * Draws Heracles-style rendered entity icons, clipped to the icon bounds
 *
 * - Sample: `EntityIconRenderer.of(EntityType.ZOMBIE)`
 * - Sample: `EntityIconRenderer.from { world -> EntityType.ZOMBIE.create(world)?.also { entity -> /* configure here */ } }`
 *
 */
class EntityIconRenderer private constructor(
    private val factory: (ClientWorld) -> Entity?
) : WidgetIconRenderer {
    private var cachedWorld: ClientWorld? = null
    private var cachedEntity: Entity? = null

    override fun render(
        context: DrawContext, scissor: ScissorBoxStack,
        x: Int, y: Int, size: Int
    ): Boolean {
        val client = MinecraftClient.getInstance()
        val world = client.world ?: run {
            cachedWorld = null
            cachedEntity = null
            return false
        }

        if (world !== cachedWorld) {
            cachedWorld = world
            cachedEntity = factory(world)
        }

        val entity = cachedEntity ?: return false
        RenderUtils.createScissorBoxStack(
            scissor, client, context.matrices, x, y, size, size
        ).use {
            WidgetUtils.drawEntity(context, x, y, size, entity)
        }
        return true
    }

    companion object {
        /**
         * Creates a standard preview of an entity type
         */
        @JvmStatic
        fun of(type: EntityType<*>): EntityIconRenderer = EntityIconRenderer { world -> type.create(world) }

        /**
         * Creates a standard preview of an entity type by its string ID
         */
        @JvmStatic
        fun of(id: String): EntityIconRenderer? =
            EntityType.get(id)
                .map { type -> of(type) }
                .orElse(null)

        /**
         * Creates an entity preview from a [RestrictedEntityPredicate],
         * including any custom [net.minecraft.nbt.NbtCompound] data.
         */
        @JvmStatic
        fun of(predicate: RestrictedEntityPredicate): EntityIconRenderer =
            from { world ->
                predicate.entityType().create(world)?.also { entity ->
                    predicate.tag.ifPresent { tag ->
                        entity.readNbt(tag.copy())
                    }
                }
            }

        /**
         * For previews that require NBT or other special configuration.
         */
        @JvmStatic
        fun from(factory: (ClientWorld) -> Entity?): EntityIconRenderer = EntityIconRenderer(factory)
    }
}
