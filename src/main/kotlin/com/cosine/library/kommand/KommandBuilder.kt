package com.cosine.library.kommand

import com.cosine.library.extension.applyColor
import com.cosine.library.extension.async
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.command.PluginCommand
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.util.StringUtil
import java.lang.NullPointerException
import kotlin.reflect.KFunction
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.jvm.javaMethod
import kotlin.reflect.jvm.jvmErasure

abstract class KommandBuilder(
    private val plugin: JavaPlugin,
    prefix: String? = null
): CommandExecutor, TabCompleter {

    companion object {
        private const val NULLABLE_BOX = "[%s]"
        private const val NONNULL_BOX = "<%s>"
        private const val IS_CONSOLE_ERORR_MESSAGE = "argument type mismatch"
    }

    private var pluginCommand: PluginCommand
    var prefix = "§c§l[ ! ]§f"

    private val arguments = mutableMapOf<String, CommandArgument>()

    inner class CommandArgument(
        argument: String,
        val annotation: SubKommand,
        private val function: KFunction<Unit>
    ) {
        private val params = ArrayList<Pair<ArgumentAdapter<*>, Boolean>>()
        private var description = "$prefix /${pluginCommand.name} $argument"

        init {
            function.parameters.forEachIndexed { index, param ->
                if (index in 0 .. 1) {
                    return@forEachIndexed
                }

                val name = param.type.jvmErasure.simpleName
                    .run { if (this == "Int") "Integer" else this }
                val arg = ArgumentAdapter.getArgument(name)
                    ?.apply { params.add(this to !param.type.isMarkedNullable) }

                description +=
                    if (param.type.isMarkedNullable) {
                        " ${String.format(NULLABLE_BOX, arg?.label ?: "에러")}"
                    } else {
                        " ${String.format(NONNULL_BOX, arg?.label ?: "에러")}"
                    }
            }
            description += " : ${annotation.description}"
        }

        fun printDescription(receiver: CommandSender) = receiver.sendMessage(description)

        fun getArgument(sender: CommandSender, index: Int): ArgumentAdapter<*>? {
            return if (!sender.isOp && annotation.isOp) {
                null
            } else {
                params.getOrNull(index)?.first
            }
        }

        fun runArgument(sender: CommandSender, args: Array<String>) {
            if (annotation.isOp && !sender.isOp) {
                sender.sendMessage("$prefix 권한이 없습니다.")
                return
            }
            val arguments = ArrayList<Any?>()
            params.forEachIndexed { index, pair ->
                val param = if (args.size > index) pair.first.cast(args[index]) else null
                if (pair.second && param == null) {
                    sender.sendMessage("$prefix ${pair.first.label}(을)를 입력해주세요.")
                    return
                } else {
                    arguments.add(param)
                }
            }
            function.javaMethod?.invoke(this@KommandBuilder, sender, *arguments.toTypedArray())
        }
    }

    init {
        if (prefix != null) {
            this.prefix = prefix
        }
        val kommand = this::class.annotations.filterIsInstance<Kommand>().firstOrNull()
            ?: throw NullPointerException("Kommand Annotation이 등록되어 있지 않습니다.")
        kommand.apply {
            pluginCommand = plugin.getCommand(command)
                ?: throw NullPointerException("${command}가 등록되어 있지 않습니다.")
        }

        this::class.memberFunctions.filterIsInstance<KFunction<Unit>>().forEach { function ->
            function.annotations.filterIsInstance<SubKommand>().forEach { subKommand ->
                arguments[subKommand.argument] = CommandArgument(subKommand.argument, subKommand, function)
            }
        }
    }

    fun register() {
        pluginCommand.setExecutor(this)
        pluginCommand.tabCompleter = this
    }

    private fun printHelp(sender: CommandSender) {
        sender.sendMessage("$prefix ${pluginCommand.name} - ${pluginCommand.description.run { ifEmpty { "도움말" } }}".applyColor())
        sender.sendMessage("")
        arguments.values.sortedBy { it.annotation.helpPriority }.forEach {
            if (it.annotation.isOp && !sender.isOp) return@forEach
            it.printDescription(sender)
        }
    }

    open fun runDefaultCommand(sender: CommandSender) = printHelp(sender)

    open fun runDefaultCommand(player: Player) = printHelp(player)

    private fun onCommand(sender: CommandSender, args: Array<String>) {
        val kommandData = pluginCommand.run {
            if (args.isEmpty()) {
                KommandData(name, null, "/${name}", description)
            } else {
                val subCommands = args.copyOfRange(0, args.size).joinToString(" ")
                KommandData(name, args[0], "/${name} $subCommands", description)
            }
        }
        if (!kommandData.isSubCommand) {
            runDefaultCommand(sender)
            return
        }
        val argument = arguments[args[0]] ?: run {
            sender.sendMessage("$prefix 존재하지 않는 명령어입니다.")
            return
        }
        if (argument.annotation.async) {
            async { argument.runArgument(sender, args.copyOfRange(1, args.size)) }
        } else {
            argument.runArgument(sender, args.copyOfRange(1, args.size))
        }
    }

    open fun tabComplete(sender: CommandSender, args: Array<String>): List<String>? {
        return when(args.size) {
            in 0..1 -> {
                val tabList = arguments.filter { (_, v) ->
                    !(v.annotation.isOp && !sender.isOp || v.annotation.hide)
                }.keys.toList()
                StringUtil.copyPartialMatches(args[0], tabList, ArrayList())
            }
            else -> {
                val index = args.size - 2
                val target = arguments[args[0]]
                if (target == null) null
                else {
                    val tab = target.getArgument(sender, index)
                    if (tab == null) {
                        emptyList()
                    } else {
                        tab.tabCompleter.run {
                            invoke()?.run {
                                StringUtil.copyPartialMatches(args[args.size - 1], this, ArrayList())
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        runCatching {
            onCommand(sender, args)
        }.onFailure { throwable ->
            if (throwable is IllegalArgumentException && throwable.message == IS_CONSOLE_ERORR_MESSAGE) {
                sender.sendMessage("$prefix 콘솔에서 실행할 수 없는 명령어입니다.")
                return true
            }
            sender.sendMessage(
                "$prefix 명령어 실행에 실패하였습니다.",
                "§7└ $throwable"
            )
            throwable.stackTrace.forEach {
                plugin.logger.warning("$it")
            }
        }
        return true
    }

    override fun onTabComplete(sender: CommandSender, cmd: Command, label: String, args: Array<String>): List<String>? {
        return if (sender is Player) tabComplete(sender, args) else null
    }

}