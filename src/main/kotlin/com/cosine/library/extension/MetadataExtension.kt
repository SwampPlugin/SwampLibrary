package com.cosine.library.extension

import com.cosine.library.SwampLibrary.Companion.plugin
import org.bukkit.block.Block
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.metadata.MetadataValue

fun createMetadata(any: Any?): FixedMetadataValue = FixedMetadataValue(plugin, any)

fun Block.removeMetadata(key: String) {
    removeMetadata(key, plugin)
}

fun List<MetadataValue>.getString(): String = first().asString()

fun List<MetadataValue>.getBoolean(): Boolean = first().asBoolean()

fun List<MetadataValue>.getInt(): Int = first().asInt()