package com.cosine.library.extension

import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType

fun Player.getTag(key: NamespacedKey): String? {
    return persistentDataContainer.get(key, PersistentDataType.STRING)
}

fun Player.setTag(key: NamespacedKey, value: String) {
    persistentDataContainer.set(key, PersistentDataType.STRING, value)
}

fun Player.removeTag(key: NamespacedKey) {
    persistentDataContainer.remove(key)
}

fun Player.hasTag(key: NamespacedKey): Boolean {
    return persistentDataContainer.has(key)
}