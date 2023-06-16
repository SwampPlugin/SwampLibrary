package com.cosine.library.sign

import com.cosine.library.SwampLibrary.Companion.plugin
import com.cosine.library.extension.later
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Sign
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.SignChangeEvent
import java.util.UUID

class ExtraSign(
    val player: Player,
    val texts: Array<String>,
    val response: (Array<String>) -> Boolean
) {

    companion object {
        private val receivers = mutableMapOf<UUID, ExtraSign>()

        fun listener() {
            plugin.server.pluginManager.registerEvents(object : Listener {
                @EventHandler
                fun onSignEdit(event: SignChangeEvent) {
                    val player = event.player
                    val extraSign = receivers.remove(player.uniqueId) ?: return

                    if (extraSign.response(event.lines)) {
                        extraSign.signLocation.block.type = extraSign.originalBlock
                    } else {
                        later { extraSign.open() }
                    }
                }
            }, plugin)
        }
    }

    private val playerLocation = player.location

    private val signLocation = Location(player.world, playerLocation.x, playerLocation.y, playerLocation.z)
    private val signBlock = signLocation.block

    private var originalBlock = signBlock.type

    fun open() {
        signBlock.type = Material.OAK_SIGN

        val state = signBlock.state
        val sign = state as Sign

        texts.forEachIndexed { index, text ->
            state.setLine(index, text)
        }
        sign.update(true)

        later(2) {
            player.openSign(sign)
            receivers[player.uniqueId] = this
        }
    }
}