package com.ziroau.iolaus.api.client.settings

import earth.terrarium.heracles.api.client.settings.base.EnumSetting
import net.minecraft.util.StringIdentifiable

/**
 * Creates a Heracles [earth.terrarium.heracles.api.client.settings.Setting] for an enum type.
 * Requires that the enum type implements [StringIdentifiable] to provide a string representation for each value.
 *
 * Sample: `private val veryCoolEnum = enumSetting(MyCoolEnumType.SICK_VALUE)`
 *
 * @param E The enum type, which must implement [StringIdentifiable].
 * @param default The initially selected value.
 * @return A setting for selecting enum values.
 */
inline fun <reified E> enumSetting(
    default: E
): EnumSetting<E> where E : Enum<E>, E : StringIdentifiable =
    EnumSetting(E::class.java, default)
