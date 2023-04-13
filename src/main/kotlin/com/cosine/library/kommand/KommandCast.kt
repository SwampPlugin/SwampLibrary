package com.cosine.library.kommand

import kotlin.reflect.KClass

class KommandCast<T : BaseType<String>>(private val type: KClass<T>) : Castable<T> {
    override fun cast(string: String?): T {
        return type.java.enumConstants.firstOrNull {
            if (string != null) { it.text = string }
            it.text == string
        } ?: throw IllegalArgumentException("올바르지 않은 파라미터입니다. (${type::class.java.name})")
    }
}