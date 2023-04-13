package com.cosine.library.extension

import com.google.gson.Gson
import net.minecraft.nbt.NBTTagCompound
import org.bukkit.craftbukkit.v1_19_R2.inventory.CraftItemStack
import org.bukkit.inventory.ItemStack

fun ItemStack.amount(amount: Int = 1): ItemStack {
    this.amount = amount
    return this
}

inline fun <reified T> ItemStack.addNBTTag(obj: T) {
    itemMeta = CraftItemStack.asBukkitCopy(
        CraftItemStack.asNMSCopy(this).apply {
            c((u() ?: NBTTagCompound()).apply {
                a(T::class.simpleName, Gson().toJson(obj))
            })
        }
    ).itemMeta
}

inline fun <reified T> ItemStack.getNBTTag(clazz: Class<T>): T? {
    val jsonData = CraftItemStack.asNMSCopy(this).u()?.l(T::class.simpleName) ?: return null
    return Gson().fromJson(jsonData, clazz)
}

inline fun <reified T> ItemStack.hasNBTTag(clazz: Class<T>): Boolean {
    return CraftItemStack.asNMSCopy(this).u()?.e(T::class.java.simpleName) ?: false
}

inline fun <reified T> ItemStack.removeNBTTag(clazz: Class<T>) {
    itemMeta = CraftItemStack.asBukkitCopy(
        CraftItemStack.asNMSCopy(this).apply {
            u()?.r(T::class.java.simpleName)
        }
    ).itemMeta
}