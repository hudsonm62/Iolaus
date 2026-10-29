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
