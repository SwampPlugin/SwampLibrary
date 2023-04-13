package com.cosine.library.extension

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

fun Player.removeHand() = inventory.itemInMainHand.apply { amount -= 1 }

fun Player.giveItem(item: ItemStack) = inventory.addItem(item)