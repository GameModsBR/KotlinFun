package br.com.gamemods.kotlinfun.bungee

import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.plugin.Command
import net.md_5.bungee.api.plugin.TabExecutor

abstract class KotlinCommand(name: String, permission: String? = null, vararg aliases: String) : Command(name, permission, *aliases), TabExecutor {
    abstract override fun execute(sender: CommandSender, vararg args: String)

    override fun onTabComplete(sender: CommandSender, vararg args: String): MutableIterable<String> {
        return mutableSetOf()
    }
}
