package com.cosine.library.kommand

object KommandManager {

    fun registerAdapter(vararg adapter: ArgumentAdapter<*>) = adapter.forEach(ArgumentAdapter.Companion::registerAdapter)
    fun registerCommand(vararg command: KommandBuilder) = command.forEach(KommandBuilder::register)

}