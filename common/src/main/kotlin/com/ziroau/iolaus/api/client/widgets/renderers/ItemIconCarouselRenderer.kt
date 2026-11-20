/*
 * Copyright (c) 2026 Hudson M.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ziroau.iolaus.api.client.widgets.renderers

import com.teamresourceful.resourcefullib.client.scissor.ScissorBoxStack
import com.teamresourceful.resourcefullib.client.utils.RenderUtils
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

/**
 * Renders a carousel of item icons, cycling at a fixed interval
 *
 * - Sample: `ItemIconCarouselRenderer.of(Items.DIAMOND, Items.GOLD_INGOT)`
 *
 * @see WidgetIconRenderer
 * @property icons The list of item stacks to cycle through
 * @property intervalMs The interval in milliseconds between icon changes
 */
class ItemIconCarouselRenderer private constructor(
    private val icons: List<ItemStack>,
    private val intervalMs: Long
) : WidgetIconRenderer {
    override fun render(
        context: DrawContext, scissor: ScissorBoxStack,
        x: Int, y: Int, size: Int
    ): Boolean {
        if (icons.isEmpty() || size <= 0) return false

        // current item index
        val index = ((System.currentTimeMillis() / intervalMs) % icons.size).toInt()

        try {
            RenderUtils.createScissorBoxStack(
                scissor, MinecraftClient.getInstance(),
                context.matrices, x, y, size, size
            ).use {
                with(context.matrices) {
                    push()
                    try {
                        translate(x.toDouble(), y.toDouble(), 0.0)
                        scale(size / 16f, size / 16f, 1f)

                        context.drawItem(icons[index], 0, 0)
                    } finally {
                        pop()
                    }
                }
            }
            return true

        } catch (e: Exception) { return false }
    }

    companion object {
        private const val DEFAULT_INTERVAL = 1000L

        @JvmStatic @JvmOverloads
        fun of(
            icons: List<Item>, intervalMs: Long = DEFAULT_INTERVAL
        ) = ItemIconCarouselRenderer(
            icons.map { it.defaultStack }, intervalMs
        )
        @JvmStatic @JvmOverloads
        @JvmName("ofStacks")
        fun of(
            iconStacks: List<ItemStack>, intervalMs: Long = DEFAULT_INTERVAL
        ) = ItemIconCarouselRenderer(
            iconStacks.map { it.copy() }, intervalMs
        )

        @JvmStatic
        fun of(vararg icons: Item) = of(icons.toList())
        @JvmStatic
        fun of(vararg stacks: ItemStack) = of(stacks.toList())

        fun of(intervalMs: Long, vararg icons: Item) = of(icons.toList(), intervalMs)
        fun of(intervalMs: Long, vararg stacks: ItemStack) = of(stacks.toList(), intervalMs)
    }
}
