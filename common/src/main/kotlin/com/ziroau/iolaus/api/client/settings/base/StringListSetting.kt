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

package com.ziroau.iolaus.api.client.settings.base

import com.ziroau.iolaus.api.client.settings.textbox.BorderedMultiLineTextBox
import earth.terrarium.heracles.api.client.settings.Setting
import earth.terrarium.heracles.client.components.widgets.textbox.editor.TextHighlighter
import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text

/**
 * Creates a settings entry that lets you edit a multiline list of strings.
 * Each line is treated as a separate string, and empty lines are stripped out.
 *
 * Uses Iolaus' [BorderedMultiLineTextBox] to allow for multiline editing.
 */
class StringListSetting : Setting<List<String>, BorderedMultiLineTextBox> {
    companion object {
        @JvmField
        val INSTANCE = StringListSetting()

        private val highlighter = object : TextHighlighter {
            override fun highlight(text: String): Text = Text.literal(text)
            override fun getTextColor(): Int = 0xFFE0E0E0.toInt()
        }
    }

    override fun createWidget(
        old: BorderedMultiLineTextBox?, width: Int, value: List<String>
    ): BorderedMultiLineTextBox {
        return BorderedMultiLineTextBox(
            old, MinecraftClient.getInstance().textRenderer,
            width, 100,
            highlighter
        ).apply {
            // Restores the existing value on initial creation - this preserves edits on window resize
            if (old == null) {
                setValue(value.joinToString("\n"))
            }
        }
    }

    override fun getValue(widget: BorderedMultiLineTextBox): List<String> {
        return widget.value
            .lineSequence()
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .toList()
    }
}
