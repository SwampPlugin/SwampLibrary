package com.cosine.library.extension

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack

fun YamlConfiguration.getItemStackList(path: String): MutableList<ItemStack> {
    return getList(path) as MutableList<ItemStack>
}

fun ConfigurationSection.getItemStackList(path: String): MutableList<ItemStack> {
    return getList(path) as MutableList<ItemStack>
}