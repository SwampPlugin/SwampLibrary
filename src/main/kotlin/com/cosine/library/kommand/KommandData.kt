package com.cosine.library.kommand

data class KommandData(
    val command: String,
    val subCommand: String?,
    val line: String,
    val description: String
) {
    val isSubCommand = subCommand != null
}