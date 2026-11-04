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

@file:JvmName("EnumSettings")

package com.ziroau.iolaus.api.client.settings

import earth.terrarium.heracles.api.client.settings.base.EnumSetting
import net.minecraft.util.StringIdentifiable

/**
 * Creates a Heracles [EnumSetting] for a specific enum type.
 * Requires that the enum implements [StringIdentifiable] to provide a string representation for each value.
 *
 * Sample: `private val veryCoolEnum = enumSetting(MyCoolEnumType.SICK_VALUE)`
 *
 * @param E The enum type, which must implement [StringIdentifiable].
 * @param default The initially selected value.
 * @return An [EnumSetting] for selecting values.
 */
inline fun <reified E> enumSetting(
    default: E
): EnumSetting<E> where E : Enum<E>, E : StringIdentifiable =
    EnumSetting(E::class.java, default)

/**
 * Java-compatible overload of [enumSetting].
 *
 * @param type The enum class.
 * @param default The initially selected value.
 */
fun <E> enumSetting(
    type: Class<E>, default: E
): EnumSetting<E> where E : Enum<E>, E : StringIdentifiable =
    EnumSetting(type, default)
