package com.cosine.library.inventory

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class InventoryListener: Listener {

    @EventHandler
    fun onClick(event: InventoryClickEvent) {
        event.view.topInventory.holder?.apply {
            if (this is InventoryBuilder) onInventoryClick(event)
        }
    }

    @EventHandler
    fun onClose(event: InventoryCloseEvent) {
        event.view.topInventory.holder?.apply {
            if (this is InventoryBuilder) onInventoryClose(event)
        }
    }

}