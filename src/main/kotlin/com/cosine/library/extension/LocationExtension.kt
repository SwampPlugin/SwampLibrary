package com.cosine.library.extension

import org.bukkit.Location
import org.bukkit.Material

fun Location.setAir() {
    block.type = Material.AIR
}