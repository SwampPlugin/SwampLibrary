package com.cosine.library.permission

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PermissionListener : Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        SwampPermission.init(event.player)
    }
}