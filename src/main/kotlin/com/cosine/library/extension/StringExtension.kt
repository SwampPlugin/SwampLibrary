package com.cosine.library.extension

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.ChatColor
import java.util.*

val String.component get() = Component.text(this)

val Component.text get() = PlainTextComponentSerializer.plainText().serialize(this)

val String.uuid get() = UUID.fromString(this)

fun String.applyColor(): String = ChatColor.translateAlternateColorCodes('&', this)

fun MutableList<String>.applyColors(): MutableList<String> {
    return this.map(String::applyColor).toMutableList()
}

fun String.removeColor() = ChatColor.stripColor(this)

fun String.isInt(): Boolean {
    return try {
        this.toInt()
        true
    } catch (e: NumberFormatException) {
        false
    }
}

fun String.isNotInt(): Boolean {
    return try {
        this.toInt()
        false
    } catch (e: NumberFormatException) {
        true
    }
}