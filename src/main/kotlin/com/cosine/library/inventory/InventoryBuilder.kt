package com.cosine.library.inventory

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

abstract class InventoryBuilder(
    private val display: String = "",
    private val row: Int = 1,
    private val isCancelled: Boolean = false,
    private val inventoryType: InventoryType = InventoryType.CHEST,
): InventoryHolder {

    private var inv: Inventory? = null

    override fun getInventory(): Inventory {
        return inv ?: (
                if (inventoryType == InventoryType.CHEST) Bukkit.createInventory(this, row * 9, display)
                else Bukkit.createInventory(this, inventoryType, display))
            .apply { inv = this }
    }

    abstract fun init(inventory: Inventory)

    open fun initWithPlayer(inventory: Inventory, player: Player) {}

    open fun postInitialized(inventory: Inventory, player: Player) {}

    fun openInventory(player: Player) {
        init(inventory)
        initWithPlayer(inventory, player)
        player.openInventory(inventory)
        postInitialized(inventory, player)
    }

    internal fun onInventoryClick(event: InventoryClickEvent) {
        if (isCancelled) event.isCancelled = true
        onClick(event)
    }

    internal fun onInventoryClose(event: InventoryCloseEvent) {
        onClose(event)
    }

    protected open fun onClick(event: InventoryClickEvent) {}

    protected open fun onClose(event: InventoryCloseEvent) {}
}