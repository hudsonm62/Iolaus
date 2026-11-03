
package com.ziroau.iolaus.api.client.widgets.renderers

import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import com.mojang.authlib.properties.PropertyMap
import com.teamresourceful.resourcefullib.client.scissor.ScissorBoxStack
import com.teamresourceful.resourcefullib.client.utils.RenderUtils
import com.ziroau.iolaus.api.client.widgets.renderers.PlayerHeadIconRenderer.Companion.fallback
import com.ziroau.iolaus.api.client.widgets.renderers.utility.PlayerHeadRenderMode
import com.ziroau.iolaus.api.client.widgets.renderers.utility.PlayerHeadSkinCache
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.PlayerSkinDrawer
import net.minecraft.client.util.DefaultSkinHelper
import net.minecraft.client.util.SkinTextures
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.ProfileComponent
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import java.util.*

/**
 * Renders a player's head as a widget icon.
 *
 * Supports player usernames, [UUID], [GameProfile] & Base64 Textures.
 *
 * **Warning**: This doesn't handle fallback like other renderers.
 * You should handle fallback yourself in your widget, if you don't want to show a Steve head when the player skin is missing.
 *
 * TODO: Error handling needs improving
 */
class PlayerHeadIconRenderer private constructor(
    private val profile: ProfileComponent?,
    private val head: ItemStack,
    private val mode: PlayerHeadRenderMode = PlayerHeadRenderMode.FLAT
) : WidgetIconRenderer {
    override fun render(
        context: DrawContext, scissor: ScissorBoxStack,
        x: Int, y: Int, size: Int
    ): Boolean {
        val client = MinecraftClient.getInstance()

        RenderUtils.createScissorBoxStack(
            scissor, client,
            context.matrices,
            x, y,
            size, size
        ).use {
            // Start or retrieve the cached skin lookup
            val textures = profile?.let(PlayerHeadSkinCache::get)
            when (mode) {
                PlayerHeadRenderMode.FLAT -> {
                    renderFlat(
                        context, textures,
                        x, y, size
                    )
                }
                PlayerHeadRenderMode.THREE_DIMENSIONAL -> {
                    renderThreeDimensional(
                        context, textures != null,
                        x, y, size
                    )
                }
            }
        }
        return true
    }

    /**
     * Renders a flat player skin portrait
     *
     * Uses the default Minecraft skin while the actual
     * player skin is loading
     */
    private fun renderFlat(
        context: DrawContext, textures: SkinTextures?,
        x: Int, y: Int, size: Int
    ) {
        // A valid profile is still loading
        if (textures == null && profile != null) return

        val texture = textures?.texture() ?: DefaultSkinHelper.getTexture()
        PlayerSkinDrawer.draw(
            context, texture,
            x, y, size,
            true,
            false
        )
    }

    /**
     * Renders a 3D player head item
     *
     * Uses a Steve head until the actual skin loads
     */
    private fun renderThreeDimensional(
        context: DrawContext, skinLoaded: Boolean,
        x: Int, y: Int, size: Int
    ) {
        // A valid profile is still loading
        if (!skinLoaded && profile != null) return

        val matrices = context.matrices
        val stack = if (skinLoaded) head else ItemStack(Items.PLAYER_HEAD)
        matrices.push()
        try {
            // Position at the centre of the widget
            matrices.translate(
                x + size / 2f,
                y + size / 2f - 1f,
                0f
            )

            // Scale around the centre
            val scale = size / 16f
            matrices.scale(scale, scale, scale)

            // Draw centred on the origin
            context.drawItem(stack, -8, -8)
        } finally {
            matrices.pop()
        }
    }

    companion object {
        /**
         * Creates a head renderer from a player name
         */
        @JvmStatic
        fun of(name: String): PlayerHeadIconRenderer {
            return of(name, PlayerHeadRenderMode.FLAT)
        }

        @JvmStatic
        fun of(name: String, mode: PlayerHeadRenderMode): PlayerHeadIconRenderer {
            val profile = ProfileComponent(
                Optional.of(name),
                Optional.empty(),
                PropertyMap()
            )
            return from(profile, mode)
        }

        /**
         * Creates a head renderer from a player [UUID]
         */
        @JvmStatic
        fun of(uuid: UUID): PlayerHeadIconRenderer {
            return of(uuid, PlayerHeadRenderMode.FLAT)
        }

        @JvmStatic
        fun of(uuid: UUID, mode: PlayerHeadRenderMode): PlayerHeadIconRenderer {
            val profile = ProfileComponent(
                Optional.empty(),
                Optional.of(uuid),
                PropertyMap()
            )
            return from(profile, mode)
        }

        /**
         * Creates a head renderer from an existing [GameProfile]
         */
        @JvmStatic
        fun of(profile: GameProfile): PlayerHeadIconRenderer {
            return of(profile, PlayerHeadRenderMode.FLAT)
        }

        @JvmStatic
        fun of(profile: GameProfile, mode: PlayerHeadRenderMode): PlayerHeadIconRenderer {
            return from(ProfileComponent(profile), mode)
        }

        /**
         * Creates a head renderer from a [ProfileComponent]
         */
        private fun from(profile: ProfileComponent, mode: PlayerHeadRenderMode): PlayerHeadIconRenderer {
            val stack = ItemStack(Items.PLAYER_HEAD)
            stack.set(
                DataComponentTypes.PROFILE, profile
            )
            return PlayerHeadIconRenderer(
                profile, stack, mode
            )
        }

        /**
         * Clears all cached player skin lookup futures
         */
        @JvmStatic
        fun clearSkinCache() {
            PlayerHeadSkinCache.clear()
        }


        /////////// BASE64 Texture Support Below ///////////

        /**
         * Creates a head renderer from a Base64-encoded texture payload.
         *
         * The payload should be the value of the "textures" profile property.
         */
        @JvmStatic
        fun ofTexture(
            base64: String
        ): PlayerHeadIconRenderer = ofTexture(base64, PlayerHeadRenderMode.FLAT)
        @JvmStatic
        fun ofTexture(
            base64: String, mode: PlayerHeadRenderMode
        ): PlayerHeadIconRenderer = fromTexture(base64, null, mode)

        /**
         * Creates a head renderer from a signed Base64 texture payload.
         */
        @JvmStatic
        fun ofSignedTexture(
            base64: String, signature: String
        ): PlayerHeadIconRenderer = ofSignedTexture(base64, signature, PlayerHeadRenderMode.FLAT)
        @JvmStatic
        fun ofSignedTexture(
            base64: String, signature: String, mode: PlayerHeadRenderMode
        ): PlayerHeadIconRenderer = fromTexture(base64, signature, mode)

        /**
         * Creates a renderer using a custom texture property.
         * Returns [fallback] Steve head if missing/invalid.
         */
        private fun fromTexture(
            base64: String?, signature: String?, mode: PlayerHeadRenderMode
        ): PlayerHeadIconRenderer {
            // Missing or empty texture uses fallback
            val texture = base64?.trim()?.takeIf { it.isNotEmpty() }
                ?: return fallback(mode)

            // Validate or reject with fallback
            val decoded = try {
                Base64.getDecoder().decode(texture)
            } catch (_: IllegalArgumentException) {
                return fallback(mode)
            }
            if (decoded.isEmpty()) return fallback(mode)

            // Create profile properties with the texture and optional signature
            val properties = PropertyMap()
            val textureProperty = signature
                ?.takeIf { it.isNotBlank() }
                ?.let { Property("textures", texture, it) }
                ?: Property("textures", texture)

            properties.put("textures", textureProperty)

            // Generate a UUID for this texture
            val uuid = UUID.nameUUIDFromBytes(texture.toByteArray(Charsets.UTF_8))
            val profile = ProfileComponent(
                Optional.of("Custom"),
                Optional.of(uuid),
                properties
            )

            // Hand off to normal renderer with generated profile
            return from(profile, mode)
        }

        /**
         * Creates a renderer that displays the default Minecraft head.
         */
        private fun fallback(
            mode: PlayerHeadRenderMode
        ) = PlayerHeadIconRenderer(null, ItemStack(Items.PLAYER_HEAD), mode)
    }
}
