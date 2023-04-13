package com.cosine.library.extension

import com.google.gson.Gson
import net.minecraft.nbt.NBTTagCompound
import org.bukkit.craftbukkit.v1_19_R2.inventory.CraftItemStack
import org.bukkit.inventory.ItemStack
import kotlin.reflect.KClass

fun ItemStack.amount(amount: Int = 1): ItemStack {
    this.amount = amount
    return this
}

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

inline fun <reified T : Any> ItemStack.removeNBTTag(clazz: KClass<T>) {
    itemMeta = CraftItemStack.asBukkitCopy(
        CraftItemStack.asNMSCopy(this).apply {
            u()?.r(T::class.java.simpleName)
        }
    ).itemMeta
}