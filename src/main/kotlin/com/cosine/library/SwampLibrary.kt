package com.cosine.library

import com.cosine.library.inventory.InventoryListener
import org.bukkit.plugin.java.JavaPlugin

class SwampLibrary : JavaPlugin() {

    companion object {
        internal lateinit var plugin: JavaPlugin
    }

    override fun onLoad() {
        plugin = this
    }

    override fun onEnable() {
        server.pluginManager.registerEvents(InventoryListener(), this)
    }
}