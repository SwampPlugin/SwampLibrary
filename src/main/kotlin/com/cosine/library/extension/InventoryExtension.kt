package com.cosine.library.extension

import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

fun Inventory.getEmptySlots(): Collection<Int> =
    List(storageContents.filter { it == null || it.type == Material.AIR }.size) { index -> index }

fun Inventory.isAddable(item: ItemStack): Boolean = storageContents.any {
    it == null || it.type == Material.AIR || (it.isSimilar(item) && it.amount + item.amount <= it.maxStackSize)
}