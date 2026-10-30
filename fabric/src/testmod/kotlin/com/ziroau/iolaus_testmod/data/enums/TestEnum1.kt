package com.ziroau.iolaus_testmod.data.enums

import net.minecraft.util.StringIdentifiable

enum class TestEnum1 : StringIdentifiable {
    VALUE1,
    VALUE2;

    override fun asString(): String {
        return name.lowercase()
    }
}
