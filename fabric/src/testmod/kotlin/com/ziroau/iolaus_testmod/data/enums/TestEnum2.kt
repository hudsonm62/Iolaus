package com.ziroau.iolaus_testmod.data.enums

import net.minecraft.util.StringIdentifiable

enum class TestEnum2 : StringIdentifiable {
    VALUE3,
    VALUE4;

    override fun asString(): String {
        return name.lowercase()
    }
}
