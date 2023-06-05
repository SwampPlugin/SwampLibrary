package com.cosine.library.util

import com.google.gson.Gson
import org.bukkit.craftbukkit.v1_19_R2.inventory.CraftItemStack
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

object ItemStackLocale {

    private var languageMap: MutableMap<String, String> = mutableMapOf()

    internal fun init(plugin: JavaPlugin) {
        try {
            plugin.getResource("ko_kr_19.json")?.use {
                val gson = Gson()
                val reader = InputStreamReader(it, StandardCharsets.UTF_8)
                languageMap = gson.fromJson(reader, MutableMap::class.java) as MutableMap<String, String>
            }
        } catch (_: Exception) {}
    }

    fun ItemStack.getDisplay(): String {
        if (this.hasItemMeta() && this.itemMeta.hasDisplayName()) {
            return this.itemMeta.displayName
        }
        try {
            val nmsItem = CraftItemStack.asNMSCopy(this)
            val item = nmsItem.c()
            val unlocalizedName = item.j(nmsItem)
            if (languageMap.containsKey(unlocalizedName)) {
                return languageMap[unlocalizedName] ?: this.type.name.lowercase()
            }
        } catch (_: Exception) {
            return this.type.name.lowercase()
        }
        return this.type.name.lowercase().replace("_", " ")
    }
}