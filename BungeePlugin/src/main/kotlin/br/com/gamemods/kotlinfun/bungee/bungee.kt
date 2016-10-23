@file:JvmName(name = "BungeeUtil")
package br.com.gamemods.kotlinfun.bungee

import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.plugin.Command
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.api.plugin.Plugin

class KotlinFun : Plugin()

fun Plugin.register(listener: Listener) = proxy.pluginManager.registerListener(this, listener)
fun Plugin.unregister(listener: Listener) = proxy.pluginManager.unregisterListener(listener)
fun Listener.unregister() = ProxyServer.getInstance().pluginManager.unregisterListener(this)
fun Plugin.unregisterListeners() = proxy.pluginManager.unregisterListeners(this)
fun Plugin.register(listener: Listener, vararg listeners: Listener) {
    val manager = proxy.pluginManager
    manager.registerListener(this, listener)
    for(other in listeners)
        manager.registerListener(this, other)
}
fun Plugin.unregister(listener: Listener, vararg listeners: Listener) {
    val manager = proxy.pluginManager
    manager.unregisterListener(listener)
    for(other in listeners)
        manager.unregisterListener(other)
}
fun Plugin.register(command: Command) = proxy.pluginManager.registerCommand(this, command)
fun Plugin.register(command: Command, vararg commands: Command) {
    val manager = proxy.pluginManager
    manager.registerCommand(this, command)
    for(other in commands)
        manager.registerCommand(this, command)
}
fun Plugin.unregister(command: Command) = proxy.pluginManager.unregisterCommand(command)
fun Command.unregister() = ProxyServer.getInstance().pluginManager.unregisterCommand(this)
fun Plugin.unregisterCommands() = proxy.pluginManager.unregisterCommands(this)
