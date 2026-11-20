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
import net.minecraft.client.gui.DrawContext

/**
 * An Icon renderer for a widget.
 * Useful for rendering entities or other custom elements that isn't the standard item icon.
 *
 * @see EntityIconRenderer
 */
fun interface WidgetIconRenderer {
    fun render(context: DrawContext, scissor: ScissorBoxStack, x: Int, y: Int, size: Int): Boolean
}
