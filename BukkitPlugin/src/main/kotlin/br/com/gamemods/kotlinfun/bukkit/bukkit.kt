@file:JvmName(name = "BukkitUtil")
package br.com.gamemods.kotlinfun.bukkit

import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

class KotlinFun : JavaPlugin() {
    companion object {
        lateinit var instance : KotlinFun
    }

    override fun onEnable() {
        instance = this
    }
}

fun Plugin.register(listener: Listener) = server.pluginManager.registerEvents(listener, this)
fun Plugin.register(listener: Listener, vararg listeners : Listener) {
    val manager = server.pluginManager
    manager.registerEvents(listener, this)
    for(other in listeners)
        manager.registerEvents(other, this)
}
