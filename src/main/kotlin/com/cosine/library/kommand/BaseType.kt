package com.cosine.library.kommand

interface BaseType<T> {
    var text: T

    companion object {
        inline fun <reified T> get(text: String): T where T : Enum<T>, T : BaseType<String> {
            return enumValues<T>().firstOrNull {
                it.text = text
                it.text == text
            } ?: throw IllegalArgumentException("No enum constant with property value $text for enum ${T::class.java.name}")
        }
    }
}