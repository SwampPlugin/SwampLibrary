package com.cosine.library.util

import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.plugin.java.JavaPlugin

class ChatSettingListener(private val plugin: JavaPlugin) {

    private val listener = object: Listener {}

    fun listen(block: (AsyncPlayerChatEvent) -> Unit) {
        plugin.server.pluginManager.registerEvent(AsyncPlayerChatEvent::class.java, listener, EventPriority.LOWEST, { _, event ->
            block(event as AsyncPlayerChatEvent)
            AsyncPlayerChatEvent.getHandlerList().unregister(listener)
        }, plugin)
    }
}