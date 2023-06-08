package com.cosine.library.permission

import org.bukkit.entity.Player
import java.util.UUID

object SwampPermission {

    private val permissions = mutableMapOf<UUID, MutableSet<String>>()

    fun init(player: Player) {
        val uuid = player.uniqueId
        if (!permissions.containsKey(uuid)) {
            permissions[uuid] = mutableSetOf()
        }
    }

    fun hasPermission(player: Player, permission: String): Boolean {
        return permissions[player.uniqueId]?.contains(permission) == true
    }

    fun addPermission(player: Player, permission: String) {
        permissions[player.uniqueId]?.add(permission)
    }

    fun removePermission(player: Player, permission: String) {
        permissions[player.uniqueId]?.remove(permission)
    }

    fun clearPermission(player: Player) {
        permissions[player.uniqueId]?.clear()
    }
}