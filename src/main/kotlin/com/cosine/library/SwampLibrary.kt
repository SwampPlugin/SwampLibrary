package com.cosine.library

import com.cosine.library.inventory.InventoryListener
import com.cosine.library.permission.PermissionListener
import com.cosine.library.util.ItemStackLocale
import org.bukkit.plugin.java.JavaPlugin

class SwampLibrary : JavaPlugin() {

    companion object {
        internal lateinit var plugin: JavaPlugin
            private set
    }

    override fun onLoad() {
        plugin = this
    }

    override fun onEnable() {
        ItemStackLocale.init(this)
        server.pluginManager.registerEvents(InventoryListener(), this)
        server.pluginManager.registerEvents(PermissionListener(), this)
    }
}