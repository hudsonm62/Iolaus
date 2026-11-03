package com.ziroau.iolaus.api.client.widgets.renderers.utility

import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.SkinTextures
import net.minecraft.component.type.ProfileComponent
import java.util.concurrent.CancellationException
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionException

/**
 * Caches asynchronously resolved player skin textures
 */
object PlayerHeadSkinCache {
    private const val MAX_ENTRIES = 128
    private val cache = LinkedHashMap<ProfileComponent, CompletableFuture<SkinTextures>>(16, 0.75f, true)

    /**
     * Returns the skin textures if they have finished loading
     * and returns null while loading or on failure
     */
    @Synchronized
    fun get(profile: ProfileComponent): SkinTextures? {
        val future = cache[profile] ?: createFuture(profile)

        // Don't block the rendering thread
        if (!future.isDone || future.isCancelled) return null

        return try {
            future.getNow(null)
        } catch (_: CompletionException) {
            null
        } catch (_: CancellationException) {
            null
        }
    }

    /**
     * Starts an asynchronous lookup for the skin textures of a player profile
     */
    private fun createFuture(
        profile: ProfileComponent
    ): CompletableFuture<SkinTextures> {
        val client = MinecraftClient.getInstance()
        val future = profile.future.thenCompose { resolved ->
            client.skinProvider.fetchSkinTextures(resolved.gameProfile())
        }

        cache[profile] = future

        // Evict the least recently used entry (when necessary)
        while (cache.size > MAX_ENTRIES) {
            val iterator = cache.entries.iterator()
            if (!iterator.hasNext()) break
            iterator.next()
            iterator.remove()
        }
        return future
    }

    /**
     * Clears the cached skin lookup futures
     */
    @Synchronized
    fun clear() = cache.clear()
}
