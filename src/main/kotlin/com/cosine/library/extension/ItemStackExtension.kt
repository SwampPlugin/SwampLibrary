package com.cosine.library.extension

import com.google.gson.Gson
import net.minecraft.nbt.NBTTagCompound
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_19_R2.inventory.CraftItemStack
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import kotlin.reflect.KClass

val ItemStack?.isAir get() = this?.type?.equals(Material.AIR) == true

fun ItemStack.amount(amount: Int = 1): ItemStack {
    this.amount = amount
    return this
}

fun ItemStack.setDisplayName(displayName: String): ItemStack {
    return apply { itemMeta = itemMeta?.apply { setDisplayName(displayName) } }
}

fun ItemStack.setNewLore(vararg lores: String?): ItemStack {
    return apply { itemMeta = itemMeta?.apply { lore = lores.filterNotNull() } }
}

fun ItemStack.setLore(vararg lores: String?): ItemStack {
    return apply {
        itemMeta = itemMeta?.apply {
            val newLore = mutableListOf<String>()
            lore?.let { newLore.addAll(it) }
            newLore.addAll(lores.filterNotNull())
            lore = newLore
        }
    }
}

fun ItemStack.hideAllItemFlags(): ItemStack {
    return apply { itemMeta = itemMeta?.apply { addItemFlags(*ItemFlag.values()) } }
}

fun ItemStack.setGlow(): ItemStack {
    return apply {
        addUnsafeEnchantment(Enchantment.LURE, 1)
        itemMeta = itemMeta?.apply {
            addItemFlags(ItemFlag.HIDE_ENCHANTS)
        }
    }
}

fun ItemStack.hasCustomModelData() = itemMeta.hasCustomModelData()

val ItemStack.customModelData get() = if (itemMeta.hasCustomModelData()) itemMeta.customModelData else 0

inline fun <reified T> ItemStack.addNBTTag(obj: T): ItemStack {
    itemMeta = CraftItemStack.asBukkitCopy(
        CraftItemStack.asNMSCopy(this).apply {
            c((u() ?: NBTTagCompound()).apply {
                a(T::class.simpleName, Gson().toJson(obj))
            })
        }
    ).itemMeta
    return this
}

inline fun <reified T : Any> ItemStack.getNBTTag(clazz: KClass<T>): T? {
    val jsonData = CraftItemStack.asNMSCopy(this).u()?.l(T::class.simpleName) ?: return null
    return Gson().fromJson(jsonData, clazz.java)
}

inline fun <reified T : Any> ItemStack.hasNBTTag(clazz: KClass<T>): Boolean {
    return CraftItemStack.asNMSCopy(this).u()?.e(T::class.java.simpleName) ?: false
}

inline fun <reified T : Any> ItemStack.removeNBTTag(clazz: KClass<T>): ItemStack {
    itemMeta = CraftItemStack.asBukkitCopy(
        CraftItemStack.asNMSCopy(this).apply {
            u()?.r(T::class.java.simpleName)
        }
    ).itemMeta
    return this
}