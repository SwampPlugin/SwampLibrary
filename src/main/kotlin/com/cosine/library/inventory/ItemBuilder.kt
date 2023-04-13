package com.cosine.library.inventory

import org.bukkit.*
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

class ItemBuilder(
    material: Material,
    amount: Int = 1
) {
    private var itemStack: ItemStack
    private var itemMeta: ItemMeta

    init {
        itemStack = ItemStack(material, amount)
        itemMeta = itemStack.itemMeta!!
    }

    fun setDisplayName(displayName: String): ItemBuilder {
        itemMeta.setDisplayName(displayName)
        return this
    }

    fun setLore(vararg lore: String?): ItemBuilder {
        itemMeta.lore = lore.filterNotNull()
        return this
    }

    fun setGlow(): ItemBuilder {
        itemStack.itemMeta = itemMeta
        itemStack.addUnsafeEnchantment(Enchantment.LURE, 1)
        itemMeta = itemStack.itemMeta!!
        addItemFlags(ItemFlag.HIDE_ENCHANTS)
        return this
    }

    fun addItemFlags(vararg flags: ItemFlag): ItemBuilder {
        itemMeta.addItemFlags(*flags)
        return this
    }

    fun setUnbreakable(): ItemBuilder {
        itemMeta.isUnbreakable = true
        return this
    }

    fun build(): ItemStack {
        itemStack.itemMeta = itemMeta
        return itemStack
    }
}